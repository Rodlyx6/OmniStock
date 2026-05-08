package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.omnistock.backend.common.Result;
import com.omnistock.backend.common.security.JwtUtils;
import com.omnistock.backend.domain.dto.AuthResponse;
import com.omnistock.backend.domain.dto.ForgotPasswordRequest;
import com.omnistock.backend.domain.dto.LoginRequest;
import com.omnistock.backend.domain.dto.RegisterRequest;
import com.omnistock.backend.domain.entity.Role;
import com.omnistock.backend.domain.entity.User;
import com.omnistock.backend.domain.entity.UserRole;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.exception.ErrorCode;
import com.omnistock.backend.mapper.RoleMapper;
import com.omnistock.backend.mapper.UserMapper;
import com.omnistock.backend.mapper.UserRoleMapper;
import com.omnistock.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        org.springframework.security.core.userdetails.User userDetails = 
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userDetails.getUsername()));
        
        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "您的账号已被禁用，请联系管理员");
        }
        
        String token = jwtUtils.generateToken(user.getUsername());
        
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername()));
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setStatus(1); // 默认启用
        userMapper.insert(user);

        // 分配默认角色 (普通用户)
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getCode, "ROLE_USER"));
        if (role != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())
                .eq(User::getEmail, request.getEmail()));
        
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名或邮箱错误");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userMapper.updateById(user);
    }
}
