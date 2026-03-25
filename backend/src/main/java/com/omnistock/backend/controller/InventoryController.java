package com.omnistock.backend.controller;

import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.dto.InboundCreateRequest;
import com.omnistock.backend.domain.dto.OutboundCreateRequest;
import com.omnistock.backend.domain.vo.InventoryLogVO;
import com.omnistock.backend.domain.vo.InventoryVO;
import com.omnistock.backend.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/inbound")
    public Result<String> inbound(@Valid @RequestBody InboundCreateRequest request) {
        return Result.success(inventoryService.inbound(request));
    }

    @PostMapping("/outbound")
    public Result<String> outbound(@Valid @RequestBody OutboundCreateRequest request) {
        return Result.success(inventoryService.outbound(request));
    }

    @GetMapping
    public Result<List<InventoryVO>> list(
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) Long locationId) {
        return Result.success(inventoryService.list(skuId, locationId));
    }

    @GetMapping("/logs")
    public Result<List<InventoryLogVO>> listLogs(
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) String changeType) {
        return Result.success(inventoryService.listLogs(skuId, changeType));
    }
}
