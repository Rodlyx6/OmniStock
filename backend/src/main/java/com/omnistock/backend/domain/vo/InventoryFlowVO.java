package com.omnistock.backend.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 库存流水VO
 * 对应接口文档: 03_INVENTORY_API.md - 3.6 查询库存流水
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryFlowVO {
    private Long flowId;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private Long inventoryId;
    private String flowType;
    private Integer quantityChange;
    private Integer beforeQuantity;
    private Integer afterQuantity;
    private String referenceNo;
    private Long operatorId;
    private String operatorName;
    private String remark;
    private LocalDateTime createdAt;
}
