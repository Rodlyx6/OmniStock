package com.omnistock.backend.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarehouseVO {

    private Long id;

    private String name;

    private String address;

    private Integer capacity;

    private Integer currentUsage;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}
