package com.omnistock.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.domain.dto.CreateInboundDTO;
import com.omnistock.backend.domain.entity.Inbound;

/**
 * 入库Service接口
 */
public interface InboundService {
    IPage<Inbound> queryInbound(int page, int pageSize, String status, Long warehouseId);
    Inbound getInboundDetail(Long inboundId);
    Inbound createInbound(CreateInboundDTO dto);
    void receiveInbound(Long inboundId, CreateInboundDTO.ReceiveDTO dto);
    void cancelInbound(Long inboundId, Long operatorId, String remark);
}
