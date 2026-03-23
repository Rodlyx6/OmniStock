package com.omnistock.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.dto.CreateOutboundDTO;
import com.omnistock.backend.domain.dto.PickOutboundDTO;
import com.omnistock.backend.domain.vo.OutboundVO;
import com.omnistock.backend.service.OutboundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 出库Controller
 * 
 * 对应接口文档: 05_OUTBOUND_API.md
 */
@RestController
@RequestMapping("/v1/outbounds")
@RequiredArgsConstructor
public class OutboundController {

    private final OutboundService outboundService;

    /**
     * 创建出库单
     * POST /v1/outbounds
     */
    @PostMapping
    public Result<OutboundVO> createOutbound(@Valid @RequestBody CreateOutboundDTO dto) {
        OutboundVO result = outboundService.createOutbound(dto);
        return Result.created(result);
    }

    /**
     * 查询出库单列表
     * GET /v1/outbounds?page=1&pageSize=10&status=PENDING
     */
    @GetMapping
    public Result<IPage<OutboundVO>> queryOutbounds(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long warehouseId) {
        IPage<OutboundVO> result = outboundService.queryOutbounds(page, pageSize, status, warehouseId);
        return Result.success(result);
    }

    /**
     * 查询出库单详情
     * GET /v1/outbounds/{outboundId}
     */
    @GetMapping("/{outboundId}")
    public Result<OutboundVO> getOutboundDetail(@PathVariable Long outboundId) {
        OutboundVO result = outboundService.getOutboundDetail(outboundId);
        return Result.success(result);
    }

    /**
     * 确认出库（拣货完成）
     * POST /v1/outbounds/{outboundId}/pick
     */
    @PostMapping("/{outboundId}/pick")
    public Result<OutboundVO> pickOutbound(
            @PathVariable Long outboundId,
            @Valid @RequestBody PickOutboundDTO dto) {
        OutboundVO result = outboundService.pickOutbound(outboundId, dto);
        return Result.success(result);
    }

    /**
     * 取消出库单
     * DELETE /v1/outbounds/{outboundId}
     */
    @DeleteMapping("/{outboundId}")
    public Result<?> cancelOutbound(
            @PathVariable Long outboundId,
            @RequestParam Long operatorId,
            @RequestParam(required = false) String remark) {
        outboundService.cancelOutbound(outboundId, operatorId, remark);
        return Result.success();
    }
}
