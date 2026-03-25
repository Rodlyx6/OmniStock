package com.omnistock.backend.service;

import com.omnistock.backend.domain.dto.LocationUpsertRequest;
import com.omnistock.backend.domain.vo.LocationVO;

import java.util.List;

public interface LocationService {

    Long upsert(LocationUpsertRequest request);

    List<LocationVO> listAll();

    List<LocationVO> listByWarehouse(Long warehouseId);
}
