package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.omnistock.backend.domain.dto.WarehouseUpsertRequest;
import com.omnistock.backend.domain.entity.Warehouse;
import com.omnistock.backend.domain.vo.WarehouseVO;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.exception.ErrorCode;
import com.omnistock.backend.mapper.WarehouseMapper;
import com.omnistock.backend.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseMapper warehouseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long upsert(WarehouseUpsertRequest request) {
        if (request.getId() == null) {
            Warehouse warehouse = new Warehouse();
            warehouse.setName(request.getName());
            warehouse.setAddress(request.getAddress());
            warehouse.setCapacity(request.getCapacity());
            warehouse.setCurrentUsage(0);
            warehouseMapper.insert(warehouse);
            return warehouse.getId();
        }

        Warehouse exist = warehouseMapper.selectById(request.getId());
        if (exist == null) {
            throw new BusinessException(ErrorCode.WAREHOUSE_NOT_FOUND, "仓库不存在");
        }
        exist.setName(request.getName());
        exist.setAddress(request.getAddress());
        exist.setCapacity(request.getCapacity());
        warehouseMapper.updateById(exist);
        return exist.getId();
    }

    @Override
    public List<WarehouseVO> listAll() {
        return warehouseMapper.selectList(
                new LambdaQueryWrapper<Warehouse>().orderByDesc(Warehouse::getUpdatedTime))
                .stream().map(this::toVO).collect(Collectors.toList());
    }

    private WarehouseVO toVO(Warehouse w) {
        WarehouseVO vo = new WarehouseVO();
        vo.setId(w.getId());
        vo.setName(w.getName());
        vo.setAddress(w.getAddress());
        vo.setCapacity(w.getCapacity());
        vo.setCurrentUsage(w.getCurrentUsage());
        vo.setCreatedTime(w.getCreatedTime());
        vo.setUpdatedTime(w.getUpdatedTime());
        return vo;
    }
}
