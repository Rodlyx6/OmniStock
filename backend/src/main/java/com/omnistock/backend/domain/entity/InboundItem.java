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
@TableName("inbound_item")
public class InboundItem {
    @TableId(type = IdType.ASSIGN_ID)
    private Long itemId;
    private Long inboundId;
    private Long skuId;
    private Integer quantity;
    private Integer receivedQuantity;
    private String status;
    private LocalDateTime createdAt;
}
