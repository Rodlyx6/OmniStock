package com.omnistock.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建出库单请求DTO
 * 对应接口文档: 05_OUTBOUND_API.md - 5.1 创建出库单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOutboundDTO {
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @NotNull(message = "操作人ID不能为空")
    private Long operatorId;

    private String remark;

    @NotEmpty(message = "出库明细不能为空")
    private List<OutboundItemDTO> items;

    /**
     * 出库明细DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutboundItemDTO {
        @NotNull(message = "商品ID不能为空")
        private Long skuId;

        @NotNull(message = "出库数量不能为空")
        private Integer quantity;
    }
}
