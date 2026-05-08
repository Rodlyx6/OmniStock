package com.omnistock.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.omnistock.backend.domain.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
