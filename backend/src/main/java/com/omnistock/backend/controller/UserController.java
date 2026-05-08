package com.omnistock.backend.controller;

import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.entity.User;
import com.omnistock.backend.domain.vo.UserVO;
import com.omnistock.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<UserVO>> listUsers() {
        return Result.success(userService.listUsers());
    }

    @GetMapping("/profile")
    public Result<UserVO> getProfile(java.security.Principal principal) {
        User user = userService.getOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, principal.getName()));
        return Result.success(userService.getUserById(user.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<UserVO> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success(null);
    }
}
