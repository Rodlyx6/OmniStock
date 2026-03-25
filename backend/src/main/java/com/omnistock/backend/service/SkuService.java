package com.omnistock.backend.service;

import com.omnistock.backend.domain.dto.SkuUpsertRequest;
import com.omnistock.backend.domain.vo.SkuVO;

import java.util.List;

public interface SkuService {

    Long upsert(SkuUpsertRequest request);

    List<SkuVO> listAll();
}
