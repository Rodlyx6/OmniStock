package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.omnistock.backend.domain.entity.Role;
import com.omnistock.backend.domain.entity.User;
import com.omnistock.backend.domain.vo.UserVO;
import com.omnistock.backend.mapper.RoleMapper;
import com.omnistock.backend.mapper.UserMapper;
import com.omnistock.backend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<UserVO> listUsers() {
        return this.list().stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = this.getById(id);
        return user != null ? convertToVO(user) : null;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        this.updateById(user);
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        vo.setRoles(roles.stream().map(Role::getCode).collect(Collectors.toList()));
        return vo;
    }
}
