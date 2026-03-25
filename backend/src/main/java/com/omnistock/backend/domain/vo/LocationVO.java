package com.omnistock.backend.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationVO {

    private Long id;

    private Long warehouseId;

    private String code;

    private Integer maxCapacity;

    private Double maxWeight;

    private Integer currentCapacity;

    private Double currentWeight;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}
