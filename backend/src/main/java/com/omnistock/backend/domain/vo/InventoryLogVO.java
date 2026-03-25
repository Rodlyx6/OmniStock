package com.omnistock.backend.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InventoryLogVO {

    private Long id;

    private Long skuId;

    private String skuName;

    private Long locationId;

    private String locationCode;

    private Integer changeQty;

    private String changeType;

    private String bizId;

    private Long operatorId;

    private LocalDateTime createdTime;
}
