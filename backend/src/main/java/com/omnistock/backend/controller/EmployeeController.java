package com.omnistock.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.omnistock.backend.common.Result;
import com.omnistock.backend.domain.entity.Employee;
import com.omnistock.backend.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 员工Controller
 */
@RestController
@RequestMapping("/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public Result<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return Result.created(employeeService.createEmployee(employee));
    }

    @GetMapping
    public Result<IPage<Employee>> queryEmployee(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(employeeService.queryEmployee(page, pageSize));
    }

    @GetMapping("/{employeeId}")
    public Result<Employee> getEmployeeDetail(@PathVariable Long employeeId) {
        return Result.success(employeeService.getEmployeeDetail(employeeId));
    }

    @PutMapping("/{employeeId}")
    public Result<Employee> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody Employee employee) {
        employeeService.updateEmployee(employeeId, employee);
        return Result.success(employeeService.getEmployeeDetail(employeeId));
    }

    @DeleteMapping("/{employeeId}")
    public Result<?> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return Result.success();
    }
}
