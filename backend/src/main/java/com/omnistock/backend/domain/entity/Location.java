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
@TableName("location")
public class Location {
    @TableId(type = IdType.ASSIGN_ID)
    private Long locationId;
    private Long warehouseId;
    private String locationCode;
    private String area;
    private String shelf;
    private Integer layer;
    private Integer position;
    private Integer capacity;
    private Integer currentQuantity;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
