package com.omnistock.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WarehouseUpsertRequest {

    private Long id;

    @NotBlank(message = "仓库名称不能为空")
    private String name;

    @NotBlank(message = "仓库地址不能为空")
    private String address;

    @NotNull(message = "仓库容量不能为空")
    private Integer capacity;
}
