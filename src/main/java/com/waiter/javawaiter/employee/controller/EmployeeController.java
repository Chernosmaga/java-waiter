package com.waiter.javawaiter.employee.controller;

import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.EmployeeForAdminDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.employee.service.EmployeeService;
import com.waiter.javawaiter.tip.model.Tip;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final String HEADER = "Employee-Id";

    @PostMapping
    public EmployeeShortDto createAdmin(@RequestBody EmployeeDto employee) {
        return employeeService.createAdmin(employee);
    }

    @PostMapping("/{adminId}")
    public EmployeeShortDto create(@PathVariable Long adminId, @RequestBody EmployeeDto employee) {
        return employeeService.create(adminId, employee);
    }

    @PutMapping("/{employeeId}")
    public EmployeeShortDto update(@RequestHeader(HEADER) Long adminId,
                                   @PathVariable Long employeeId,
                                   @RequestBody EmployeeDto employee) {
        return employeeService.update(adminId, employeeId, employee);
    }

    @GetMapping("/{phone}")
    public EmployeeDto get(@PathVariable String phone)  {
        return employeeService.get(phone);
    }

    @GetMapping("/admin/{employeeId}")
    public EmployeeForAdminDto getByAdmin(@RequestHeader(HEADER) Long adminId,
                                          @PathVariable Long employeeId) {
        return employeeService.getByAdmin(adminId, employeeId);
    }

    @PutMapping
    public void updateIsActive(@RequestHeader(HEADER) Long adminId,
                               @RequestBody Long employeeId,
                               @RequestBody Boolean isActive) {
        employeeService.updateIsActive(adminId, employeeId, isActive);
    }

    @GetMapping
    public List<EmployeeShortDto> getEmployees(@RequestHeader(HEADER) Long adminId) {
        return employeeService.getEmployees(adminId);
    }

    @PostMapping("/tip")
    public Tip addTip(@RequestHeader(HEADER) Long employeeId,
                      @RequestBody Tip tip) {
        return employeeService.addTip(employeeId, tip);
    }

    @GetMapping("/tip")
    public Tip getTip(@RequestHeader(HEADER) Long employeeId) {
        return employeeService.getTip(employeeId);
    }

    @DeleteMapping("/tip")
    public void deleteTip(@RequestHeader(HEADER) Long employeeId) {
        employeeService.deleteTip(employeeId);
    }
}
