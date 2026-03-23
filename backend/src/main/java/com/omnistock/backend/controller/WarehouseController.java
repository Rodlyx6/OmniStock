package com.omnistock.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.entity.Warehouse;
import com.omnistock.backend.domain.entity.Location;
import com.omnistock.backend.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 仓库Controller
 */
@RestController
@RequestMapping("/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    public Result<Warehouse> createWarehouse(@Valid @RequestBody Warehouse warehouse) {
        Warehouse result = warehouseService.createWarehouse(warehouse);
        return Result.created(result);
    }

    @GetMapping
    public Result<IPage<Warehouse>> queryWarehouse(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        IPage<Warehouse> result = warehouseService.queryWarehouse(page, pageSize);
        return Result.success(result);
    }

    @GetMapping("/{warehouseId}")
    public Result<Warehouse> getWarehouseDetail(@PathVariable Long warehouseId) {
        Warehouse result = warehouseService.getWarehouseDetail(warehouseId);
        return Result.success(result);
    }

    @PutMapping("/{warehouseId}")
    public Result<Warehouse> updateWarehouse(@PathVariable Long warehouseId, @Valid @RequestBody Warehouse warehouse) {
        warehouseService.updateWarehouse(warehouseId, warehouse);
        return Result.success(warehouseService.getWarehouseDetail(warehouseId));
    }

    @PostMapping("/{warehouseId}/locations")
    public Result<Location> createLocation(@PathVariable Long warehouseId, @Valid @RequestBody Location location) {
        Location result = warehouseService.createLocation(warehouseId, location);
        return Result.created(result);
    }

    @GetMapping("/{warehouseId}/locations")
    public Result<IPage<Location>> queryLocation(
            @PathVariable Long warehouseId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        IPage<Location> result = warehouseService.queryLocation(page, pageSize, warehouseId);
        return Result.success(result);
    }

    @GetMapping("/{warehouseId}/locations/{locationId}")
    public Result<Location> getLocationDetail(
            @PathVariable Long warehouseId,
            @PathVariable Long locationId) {
        Location result = warehouseService.getLocationDetail(locationId);
        return Result.success(result);
    }

    @PutMapping("/{warehouseId}/locations/{locationId}")
    public Result<Location> updateLocation(
            @PathVariable Long warehouseId,
            @PathVariable Long locationId,
            @Valid @RequestBody Location location) {
        warehouseService.updateLocation(locationId, location);
        return Result.success(warehouseService.getLocationDetail(locationId));
    }
}
