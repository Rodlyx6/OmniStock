package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.omnistock.backend.domain.entity.Inbound;
import com.omnistock.backend.domain.entity.InboundItem;
import com.omnistock.backend.domain.dto.CreateInboundDTO;
import com.omnistock.backend.domain.dto.IncreaseInventoryDTO;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.constant.ErrorCode;
import com.omnistock.backend.constant.BusinessConstant;
import com.omnistock.backend.mapper.InboundMapper;
import com.omnistock.backend.mapper.InboundItemMapper;
import com.omnistock.backend.service.InboundService;
import com.omnistock.backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 入库Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InboundServiceImpl implements InboundService {

    private final InboundMapper inboundMapper;
    private final InboundItemMapper inboundItemMapper;
    private final InventoryService inventoryService;

    @Override
    public IPage<Inbound> queryInbound(int page, int pageSize, String status, Long warehouseId) {
        Page<Inbound> pageObj = new Page<>(page, pageSize);
        LambdaQueryWrapper<Inbound> wrapper = new LambdaQueryWrapper<>();
        
        if (status != null) {
            wrapper.eq(Inbound::getStatus, status);
        }
        if (warehouseId != null) {
            wrapper.eq(Inbound::getWarehouseId, warehouseId);
        }
        
        wrapper.orderByDesc(Inbound::getCreatedAt);
        return inboundMapper.selectPage(pageObj, wrapper);
    }

    @Override
    public Inbound getInboundDetail(Long inboundId) {
        Inbound inbound = inboundMapper.selectById(inboundId);
        if (inbound == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "入库单不存在");
        }
        return inbound;
    }

    @Override
    @Transactional
    public Inbound createInbound(CreateInboundDTO dto) {
        String inboundNo = "IN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
        
        Inbound inbound = Inbound.builder()
                .inboundNo(inboundNo)
                .supplierId(dto.getSupplierId())
                .warehouseId(dto.getWarehouseId())
                .status(BusinessConstant.INBOUND_STATUS_PENDING)
                .totalQuantity(dto.getItems().stream().mapToInt(CreateInboundDTO.InboundItemDTO::getQuantity).sum())
                .operatorId(dto.getOperatorId())
                .remark(dto.getRemark())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        inboundMapper.insert(inbound);

        for (CreateInboundDTO.InboundItemDTO item : dto.getItems()) {
            InboundItem inboundItem = InboundItem.builder()
                    .inboundId(inbound.getInboundId())
                    .skuId(item.getSkuId())
                    .quantity(item.getQuantity())
                    .receivedQuantity(0)
                    .status(BusinessConstant.INBOUND_ITEM_STATUS_PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            inboundItemMapper.insert(inboundItem);
        }

        log.info("创建入库单成功: inboundNo={}", inboundNo);
        return inbound;
    }

    @Override
    @Transactional
    public void receiveInbound(Long inboundId, CreateInboundDTO.ReceiveDTO dto) {
        Inbound inbound = inboundMapper.selectById(inboundId);
        if (inbound == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "入库单不存在");
        }

        if (!BusinessConstant.INBOUND_STATUS_PENDING.equals(inbound.getStatus())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "入库单状态错误");
        }

        for (CreateInboundDTO.ReceiveItemDTO receiveItem : dto.getItems()) {
            InboundItem inboundItem = inboundItemMapper.selectById(receiveItem.getItemId());
            if (inboundItem == null) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "入库明细不存在");
            }

            if (receiveItem.getReceivedQuantity() > inboundItem.getQuantity()) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "收货数量超过计划数量");
            }

            // 增加库存
            inventoryService.increaseInventory(
                com.omnistock.backend.domain.dto.IncreaseInventoryDTO.builder()
                    .skuId(inboundItem.getSkuId())
                    .locationId(receiveItem.getLocationId())
                    .quantity(receiveItem.getReceivedQuantity())
                    .referenceNo(inbound.getInboundNo())
                    .operatorId(dto.getOperatorId())
                    .remark("入库收货")
                    .build()
            );

            inboundItem.setReceivedQuantity(receiveItem.getReceivedQuantity());
            inboundItem.setStatus(BusinessConstant.INBOUND_ITEM_STATUS_RECEIVED);
            inboundItemMapper.updateById(inboundItem);
        }

        inbound.setStatus(BusinessConstant.INBOUND_STATUS_COMPLETED);
        inbound.setInboundTime(LocalDateTime.now());
        inbound.setUpdatedAt(LocalDateTime.now());
        inboundMapper.updateById(inbound);

        log.info("入库单收货完成: inboundNo={}", inbound.getInboundNo());
    }

    @Override
    @Transactional
    public void cancelInbound(Long inboundId, Long operatorId, String remark) {
        Inbound inbound = inboundMapper.selectById(inboundId);
        if (inbound == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "入库单不存在");
        }

        if (!BusinessConstant.INBOUND_STATUS_PENDING.equals(inbound.getStatus())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "只有待入库状态才能取消");
        }

        inbound.setStatus(BusinessConstant.INBOUND_STATUS_CANCELLED);
        inbound.setUpdatedAt(LocalDateTime.now());
        inboundMapper.updateById(inbound);

        log.info("入库单已取消: inboundNo={}", inbound.getInboundNo());
    }
}
