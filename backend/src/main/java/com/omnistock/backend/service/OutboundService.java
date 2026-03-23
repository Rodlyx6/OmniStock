package com.omnistock.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.domain.dto.CreateOutboundDTO;
import com.omnistock.backend.domain.dto.PickOutboundDTO;
import com.omnistock.backend.domain.vo.OutboundVO;

/**
 * 出库Service接口
 */
public interface OutboundService {

    /**
     * 创建出库单
     * 
     * 流程：
     * 1. 检查库存是否充足
     * 2. 创建出库单
     * 3. 创建出库明细
     * 4. 预留库存
     */
    OutboundVO createOutbound(CreateOutboundDTO dto);

    /**
     * 查询出库单列表
     */
    IPage<OutboundVO> queryOutbounds(int page, int pageSize, String status, Long warehouseId);

    /**
     * 查询出库单详情
     */
    OutboundVO getOutboundDetail(Long outboundId);

    /**
     * 确认出库（拣货完成）
     * 
     * 流程：
     * 1. 验证出库单状态
     * 2. 扣减库存
     * 3. 记录流水
     * 4. 更新出库单状态
     */
    OutboundVO pickOutbound(Long outboundId, PickOutboundDTO dto);

    /**
     * 取消出库单
     */
    void cancelOutbound(Long outboundId, Long operatorId, String remark);
}
