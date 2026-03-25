package com.omnistock.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SkuUpsertRequest {

    private Long id;

    @NotBlank(message = "sku编码不能为空")
    private String code;

    @NotBlank(message = "sku名称不能为空")
    private String name;

    private String category;

    private String unit;

    @NotNull(message = "重量不能为空")
    private Double weight;

    @NotNull(message = "体积不能为空")
    private Double volume;
}
