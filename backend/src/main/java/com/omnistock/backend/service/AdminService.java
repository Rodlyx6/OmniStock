package com.omnistock.backend.service;

import com.omnistock.backend.domain.vo.SystemStatsVO;

public interface AdminService {
    SystemStatsVO getSystemStats();
}
