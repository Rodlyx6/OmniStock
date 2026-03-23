package com.omnistock.backend.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 库存VO
 * 对应接口文档: 03_INVENTORY_API.md - 3.1 查询库存列表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryVO {
    private Long inventoryId;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private Long locationId;
    private String locationCode;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
