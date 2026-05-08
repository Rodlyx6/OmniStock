package com.omnistock.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.omnistock.backend.domain.entity.User;
import com.omnistock.backend.domain.vo.UserVO;

import java.util.List;

public interface UserService extends IService<User> {
    List<UserVO> listUsers();
    UserVO getUserById(Long id);
    void updateStatus(Long id, Integer status);
}
