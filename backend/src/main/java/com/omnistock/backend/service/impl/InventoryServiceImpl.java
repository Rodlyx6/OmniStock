package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.omnistock.backend.constant.BusinessConstant;
import com.omnistock.backend.constant.ErrorCode;
import com.omnistock.backend.domain.dto.DeductInventoryDTO;
import com.omnistock.backend.domain.dto.IncreaseInventoryDTO;
import com.omnistock.backend.domain.entity.Inventory;
import com.omnistock.backend.domain.entity.InventoryFlow;
import com.omnistock.backend.domain.vo.InventoryOperationVO;
import com.omnistock.backend.domain.vo.InventoryVO;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.mapper.InventoryFlowMapper;
import com.omnistock.backend.mapper.InventoryMapper;
import com.omnistock.backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 库存Service实现
 *
 * 防超卖机制：先用乐观锁，失败后用Redis分布式锁重试
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final InventoryFlowMapper inventoryFlowMapper;
    private final RedissonClient redissonClient;

    @Override
    public IPage<InventoryVO> queryInventories(int page, int pageSize, Long skuId, Long warehouseId) {
        Page<Inventory> pageObj = new Page<>(page, pageSize);
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        if (skuId != null) wrapper.eq(Inventory::getSkuId, skuId);
        return inventoryMapper.selectPage(pageObj, wrapper).convert(this::toVO);
    }

    @Override
    public InventoryVO getInventoryDetail(Long inventoryId) {
        Inventory inventory = inventoryMapper.selectById(inventoryId);
        if (inventory == null) {
            throw new BusinessException(ErrorCode.INVENTORY_INSUFFICIENT, "库存不存在");
        }
        return toVO(inventory);
    }

    @Override
    public Map<String, Object> getSkuTotalInventory(Long skuId) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getSkuId, skuId);
        List<Inventory> inventories = inventoryMapper.selectList(wrapper);

        if (inventories.isEmpty()) {
            throw new BusinessException(ErrorCode.INVENTORY_INSUFFICIENT, "该SKU无库存记录");
        }

        int totalQty      = inventories.stream().mapToInt(Inventory::getQuantity).sum();
        int totalReserved = inventories.stream().mapToInt(Inventory::getReservedQuantity).sum();
        int totalAvail    = inventories.stream().mapToInt(Inventory::getAvailableQuantity).sum();

        List<Map<String, Object>> locations = inventories.stream().map(inv -> Map.<String, Object>of(
                "locationId",        inv.getLocationId(),
                "quantity",          inv.getQuantity(),
                "reservedQuantity",  inv.getReservedQuantity(),
                "availableQuantity", inv.getAvailableQuantity()
        )).toList();

        return Map.of(
                "skuId",                  skuId,
                "totalQuantity",          totalQty,
                "totalReservedQuantity",  totalReserved,
                "totalAvailableQuantity", totalAvail,
                "locations",              locations
        );
    }

    @Override
    @Transactional
    public InventoryOperationVO deductInventory(DeductInventoryDTO dto) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getSkuId, dto.getSkuId());
        Inventory inventory = inventoryMapper.selectOne(wrapper);

        if (inventory == null) {
            throw new BusinessException(ErrorCode.INVENTORY_INSUFFICIENT, "库存不存在");
        }
        if (inventory.getAvailableQuantity() < dto.getQuantity()) {
            throw new BusinessException(ErrorCode.INVENTORY_INSUFFICIENT,
                    "库存不足，可用库存: " + inventory.getAvailableQuantity());
        }

        // 乐观锁扣减
        int updated = inventoryMapper.deductInventory(dto.getSkuId(), dto.getQuantity(), inventory.getVersion());
        if (updated == 0) {
            // 乐观锁失败 → Redis分布式锁重试
            inventory = retryDeductWithLock(dto, wrapper);
        } else {
            log.info("库存扣减成功(乐观锁): skuId={}, qty={}", dto.getSkuId(), dto.getQuantity());
        }

        InventoryFlow flow = saveFlow(inventory, -dto.getQuantity(),
                BusinessConstant.FLOW_OUTBOUND, dto.getReferenceNo(), dto.getOperatorId(), dto.getRemark());

        return InventoryOperationVO.builder()
                .inventoryId(inventory.getInventoryId())
                .skuId(inventory.getSkuId())
                .flowId(flow.getFlowId())
                .beforeQuantity(inventory.getQuantity())
                .afterQuantity(inventory.getQuantity() - dto.getQuantity())
                .changeQuantity(-dto.getQuantity())
                .version(inventory.getVersion() + 1)
                .createdAt(flow.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public InventoryOperationVO increaseInventory(IncreaseInventoryDTO dto) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getSkuId, dto.getSkuId())
               .eq(Inventory::getLocationId, dto.getLocationId());
        Inventory inventory = inventoryMapper.selectOne(wrapper);

        int beforeQty;
        if (inventory == null) {
            // 初次入库，创建库存记录
            inventory = Inventory.builder()
                    .skuId(dto.getSkuId())
                    .locationId(dto.getLocationId())
                    .quantity(dto.getQuantity())
                    .reservedQuantity(0)
                    .availableQuantity(dto.getQuantity())
                    .version(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            inventoryMapper.insert(inventory);
            beforeQty = 0;
        } else {
            beforeQty = inventory.getQuantity();
            inventoryMapper.increaseInventory(dto.getSkuId(), dto.getLocationId(), dto.getQuantity());
        }

        InventoryFlow flow = saveFlow(inventory, dto.getQuantity(),
                BusinessConstant.FLOW_INBOUND, dto.getReferenceNo(), dto.getOperatorId(), dto.getRemark());

        log.info("库存增加成功: skuId={}, qty={}", dto.getSkuId(), dto.getQuantity());
        return InventoryOperationVO.builder()
                .inventoryId(inventory.getInventoryId())
                .skuId(inventory.getSkuId())
                .flowId(flow.getFlowId())
                .beforeQuantity(beforeQty)
                .afterQuantity(beforeQty + dto.getQuantity())
                .changeQuantity(dto.getQuantity())
                .version(inventory.getVersion() + 1)
                .createdAt(flow.getCreatedAt())
                .build();
    }

    // ── 私有方法 ────────────────────────────────────────────────────────────

    /**
     * 乐观锁失败后，持有Redis分布式锁重试扣减
     */
    private Inventory retryDeductWithLock(DeductInventoryDTO dto,
                                          LambdaQueryWrapper<Inventory> wrapper) {
        String lockKey = "lock:inventory:" + dto.getSkuId();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(5, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(ErrorCode.INVENTORY_VERSION_CONFLICT, "库存版本冲突，请重试");
            }
            Inventory fresh = inventoryMapper.selectOne(wrapper);
            if (fresh.getAvailableQuantity() < dto.getQuantity()) {
                throw new BusinessException(ErrorCode.INVENTORY_INSUFFICIENT, "库存不足");
            }
            int updated = inventoryMapper.deductInventory(dto.getSkuId(), dto.getQuantity(), fresh.getVersion());
            if (updated == 0) {
                throw new BusinessException(ErrorCode.INVENTORY_VERSION_CONFLICT, "库存版本冲突，请重试");
            }
            log.info("库存扣减成功(分布式锁): skuId={}, qty={}", dto.getSkuId(), dto.getQuantity());
            return fresh;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取锁超时，请重试");
        } finally {
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    /**
     * 记录库存流水
     */
    private InventoryFlow saveFlow(Inventory inventory, int quantityChange, String flowType,
                                   String referenceNo, Long operatorId, String remark) {
        InventoryFlow flow = InventoryFlow.builder()
                .skuId(inventory.getSkuId())
                .inventoryId(inventory.getInventoryId())
                .flowType(flowType)
                .quantityChange(quantityChange)
                .beforeQuantity(inventory.getQuantity())
                .afterQuantity(inventory.getQuantity() + quantityChange)
                .referenceNo(referenceNo)
                .operatorId(operatorId)
                .remark(remark)
                .createdAt(LocalDateTime.now())
                .build();
        inventoryFlowMapper.insert(flow);
        return flow;
    }

    /**
     * Entity → VO
     */
    private InventoryVO toVO(Inventory inv) {
        return InventoryVO.builder()
                .inventoryId(inv.getInventoryId())
                .skuId(inv.getSkuId())
                .locationId(inv.getLocationId())
                .quantity(inv.getQuantity())
                .reservedQuantity(inv.getReservedQuantity())
                .availableQuantity(inv.getAvailableQuantity())
                .version(inv.getVersion())
                .createdAt(inv.getCreatedAt())
                .updatedAt(inv.getUpdatedAt())
                .build();
    }
}
