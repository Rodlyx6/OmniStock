package com.omnistock.backend.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 库存操作结果VO
 * 对应接口文档: 03_INVENTORY_API.md - 3.4 库存扣减 / 3.5 库存增加
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryOperationVO {
    private Long inventoryId;
    private Long skuId;
    private Long flowId;
    private Integer beforeQuantity;
    private Integer afterQuantity;
    private Integer changeQuantity;  // 正数=增加，负数=扣减
    private Integer version;
    private LocalDateTime createdAt;
}
