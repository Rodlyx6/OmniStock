package com.omnistock.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inventory_log")
public class InventoryLog {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(value = "sku_id")
    private Long skuId;
    @TableField(value = "location_id")
    private Long locationId;
    @TableField(value = "change_qty")
    private Integer changeQty;
    @TableField(value = "change_type")
    private String changeType;
    @TableField(value = "biz_id")
    private String bizId;
    @TableField(value = "operator_id")
    private Long operatorId;
    @TableField(value = "created_time" , fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    @TableField(value = "updated_time" , fill = FieldFill.UPDATE)
    private LocalDateTime updatedTime;
}
