package com.omnistock.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.domain.entity.Warehouse;
import com.omnistock.backend.domain.entity.Location;

/**
 * 仓库Service接口
 */
public interface WarehouseService {
    IPage<Warehouse> queryWarehouse(int page, int pageSize);
    Warehouse getWarehouseDetail(Long warehouseId);
    Warehouse createWarehouse(Warehouse warehouse);
    void updateWarehouse(Long warehouseId, Warehouse warehouse);
    
    IPage<Location> queryLocation(int page, int pageSize, Long warehouseId);
    Location getLocationDetail(Long locationId);
    Location createLocation(Long warehouseId, Location location);
    void updateLocation(Long locationId, Location location);
}
