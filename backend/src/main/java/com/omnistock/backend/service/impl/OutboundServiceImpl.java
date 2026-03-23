package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.constant.BusinessConstant;
import com.omnistock.backend.constant.ErrorCode;
import com.omnistock.backend.domain.dto.CreateOutboundDTO;
import com.omnistock.backend.domain.dto.DeductInventoryDTO;
import com.omnistock.backend.domain.dto.PickOutboundDTO;
import com.omnistock.backend.domain.entity.Outbound;
import com.omnistock.backend.domain.entity.OutboundItem;
import com.omnistock.backend.domain.vo.OutboundVO;
import com.omnistock.backend.mapper.OutboundItemMapper;
import com.omnistock.backend.mapper.OutboundMapper;
import com.omnistock.backend.service.InventoryService;
import com.omnistock.backend.service.OutboundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 出库Service实现类
 * 
 * 核心职责：
 * - 创建出库单
 * - 确认出库（拣货）
 * - 取消出库单
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OutboundServiceImpl implements OutboundService {

    private final OutboundMapper outboundMapper;
    private final OutboundItemMapper outboundItemMapper;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public OutboundVO createOutbound(CreateOutboundDTO dto) {
        // 第一步：检查库存是否充足（通过InventoryService）
        for (CreateOutboundDTO.OutboundItemDTO item : dto.getItems()) {
            try {
                inventoryService.getSkuTotalInventory(item.getSkuId());
            } catch (BusinessException e) {
                throw new BusinessException(ErrorCode.OUTBOUND_INSUFFICIENT_INVENTORY,
                        "库存不存在，SKU: " + item.getSkuId());
            }
            // 获取总库存并检查可用量
            java.util.Map<String, Object> totalInv = inventoryService.getSkuTotalInventory(item.getSkuId());
            int available = (int) totalInv.get("totalAvailableQuantity");
            if (available < item.getQuantity()) {
                throw new BusinessException(ErrorCode.OUTBOUND_INSUFFICIENT_INVENTORY,
                        "库存不足，SKU: " + item.getSkuId() + "，可用库存: " + available);
            }
        }

        // 第二步：创建出库单
        String outboundNo = "OUT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
        Outbound outbound = Outbound.builder()
                .outboundNo(outboundNo)
                .warehouseId(dto.getWarehouseId())
                .status(BusinessConstant.OUTBOUND_PENDING)
                .totalQuantity(dto.getItems().stream().mapToInt(CreateOutboundDTO.OutboundItemDTO::getQuantity).sum())
                .operatorId(dto.getOperatorId())
                .remark(dto.getRemark())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        outboundMapper.insert(outbound);

        // 第三步：创建出库明细
        for (CreateOutboundDTO.OutboundItemDTO item : dto.getItems()) {
            OutboundItem outboundItem = OutboundItem.builder()
                    .outboundId(outbound.getOutboundId())
                    .skuId(item.getSkuId())
                    .quantity(item.getQuantity())
                    .pickedQuantity(0)
                    .status("PENDING")
                    .createdAt(LocalDateTime.now())
                    .build();
            outboundItemMapper.insert(outboundItem);
        }

        log.info("创建出库单成功: outboundNo={}", outboundNo);
        return getOutboundDetail(outbound.getOutboundId());
    }

    @Override
    public IPage<OutboundVO> queryOutbounds(int page, int pageSize, String status, Long warehouseId) {
        Page<Outbound> pageObj = new Page<>(page, pageSize);
        LambdaQueryWrapper<Outbound> wrapper = new LambdaQueryWrapper<>();
        
        if (status != null) {
            wrapper.eq(Outbound::getStatus, status);
        }
        if (warehouseId != null) {
            wrapper.eq(Outbound::getWarehouseId, warehouseId);
        }
        
        IPage<Outbound> result = outboundMapper.selectPage(pageObj, wrapper);
        return result.convert(this::convertToVO);
    }

    @Override
    public OutboundVO getOutboundDetail(Long outboundId) {
        Outbound outbound = outboundMapper.selectById(outboundId);
        if (outbound == null) {
            throw new BusinessException(ErrorCode.OUTBOUND_NOT_FOUND, "出库单不存在");
        }

        // 查询出库明细
        LambdaQueryWrapper<OutboundItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutboundItem::getOutboundId, outboundId);
        List<OutboundItem> items = outboundItemMapper.selectList(wrapper);

        return convertToVO(outbound, items);
    }

    @Override
    @Transactional
    public OutboundVO pickOutbound(Long outboundId, PickOutboundDTO dto) {
        // 第一步：验证出库单状态
        Outbound outbound = outboundMapper.selectById(outboundId);
        if (outbound == null) {
            throw new BusinessException(ErrorCode.OUTBOUND_NOT_FOUND, "出库单不存在");
        }

        if (!BusinessConstant.OUTBOUND_PENDING.equals(outbound.getStatus())) {
            throw new BusinessException(ErrorCode.OUTBOUND_STATUS_ERROR, "出库单状态错误");
        }

        // 第二步：扣减库存
        for (PickOutboundDTO.PickItemDTO pickItem : dto.getItems()) {
            OutboundItem outboundItem = outboundItemMapper.selectById(pickItem.getItemId());
            if (outboundItem == null) {
                throw new BusinessException(ErrorCode.OUTBOUND_NOT_FOUND, "出库明细不存在");
            }

            if (pickItem.getPickedQuantity() > outboundItem.getQuantity()) {
                throw new BusinessException(ErrorCode.OUTBOUND_STATUS_ERROR, "拣货数量超过计划数量");
            }

            // 调用库存服务扣减库存
            inventoryService.deductInventory(
                com.omnistock.backend.domain.dto.DeductInventoryDTO.builder()
                    .skuId(outboundItem.getSkuId())
                    .quantity(pickItem.getPickedQuantity())
                    .referenceNo(outbound.getOutboundNo())
                    .operatorId(dto.getOperatorId())
                    .remark("出库拣货")
                    .build()
            );

            // 更新出库明细
            outboundItem.setPickedQuantity(pickItem.getPickedQuantity());
            outboundItem.setStatus("PICKED");
            outboundItemMapper.updateById(outboundItem);
        }

        // 第三步：更新出库单状态
        outbound.setStatus(BusinessConstant.OUTBOUND_COMPLETED);
        outbound.setOutboundTime(LocalDateTime.now());
        outbound.setUpdatedAt(LocalDateTime.now());
        outboundMapper.updateById(outbound);

        log.info("出库单拣货完成: outboundNo={}", outbound.getOutboundNo());
        return getOutboundDetail(outboundId);
    }

    @Override
    @Transactional
    public void cancelOutbound(Long outboundId, Long operatorId, String remark) {
        Outbound outbound = outboundMapper.selectById(outboundId);
        if (outbound == null) {
            throw new BusinessException(ErrorCode.OUTBOUND_NOT_FOUND, "出库单不存在");
        }

        if (!BusinessConstant.OUTBOUND_PENDING.equals(outbound.getStatus())) {
            throw new BusinessException(ErrorCode.OUTBOUND_STATUS_ERROR, "只有待出库状态才能取消");
        }

        outbound.setStatus(BusinessConstant.OUTBOUND_CANCELLED);
        outbound.setUpdatedAt(LocalDateTime.now());
        outboundMapper.updateById(outbound);

        log.info("出库单已取消: outboundNo={}", outbound.getOutboundNo());
    }

    /**
     * 转换为VO
     */
    private OutboundVO convertToVO(Outbound outbound) {
        LambdaQueryWrapper<OutboundItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutboundItem::getOutboundId, outbound.getOutboundId());
        List<OutboundItem> items = outboundItemMapper.selectList(wrapper);
        return convertToVO(outbound, items);
    }

    /**
     * 转换为VO（带明细）
     */
    private OutboundVO convertToVO(Outbound outbound, List<OutboundItem> items) {
        return OutboundVO.builder()
                .outboundId(outbound.getOutboundId())
                .outboundNo(outbound.getOutboundNo())
                .warehouseId(outbound.getWarehouseId())
                .status(outbound.getStatus())
                .totalQuantity(outbound.getTotalQuantity())
                .operatorId(outbound.getOperatorId())
                .remark(outbound.getRemark())
                .outboundTime(outbound.getOutboundTime())
                .createdAt(outbound.getCreatedAt())
                .updatedAt(outbound.getUpdatedAt())
                .items(items.stream().map(item -> OutboundVO.OutboundItemVO.builder()
                        .itemId(item.getItemId())
                        .skuId(item.getSkuId())
                        .quantity(item.getQuantity())
                        .pickedQuantity(item.getPickedQuantity())
                        .status(item.getStatus())
                        .build()).toList())
                .build();
    }
}
