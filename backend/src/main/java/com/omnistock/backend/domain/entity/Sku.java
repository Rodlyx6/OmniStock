package com.omnistock.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sku")
public class Sku {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(value = "code")
    private String code;
    @TableField(value = "name")
    private String name;
    @TableField(value = "category")
    private String category;
    @TableField(value = "unit")
    private String unit;
    @TableField(value = "weight")
    private Double weight;
    @TableField(value = "volume")
    private Double volume;
    @TableField(value = "created_time" , fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    @TableField(value = "updated_time" , fill = FieldFill.UPDATE)
    private LocalDateTime updatedTime;

}
