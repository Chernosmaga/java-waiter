package com.waiter.javawaiter.employee.service;

import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.EmployeeForAdminDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;

import java.util.List;

public interface EmployeeService {
    EmployeeShortDto createAdmin(EmployeeDto employee);
    EmployeeShortDto create(Long adminId, EmployeeDto employee);
    EmployeeShortDto update(Long adminId, Long employeeId, EmployeeDto employee);
    EmployeeDto get(String phone);
    EmployeeForAdminDto getByAdmin(Long adminId, Long employeeId);
    void updateIsActive(Long adminId, Long employeeId, Boolean isActive);
    List<EmployeeShortDto> getEmployees(Long adminId);
}
