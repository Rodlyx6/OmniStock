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
@TableName("outbound_item")
public class OutboundItem {
    @TableId(type = IdType.ASSIGN_ID)
    private Long itemId;
    private Long outboundId;
    private Long skuId;
    private Integer quantity;
    private Integer pickedQuantity;
    private String status;
    private LocalDateTime createdAt;
}
