package com.omnistock.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建入库单DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInboundDTO {
    @NotNull(message = "供应商ID不能为空")
    private Long supplierId;

    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @NotNull(message = "操作人ID不能为空")
    private Long operatorId;

    private String remark;

    @NotEmpty(message = "入库明细不能为空")
    private List<InboundItemDTO> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InboundItemDTO {
        @NotNull(message = "商品ID不能为空")
        private Long skuId;

        @NotNull(message = "入库数量不能为空")
        private Integer quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiveDTO {
        @NotEmpty(message = "收货明细不能为空")
        private List<ReceiveItemDTO> items;

        @NotNull(message = "操作人ID不能为空")
        private Long operatorId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiveItemDTO {
        @NotNull(message = "明细ID不能为空")
        private Long itemId;

        @NotNull(message = "收货数量不能为空")
        private Integer receivedQuantity;

        @NotNull(message = "库位ID不能为空")
        private Long locationId;
    }
}
