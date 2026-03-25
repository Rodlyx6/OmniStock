package com.omnistock.backend.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryVO {

    private Long id;

    private Long skuId;

    private String skuName;

    private String skuCode;

    private Long locationId;

    private String locationCode;

    private Integer quantity;

    private LocalDateTime updatedTime;
}
