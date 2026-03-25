package com.omnistock.backend.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class InboundCreateRequest {

    private String bizId;

    @NotNull(message = "操作人不能为空")
    private Long operatorId;

    @Valid
    @Size(min = 1, message = "入库明细不能为空")
    private List<StockItem> items;
}
