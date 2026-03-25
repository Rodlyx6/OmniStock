package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.omnistock.backend.domain.dto.LocationUpsertRequest;
import com.omnistock.backend.domain.entity.Location;
import com.omnistock.backend.domain.vo.LocationVO;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.exception.ErrorCode;
import com.omnistock.backend.mapper.LocationMapper;
import com.omnistock.backend.mapper.WarehouseMapper;
import com.omnistock.backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationMapper locationMapper;

    private final WarehouseMapper warehouseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long upsert(LocationUpsertRequest request) {
        if (warehouseMapper.selectById(request.getWarehouseId()) == null) {
            throw new BusinessException(ErrorCode.WAREHOUSE_NOT_FOUND, "仓库不存在");
        }
        if (request.getId() == null) {
            Location location = new Location();
            location.setWarehouseId(request.getWarehouseId());
            location.setCode(request.getCode());
            location.setMaxCapacity(request.getMaxCapacity());
            location.setMaxWeight(request.getMaxWeight());
            location.setCurrentCapacity(0);
            location.setCurrentWeight(0D);
            locationMapper.insert(location);
            return location.getId();
        }

        Location exist = locationMapper.selectById(request.getId());
        if (exist == null) {
            throw new BusinessException(ErrorCode.LOCATION_NOT_FOUND, "库位不存在");
        }
        exist.setWarehouseId(request.getWarehouseId());
        exist.setCode(request.getCode());
        exist.setMaxCapacity(request.getMaxCapacity());
        exist.setMaxWeight(request.getMaxWeight());
        locationMapper.updateById(exist);
        return exist.getId();
    }

    @Override
    public List<LocationVO> listAll() {
        return locationMapper.selectList(
                new LambdaQueryWrapper<Location>().orderByDesc(Location::getUpdatedTime))
                .stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public List<LocationVO> listByWarehouse(Long warehouseId) {
        return locationMapper.selectList(
                new LambdaQueryWrapper<Location>()
                        .eq(Location::getWarehouseId, warehouseId)
                        .orderByAsc(Location::getCode))
                .stream().map(this::toVO).collect(Collectors.toList());
    }

    private LocationVO toVO(Location loc) {
        LocationVO vo = new LocationVO();
        vo.setId(loc.getId());
        vo.setWarehouseId(loc.getWarehouseId());
        vo.setCode(loc.getCode());
        vo.setMaxCapacity(loc.getMaxCapacity());
        vo.setMaxWeight(loc.getMaxWeight());
        vo.setCurrentCapacity(loc.getCurrentCapacity());
        vo.setCurrentWeight(loc.getCurrentWeight());
        vo.setCreatedTime(loc.getCreatedTime());
        vo.setUpdatedTime(loc.getUpdatedTime());
        return vo;
    }
}
