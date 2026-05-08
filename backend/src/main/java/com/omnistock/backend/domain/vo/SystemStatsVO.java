package com.omnistock.backend.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemStatsVO {
    private Long userCount;
    private Long warehouseCount;
    private Long skuCount;
    private Long totalInventory;
    private Long todayInbound;
    private Long todayOutbound;
}
