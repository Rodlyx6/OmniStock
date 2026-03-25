package com.omnistock.backend.controller;

import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.dto.LocationUpsertRequest;
import com.omnistock.backend.domain.vo.LocationVO;
import com.omnistock.backend.service.LocationService;
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
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public Result<Long> upsert(@Valid @RequestBody LocationUpsertRequest request) {
        return Result.success(locationService.upsert(request));
    }

    @GetMapping
    public Result<List<LocationVO>> listAll() {
        return Result.success(locationService.listAll());
    }

    @GetMapping("/warehouse")
    public Result<List<LocationVO>> listByWarehouse(@RequestParam Long warehouseId) {
        return Result.success(locationService.listByWarehouse(warehouseId));
    }
}
