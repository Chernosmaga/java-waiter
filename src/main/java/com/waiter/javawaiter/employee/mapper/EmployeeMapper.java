package com.waiter.javawaiter.employee.mapper;

import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.EmployeeForAdminDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.employee.repository.EmployeeRepository;
import com.waiter.javawaiter.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final EmployeeRepository repository;

    public Employee toEmployee(EmployeeDto employee) {
        log.info("Из EmployeeDto в Employee: {}", employee);
        return new Employee(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname(),
                employee.getUserPassword());
    }

    public EmployeeDto toEmployeeDto(Employee employee) {
        log.info("Из Employee в EmployeeDto: {}", employee);
        return new EmployeeDto(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname(),
                employee.getUserPassword());
    }

    public EmployeeDto toEmployeeDto(EmployeeShortDto employee) {
        log.info("Из EmployeeShortDto в EmployeeDto: {}", employee);
        Employee foundEmployee = repository.findById(employee.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return new EmployeeDto(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname(),
                foundEmployee.getUserPassword());
    }

    public EmployeeShortDto toEmployeeShortDto(Employee employee) {
        log.info("Из Employee в EmployeeShortDto: {}", employee);
        return new EmployeeShortDto(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname());
    }

    public EmployeeForAdminDto toEmployeeForAdminDto(Employee employee) {
        log.info("Из Employee в EmployeeForAdminDto: {}", employee);
        return new EmployeeForAdminDto(employee.getEmployeeId(),
                employee.getPhone(),
                employee.getFirstName(),
                employee.getSurname(),
                employee.getIsActive(),
                employee.getIsAdmin());
    }
}
