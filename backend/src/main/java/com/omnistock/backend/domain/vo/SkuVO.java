package com.omnistock.backend.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkuVO {

    private Long id;

    private String code;

    private String name;

    private String category;

    private String unit;

    private Double weight;

    private Double volume;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}
