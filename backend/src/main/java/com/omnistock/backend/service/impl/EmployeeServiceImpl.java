package com.omnistock.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.omnistock.backend.domain.entity.Employee;
import com.omnistock.backend.exception.BusinessException;
import com.omnistock.backend.constant.ErrorCode;
import com.omnistock.backend.mapper.EmployeeMapper;
import com.omnistock.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 员工Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    @Override
    public IPage<Employee> queryEmployee(int page, int pageSize) {
        Page<Employee> pageObj = new Page<>(page, pageSize);
        return employeeMapper.selectPage(pageObj, null);
    }

    @Override
    public Employee getEmployeeDetail(Long employeeId) {
        Employee employee = employeeMapper.selectById(employeeId);
        if (employee == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "员工不存在");
        }
        return employee;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        employee.setStatus(1);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employeeMapper.insert(employee);
        return employee;
    }

    @Override
    public void updateEmployee(Long employeeId, Employee employee) {
        if (employeeMapper.selectById(employeeId) == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "员工不存在");
        }
        employee.setEmployeeId(employeeId);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeMapper.updateById(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        if (employeeMapper.selectById(employeeId) == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "员工不存在");
        }
        employeeMapper.deleteById(employeeId);
    }
}
