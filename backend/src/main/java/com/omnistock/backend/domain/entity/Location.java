package com.omnistock.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("location")
public class Location {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(value = "warehouse_id")
    private Long warehouseId;
    @TableField(value = "code")
    private String code;
    @TableField(value = "max_capacity")
    private Integer maxCapacity;
    @TableField(value = "max_weight")
    private Double maxWeight;
    @TableField(value = "current_capacity")
    private Integer currentCapacity;
    @TableField(value = "current_weight")
    private Double currentWeight;
    @TableField(value = "created_time" , fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    @TableField(value = "updated_time" , fill = FieldFill.UPDATE)
    private LocalDateTime updatedTime;
}
