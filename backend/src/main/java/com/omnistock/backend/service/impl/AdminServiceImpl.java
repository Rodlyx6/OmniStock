package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.omnistock.backend.domain.entity.*;
import com.omnistock.backend.domain.vo.SystemStatsVO;
import com.omnistock.backend.mapper.*;
import com.omnistock.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InboundRecordMapper inboundRecordMapper;
    @Autowired
    private OutboundRecordMapper outboundRecordMapper;

    @Override
    public SystemStatsVO getSystemStats() {
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);

        return SystemStatsVO.builder()
                .userCount(userMapper.selectCount(null))
                .warehouseCount(warehouseMapper.selectCount(null))
                .skuCount(skuMapper.selectCount(null))
                .totalInventory(getTotalInventory())
                .todayInbound(getTodayInbound(todayStart))
                .todayOutbound(getTodayOutbound(todayStart))
                .build();
    }

    private Long getTotalInventory() {
        return inventoryMapper.selectList(null).stream()
                .mapToLong(i -> i.getQuantity().longValue())
                .sum();
    }

    private Long getTodayInbound(LocalDateTime start) {
        return inboundRecordMapper.selectList(new LambdaQueryWrapper<InboundRecord>()
                .gt(InboundRecord::getCreatedTime, start)).stream()
                .mapToLong(r -> r.getQuantity().longValue())
                .sum();
    }

    private Long getTodayOutbound(LocalDateTime start) {
        return outboundRecordMapper.selectList(new LambdaQueryWrapper<OutboundRecord>()
                .gt(OutboundRecord::getCreatedTime, start)).stream()
                .mapToLong(r -> r.getQuantity().longValue())
                .sum();
    }
}
