package com.waiter.javawaiter.employee.mapper;

import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.EmployeeForAdminDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.employee.repository.EmployeeRepository;
import com.waiter.javawaiter.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final EmployeeRepository repository;

    public Employee toEmployee(EmployeeDto employee) {
        return new Employee(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname(),
                employee.getUserPassword());
    }

    public EmployeeDto toEmployeeDto(Employee employee) {
        return new EmployeeDto(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname(),
                employee.getUserPassword());
    }

    public EmployeeDto toEmployeeDto(EmployeeShortDto employee) {
        Employee foundEmployee = repository.findById(employee.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return new EmployeeDto(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname(),
                foundEmployee.getUserPassword());
    }

    public EmployeeShortDto toEmployeeShortDto(Employee employee) {
        return new EmployeeShortDto(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname());
    }

    public EmployeeForAdminDto toEmployeeForAdminDto(Employee employee) {
        return new EmployeeForAdminDto(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname(),
                employee.getIsActive(),
                employee.getIsAdmin());
    }
}
