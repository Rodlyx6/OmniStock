package com.omnistock.backend.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 出库单VO
 * 对应接口文档: 05_OUTBOUND_API.md - 5.1 创建出库单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboundVO {
    private Long outboundId;
    private String outboundNo;
    private Long warehouseId;
    private String warehouseName;
    private String status;
    private Integer totalQuantity;
    private Long operatorId;
    private String operatorName;
    private String remark;
    private LocalDateTime outboundTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OutboundItemVO> items;

    /**
     * 出库明细VO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutboundItemVO {
        private Long itemId;
        private Long skuId;
        private String skuCode;
        private String skuName;
        private Integer quantity;
        private Integer pickedQuantity;
        private String status;
    }
}
