package com.omnistock.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.domain.vo.InventoryFlowVO;

/**
 * 库存流水Service接口
 * 对应接口文档: 03_INVENTORY_API.md - 3.6 查询库存流水
 */
public interface InventoryFlowService {

    /**
     * 查询库存流水列表
     */
    IPage<InventoryFlowVO> queryFlows(int page, int pageSize, Long skuId, Long inventoryId, 
                                      String flowType, String referenceNo, Long operatorId);

    /**
     * 查询库存流水详情
     */
    InventoryFlowVO getFlowDetail(Long flowId);
}
