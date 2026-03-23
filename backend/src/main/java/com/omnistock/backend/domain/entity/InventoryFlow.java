package com.omnistock.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 库存流水表
 * 
 * 记录所有库存变化，支持完整追溯
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("inventory_flow")
public class InventoryFlow {
    private Long flowId;
    private Long skuId;
    private Long inventoryId;
    private String flowType;
    private Integer quantityChange;
    private Integer beforeQuantity;
    private Integer afterQuantity;
    private String referenceNo;
    private Long operatorId;
    private String remark;
    private LocalDateTime createdAt;
}
