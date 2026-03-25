package com.omnistock.backend.service;

import com.omnistock.backend.domain.dto.InboundCreateRequest;
import com.omnistock.backend.domain.dto.OutboundCreateRequest;
import com.omnistock.backend.domain.vo.InventoryLogVO;
import com.omnistock.backend.domain.vo.InventoryVO;

import java.util.List;

public interface InventoryService {

    String inbound(InboundCreateRequest request);

    String outbound(OutboundCreateRequest request);

    List<InventoryVO> list(Long skuId, Long locationId);

    List<InventoryLogVO> listLogs(Long skuId, String changeType);
}
