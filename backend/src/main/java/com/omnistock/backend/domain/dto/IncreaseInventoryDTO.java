package com.omnistock.backend.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存增加请求DTO
 * 对应接口文档: 03_INVENTORY_API.md - 3.5 库存增加
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncreaseInventoryDTO {
    @NotNull(message = "商品ID不能为空")
    private Long skuId;

    @NotNull(message = "库位ID不能为空")
    private Long locationId;

    @NotNull(message = "增加数量不能为空")
    @Min(value = 1, message = "增加数量必须大于0")
    private Integer quantity;

    @NotNull(message = "关联单号不能为空")
    private String referenceNo;

    @NotNull(message = "操作人ID不能为空")
    private Long operatorId;

    private String remark;
}
