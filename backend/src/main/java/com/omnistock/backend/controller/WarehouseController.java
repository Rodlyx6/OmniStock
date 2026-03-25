package com.omnistock.backend.controller;

import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.dto.WarehouseUpsertRequest;
import com.omnistock.backend.domain.vo.WarehouseVO;
import com.omnistock.backend.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    public Result<Long> upsert(@Valid @RequestBody WarehouseUpsertRequest request) {
        return Result.success(warehouseService.upsert(request));
    }

    @GetMapping
    public Result<List<WarehouseVO>> listAll() {
        return Result.success(warehouseService.listAll());
    }
}
