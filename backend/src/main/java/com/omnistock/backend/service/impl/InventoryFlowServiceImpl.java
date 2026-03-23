package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.omnistock.backend.domain.entity.InventoryFlow;
import com.omnistock.backend.domain.vo.InventoryFlowVO;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.constant.ErrorCode;
import com.omnistock.backend.mapper.InventoryFlowMapper;
import com.omnistock.backend.service.InventoryFlowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 库存流水Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryFlowServiceImpl implements InventoryFlowService {

    private final InventoryFlowMapper inventoryFlowMapper;

    @Override
    public IPage<InventoryFlowVO> queryFlows(int page, int pageSize, Long skuId, Long inventoryId,
                                             String flowType, String referenceNo, Long operatorId) {
        Page<InventoryFlow> pageObj = new Page<>(page, pageSize);
        LambdaQueryWrapper<InventoryFlow> wrapper = new LambdaQueryWrapper<>();

        if (skuId != null) {
            wrapper.eq(InventoryFlow::getSkuId, skuId);
        }
        if (inventoryId != null) {
            wrapper.eq(InventoryFlow::getInventoryId, inventoryId);
        }
        if (flowType != null) {
            wrapper.eq(InventoryFlow::getFlowType, flowType);
        }
        if (referenceNo != null) {
            wrapper.eq(InventoryFlow::getReferenceNo, referenceNo);
        }
        if (operatorId != null) {
            wrapper.eq(InventoryFlow::getOperatorId, operatorId);
        }

        wrapper.orderByDesc(InventoryFlow::getCreatedAt);
        IPage<InventoryFlow> result = inventoryFlowMapper.selectPage(pageObj, wrapper);

        return result.convert(this::convertToVO);
    }

    @Override
    public InventoryFlowVO getFlowDetail(Long flowId) {
        InventoryFlow flow = inventoryFlowMapper.selectById(flowId);
        if (flow == null) {
            throw new BusinessException(ErrorCode.INVENTORY_FLOW_NOT_FOUND, "库存流水不存在");
        }
        return convertToVO(flow);
    }

    /**
     * 转换为VO
     */
    private InventoryFlowVO convertToVO(InventoryFlow flow) {
        return InventoryFlowVO.builder()
                .flowId(flow.getFlowId())
                .skuId(flow.getSkuId())
                .inventoryId(flow.getInventoryId())
                .flowType(flow.getFlowType())
                .quantityChange(flow.getQuantityChange())
                .beforeQuantity(flow.getBeforeQuantity())
                .afterQuantity(flow.getAfterQuantity())
                .referenceNo(flow.getReferenceNo())
                .operatorId(flow.getOperatorId())
                .remark(flow.getRemark())
                .createdAt(flow.getCreatedAt())
                .build();
    }
}
