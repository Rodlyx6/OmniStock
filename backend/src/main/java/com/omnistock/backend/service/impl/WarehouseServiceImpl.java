package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.omnistock.backend.domain.entity.Warehouse;
import com.omnistock.backend.domain.entity.Location;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.constant.ErrorCode;
import com.omnistock.backend.mapper.WarehouseMapper;
import com.omnistock.backend.mapper.LocationMapper;
import com.omnistock.backend.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 仓库Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final LocationMapper locationMapper;

    @Override
    public IPage<Warehouse> queryWarehouse(int page, int pageSize) {
        Page<Warehouse> pageObj = new Page<>(page, pageSize);
        return warehouseMapper.selectPage(pageObj, null);
    }

    @Override
    public Warehouse getWarehouseDetail(Long warehouseId) {
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        if (warehouse == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "仓库不存在");
        }
        return warehouse;
    }

    @Override
    public Warehouse createWarehouse(Warehouse warehouse) {
        warehouse.setStatus(1);
        warehouse.setCreatedAt(LocalDateTime.now());
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouseMapper.insert(warehouse);
        return warehouse;
    }

    @Override
    public void updateWarehouse(Long warehouseId, Warehouse warehouse) {
        Warehouse existing = warehouseMapper.selectById(warehouseId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "仓库不存在");
        }
        warehouse.setWarehouseId(warehouseId);
        warehouse.setUpdatedAt(LocalDateTime.now());
        warehouseMapper.updateById(warehouse);
    }

    @Override
    public IPage<Location> queryLocation(int page, int pageSize, Long warehouseId) {
        Page<Location> pageObj = new Page<>(page, pageSize);
        LambdaQueryWrapper<Location> wrapper = new LambdaQueryWrapper<>();
        if (warehouseId != null) {
            wrapper.eq(Location::getWarehouseId, warehouseId);
        }
        return locationMapper.selectPage(pageObj, wrapper);
    }

    @Override
    public Location getLocationDetail(Long locationId) {
        Location location = locationMapper.selectById(locationId);
        if (location == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "库位不存在");
        }
        return location;
    }

    @Override
    public Location createLocation(Long warehouseId, Location location) {
        location.setWarehouseId(warehouseId);
        location.setStatus(1);
        location.setCurrentQuantity(0);
        location.setCreatedAt(LocalDateTime.now());
        location.setUpdatedAt(LocalDateTime.now());
        locationMapper.insert(location);
        return location;
    }

    @Override
    public void updateLocation(Long locationId, Location location) {
        Location existing = locationMapper.selectById(locationId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "库位不存在");
        }
        location.setLocationId(locationId);
        location.setUpdatedAt(LocalDateTime.now());
        locationMapper.updateById(location);
    }
}
