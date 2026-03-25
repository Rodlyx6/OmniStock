package com.omnistock.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.omnistock.backend.domain.entity.InboundRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InboundRecordMapper extends BaseMapper<InboundRecord> {
}
