package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.omnistock.backend.domain.dto.InboundCreateRequest;
import com.omnistock.backend.domain.dto.OutboundCreateRequest;
import com.omnistock.backend.domain.dto.StockItem;
import com.omnistock.backend.domain.entity.InboundRecord;
import com.omnistock.backend.domain.entity.Inventory;
import com.omnistock.backend.domain.entity.InventoryLog;
import com.omnistock.backend.domain.entity.Location;
import com.omnistock.backend.domain.entity.OutboundRecord;
import com.omnistock.backend.domain.entity.Sku;
import com.omnistock.backend.domain.vo.InventoryLogVO;
import com.omnistock.backend.domain.vo.InventoryVO;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.exception.ErrorCode;
import com.omnistock.backend.mapper.InboundRecordMapper;
import com.omnistock.backend.mapper.InventoryLogMapper;
import com.omnistock.backend.mapper.InventoryMapper;
import com.omnistock.backend.mapper.LocationMapper;
import com.omnistock.backend.mapper.OutboundRecordMapper;
import com.omnistock.backend.mapper.SkuMapper;
import com.omnistock.backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final String CHANGE_TYPE_INBOUND = "INBOUND";
    private static final String CHANGE_TYPE_OUTBOUND = "OUTBOUND";

    private final InventoryMapper inventoryMapper;
    private final InventoryLogMapper inventoryLogMapper;
    private final InboundRecordMapper inboundRecordMapper;
    private final OutboundRecordMapper outboundRecordMapper;
    private final LocationMapper locationMapper;
    private final SkuMapper skuMapper;

    // ------------------------------------------------------------------ inbound

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String inbound(InboundCreateRequest request) {
        String bizId = buildBizId(request.getBizId(), "IN");
        for (StockItem item : request.getItems()) {
            Sku sku = getSkuOrThrow(item.getSkuId());
            Location location = getLocationOrThrow(item.getLocationId());

            Inventory inventory = getInventoryRow(item.getSkuId(), item.getLocationId());
            if (inventory == null) {
                Inventory newRow = new Inventory();
                newRow.setSkuId(item.getSkuId());
                newRow.setLocationId(item.getLocationId());
                newRow.setQuantity(item.getQuantity());
                newRow.setVersion(0);
                inventoryMapper.insert(newRow);
            } else {
                int oldVersion = inventory.getVersion();
                int newQty = inventory.getQuantity() + item.getQuantity();
                int updated = inventoryMapper.update(null,
                        new LambdaUpdateWrapper<Inventory>()
                                .eq(Inventory::getId, inventory.getId())
                                .eq(Inventory::getVersion, oldVersion)
                                .set(Inventory::getQuantity, newQty)
                                .set(Inventory::getVersion, oldVersion + 1)
                                .set(Inventory::getUpdatedTime, LocalDateTime.now()));
                if (updated <= 0) {
                    throw new BusinessException(ErrorCode.STOCK_VERSION_CONFLICT, "库存版本冲突，请重试");
                }
            }

            updateLocationCapacity(location, sku, item.getQuantity(), true);
            insertInboundRecord(request.getOperatorId(), bizId, item);
            insertInventoryLog(request.getOperatorId(), bizId, item, CHANGE_TYPE_INBOUND);
        }
        return bizId;
    }

    // ------------------------------------------------------------------ outbound

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String outbound(OutboundCreateRequest request) {
        String bizId = buildBizId(request.getBizId(), "OUT");
        for (StockItem item : request.getItems()) {
            Sku sku = getSkuOrThrow(item.getSkuId());
            Location location = getLocationOrThrow(item.getLocationId());

            Inventory inventory = getInventoryRow(item.getSkuId(), item.getLocationId());
            if (inventory == null) {
                throw new BusinessException(ErrorCode.STOCK_NOT_FOUND, "库存记录不存在");
            }
            if (inventory.getQuantity() < item.getQuantity()) {
                throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH, "库存不足，当前库存：" + inventory.getQuantity());
            }
            int oldVersion = inventory.getVersion();
            int newQty = inventory.getQuantity() - item.getQuantity();
            if (newQty == 0) {
                inventoryMapper.deleteById(inventory.getId());
            } else {
                int updated = inventoryMapper.update(null,
                        new LambdaUpdateWrapper<Inventory>()
                                .eq(Inventory::getId, inventory.getId())
                                .eq(Inventory::getVersion, oldVersion)
                                .set(Inventory::getQuantity, newQty)
                                .set(Inventory::getVersion, oldVersion + 1)
                                .set(Inventory::getUpdatedTime, LocalDateTime.now()));
                if (updated <= 0) {
                    throw new BusinessException(ErrorCode.STOCK_VERSION_CONFLICT, "库存版本冲突，请重试");
                }
            }

            updateLocationCapacity(location, sku, item.getQuantity(), false);
            insertOutboundRecord(request.getOperatorId(), bizId, item);
            insertInventoryLog(request.getOperatorId(), bizId, item, CHANGE_TYPE_OUTBOUND);
        }
        return bizId;
    }

    // ------------------------------------------------------------------ query

    @Override
    public List<InventoryVO> list(Long skuId, Long locationId) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<Inventory>()
                .eq(skuId != null, Inventory::getSkuId, skuId)
                .eq(locationId != null, Inventory::getLocationId, locationId)
                .gt(Inventory::getQuantity, 0)
                .orderByDesc(Inventory::getUpdatedTime);
        List<Inventory> inventories = inventoryMapper.selectList(wrapper);
        return enrichInventoryList(inventories);
    }

    @Override
    public List<InventoryLogVO> listLogs(Long skuId, String changeType) {
        LambdaQueryWrapper<InventoryLog> wrapper = new LambdaQueryWrapper<InventoryLog>()
                .eq(skuId != null, InventoryLog::getSkuId, skuId)
                .eq(StringUtils.hasText(changeType), InventoryLog::getChangeType, changeType)
                .orderByDesc(InventoryLog::getCreatedTime);
        List<InventoryLog> logs = inventoryLogMapper.selectList(wrapper);
        return enrichLogList(logs);
    }

    // ------------------------------------------------------------------ private helpers

    private Inventory getInventoryRow(Long skuId, Long locationId) {
        return inventoryMapper.selectOne(
                new LambdaQueryWrapper<Inventory>()
                        .eq(Inventory::getSkuId, skuId)
                        .eq(Inventory::getLocationId, locationId)
                        .last("limit 1"));
    }

    private Sku getSkuOrThrow(Long skuId) {
        Sku sku = skuMapper.selectById(skuId);
        if (sku == null) {
            throw new BusinessException(ErrorCode.SKU_NOT_FOUND, "SKU不存在，id=" + skuId);
        }
        return sku;
    }

    private Location getLocationOrThrow(Long locationId) {
        Location location = locationMapper.selectById(locationId);
        if (location == null) {
            throw new BusinessException(ErrorCode.LOCATION_NOT_FOUND, "库位不存在，id=" + locationId);
        }
        return location;
    }

    private void updateLocationCapacity(Location location, Sku sku, int quantity, boolean inbound) {
        int currentCapacity = location.getCurrentCapacity() == null ? 0 : location.getCurrentCapacity();
        int deltaCapacity = inbound ? quantity : -quantity;
        int nextCapacity = currentCapacity + deltaCapacity;
        if (nextCapacity < 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "库位当前容量不足");
        }
        if (location.getMaxCapacity() != null && nextCapacity > location.getMaxCapacity()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "库位容量超限");
        }

        double currentWeight = location.getCurrentWeight() == null ? 0D : location.getCurrentWeight();
        double skuWeight = sku.getWeight() == null ? 0D : sku.getWeight();
        double deltaWeight = (inbound ? 1 : -1) * skuWeight * quantity;
        double nextWeight = currentWeight + deltaWeight;
        if (nextWeight < 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "库位当前承重不足");
        }
        if (location.getMaxWeight() != null && nextWeight > location.getMaxWeight()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "库位承重超限");
        }

        location.setCurrentCapacity(nextCapacity);
        location.setCurrentWeight(nextWeight);
        locationMapper.updateById(location);
    }

    private void insertInboundRecord(Long operatorId, String bizId, StockItem item) {
        InboundRecord record = new InboundRecord();
        record.setBizId(bizId);
        record.setSkuId(item.getSkuId());
        record.setLocationId(item.getLocationId());
        record.setQuantity(item.getQuantity());
        record.setOperatorId(operatorId);
        inboundRecordMapper.insert(record);
    }

    private void insertOutboundRecord(Long operatorId, String bizId, StockItem item) {
        OutboundRecord record = new OutboundRecord();
        record.setBizId(bizId);
        record.setSkuId(item.getSkuId());
        record.setLocationId(item.getLocationId());
        record.setQuantity(item.getQuantity());
        record.setOperatorId(operatorId);
        outboundRecordMapper.insert(record);
    }

    private void insertInventoryLog(Long operatorId, String bizId, StockItem item, String changeType) {
        InventoryLog log = new InventoryLog();
        log.setSkuId(item.getSkuId());
        log.setLocationId(item.getLocationId());
        log.setChangeQty(CHANGE_TYPE_INBOUND.equals(changeType) ? item.getQuantity() : -item.getQuantity());
        log.setChangeType(changeType);
        log.setBizId(bizId);
        log.setOperatorId(operatorId);
        inventoryLogMapper.insert(log);
    }

    private List<InventoryVO> enrichInventoryList(List<Inventory> inventories) {
        if (inventories.isEmpty()) {
            return List.of();
        }
        Set<Long> skuIds = inventories.stream().map(Inventory::getSkuId).collect(Collectors.toSet());
        Set<Long> locIds = inventories.stream().map(Inventory::getLocationId).collect(Collectors.toSet());

        Map<Long, Sku> skuMap = skuMapper.selectList(
                new LambdaQueryWrapper<Sku>().in(Sku::getId, skuIds))
                .stream().collect(Collectors.toMap(Sku::getId, Function.identity()));

        Map<Long, Location> locMap = locationMapper.selectList(
                new LambdaQueryWrapper<Location>().in(Location::getId, locIds))
                .stream().collect(Collectors.toMap(Location::getId, Function.identity()));

        return inventories.stream().map(inv -> {
            InventoryVO vo = new InventoryVO();
            vo.setId(inv.getId());
            vo.setSkuId(inv.getSkuId());
            vo.setLocationId(inv.getLocationId());
            vo.setQuantity(inv.getQuantity());
            vo.setUpdatedTime(inv.getUpdatedTime());
            Sku sku = skuMap.get(inv.getSkuId());
            if (sku != null) {
                vo.setSkuName(sku.getName());
                vo.setSkuCode(sku.getCode());
            }
            Location loc = locMap.get(inv.getLocationId());
            if (loc != null) {
                vo.setLocationCode(loc.getCode());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    private List<InventoryLogVO> enrichLogList(List<InventoryLog> logs) {
        if (logs.isEmpty()) {
            return List.of();
        }
        Set<Long> skuIds = logs.stream().map(InventoryLog::getSkuId).collect(Collectors.toSet());
        Set<Long> locIds = logs.stream().map(InventoryLog::getLocationId).collect(Collectors.toSet());

        Map<Long, Sku> skuMap = skuMapper.selectList(
                new LambdaQueryWrapper<Sku>().in(Sku::getId, skuIds))
                .stream().collect(Collectors.toMap(Sku::getId, Function.identity()));

        Map<Long, Location> locMap = locationMapper.selectList(
                new LambdaQueryWrapper<Location>().in(Location::getId, locIds))
                .stream().collect(Collectors.toMap(Location::getId, Function.identity()));

        return logs.stream().map(log -> {
            InventoryLogVO vo = new InventoryLogVO();
            vo.setId(log.getId());
            vo.setSkuId(log.getSkuId());
            vo.setLocationId(log.getLocationId());
            vo.setChangeQty(log.getChangeQty());
            vo.setChangeType(log.getChangeType());
            vo.setBizId(log.getBizId());
            vo.setOperatorId(log.getOperatorId());
            vo.setCreatedTime(log.getCreatedTime());
            Sku sku = skuMap.get(log.getSkuId());
            if (sku != null) {
                vo.setSkuName(sku.getName());
            }
            Location loc = locMap.get(log.getLocationId());
            if (loc != null) {
                vo.setLocationCode(loc.getCode());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    private String buildBizId(String bizId, String prefix) {
        if (StringUtils.hasText(bizId)) {
            return bizId;
        }
        return prefix + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
