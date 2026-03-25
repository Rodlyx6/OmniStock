package com.omnistock.backend.service;

import com.omnistock.backend.domain.dto.WarehouseUpsertRequest;
import com.omnistock.backend.domain.vo.WarehouseVO;

import java.util.List;

public interface WarehouseService {

    Long upsert(WarehouseUpsertRequest request);

    List<WarehouseVO> listAll();
}
