package com.omnistock.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("warehouse")
public class Warehouse {
    @TableId(type = IdType.ASSIGN_ID)
    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private String address;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
