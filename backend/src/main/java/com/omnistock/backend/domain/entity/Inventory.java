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
 * 库存表
 * 
 * 核心字段：
 * - version: 乐观锁，防超卖
 * - available_quantity: 可用库存 = quantity - reserved_quantity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("inventory")
public class Inventory {
    private Long inventoryId;
    private Long skuId;
    private Long locationId;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
