package com.omnistock.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.entity.Inbound;
import com.omnistock.backend.domain.dto.CreateInboundDTO;
import com.omnistock.backend.service.InboundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 入库Controller
 */
@RestController
@RequestMapping("/v1/inbounds")
@RequiredArgsConstructor
public class InboundController {

    private final InboundService inboundService;

    @PostMapping
    public Result<Inbound> createInbound(@Valid @RequestBody CreateInboundDTO dto) {
        Inbound result = inboundService.createInbound(dto);
        return Result.created(result);
    }

    @GetMapping
    public Result<IPage<Inbound>> queryInbound(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long warehouseId) {
        IPage<Inbound> result = inboundService.queryInbound(page, pageSize, status, warehouseId);
        return Result.success(result);
    }

    @GetMapping("/{inboundId}")
    public Result<Inbound> getInboundDetail(@PathVariable Long inboundId) {
        Inbound result = inboundService.getInboundDetail(inboundId);
        return Result.success(result);
    }

    @PostMapping("/{inboundId}/receive")
    public Result<Inbound> receiveInbound(
            @PathVariable Long inboundId,
            @Valid @RequestBody CreateInboundDTO.ReceiveDTO dto) {
        inboundService.receiveInbound(inboundId, dto);
        return Result.success(inboundService.getInboundDetail(inboundId));
    }

    @DeleteMapping("/{inboundId}")
    public Result<?> cancelInbound(
            @PathVariable Long inboundId,
            @RequestParam Long operatorId,
            @RequestParam(required = false) String remark) {
        inboundService.cancelInbound(inboundId, operatorId, remark);
        return Result.success();
    }
}
