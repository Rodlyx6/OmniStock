package com.omnistock.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.domain.entity.Employee;

/**
 * 员工Service接口
 */
public interface EmployeeService {
    IPage<Employee> queryEmployee(int page, int pageSize);
    Employee getEmployeeDetail(Long employeeId);
    Employee createEmployee(Employee employee);
    void updateEmployee(Long employeeId, Employee employee);
    void deleteEmployee(Long employeeId);
}
