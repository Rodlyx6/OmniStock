package com.omnistock.backend.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 确认出库（拣货）请求DTO
 * 对应接口文档: 05_OUTBOUND_API.md - 5.4 确认出库
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickOutboundDTO {
    @NotEmpty(message = "拣货明细不能为空")
    private List<PickItemDTO> items;

    @NotNull(message = "操作人ID不能为空")
    private Long operatorId;

    /**
     * 拣货明细DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PickItemDTO {
        @NotNull(message = "明细ID不能为空")
        private Long itemId;

        @NotNull(message = "拣货数量不能为空")
        private Integer pickedQuantity;
    }
}
