package com.omnistock.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.omnistock.backend.domain.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
}
