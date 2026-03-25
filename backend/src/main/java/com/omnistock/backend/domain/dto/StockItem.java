package com.omnistock.backend.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockItem {

    @NotNull(message = "skuId不能为空")
    private Long skuId;

    @NotNull(message = "locationId不能为空")
    private Long locationId;

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;
}
