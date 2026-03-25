package com.omnistock.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("warehouse")
public class Warehouse {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "address")
    private String address;
    @TableField(value = "capacity")
    private Integer capacity;
    @TableField(value = "current_usage")
    private Integer currentUsage;
    @TableField(value = "created_time" , fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    @TableField(value = "updated_time" , fill = FieldFill.UPDATE)
    private LocalDateTime updatedTime;
}
