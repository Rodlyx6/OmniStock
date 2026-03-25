package com.omnistock.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationUpsertRequest {

    private Long id;

    @NotNull(message = "warehouseId不能为空")
    private Long warehouseId;

    @NotBlank(message = "库位编码不能为空")
    private String code;

    @NotNull(message = "最大容量不能为空")
    private Integer maxCapacity;

    @NotNull(message = "最大承重不能为空")
    private Double maxWeight;
}
