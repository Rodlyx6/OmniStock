package com.omnistock.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.omnistock.backend.domain.entity.OutboundRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OutboundRecordMapper extends BaseMapper<OutboundRecord> {
}
