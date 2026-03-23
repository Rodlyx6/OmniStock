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
 * 出库单表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("outbound")
public class Outbound {
    private Long outboundId;
    private String outboundNo;
    private Long warehouseId;
    private String status;
    private Integer totalQuantity;
    private Long operatorId;
    private LocalDateTime outboundTime;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
