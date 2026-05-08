package com.omnistock.backend.controller;

import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.vo.SystemStatsVO;
import com.omnistock.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/stats")
    public Result<SystemStatsVO> getStats() {
        return Result.success(adminService.getSystemStats());
    }
}
