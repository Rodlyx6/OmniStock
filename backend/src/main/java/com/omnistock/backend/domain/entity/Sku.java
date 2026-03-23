package com.omnistock.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sku")
public class Sku {
    @TableId(type = IdType.ASSIGN_ID)
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String category;
    private BigDecimal weight;
    private BigDecimal volume;
    private Integer abcCategory;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
