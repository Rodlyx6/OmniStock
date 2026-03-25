package com.omnistock.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inventory")
public class Inventory {

    @TableId(type =IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "sku_id")
    private Long skuId;

    @TableField(value = "location_id")
    private Long locationId;

    @TableField(value = "quantity")
    private Integer quantity;

    @TableField(value = "version")
    private Integer version;

    @TableField(value = "created_time" , fill =FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(value = "updated_time" , fill = FieldFill.UPDATE)
    private LocalDateTime updatedTime;
}
