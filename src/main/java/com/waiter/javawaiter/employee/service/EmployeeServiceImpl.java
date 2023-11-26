package com.waiter.javawaiter.employee.service;

import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.EmployeeForAdminDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.employee.mapper.EmployeeMapper;
import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.employee.repository.EmployeeRepository;
import com.waiter.javawaiter.exception.AccessViolationException;
import com.waiter.javawaiter.exception.AlreadyExistsException;
import com.waiter.javawaiter.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper mapper;

    @Override
    public EmployeeShortDto createAdmin(EmployeeDto employee) {
        if (employeeRepository.existsByPhoneAndFirstNameAndSurname(employee.getPhone(),
                employee.getFirstName(), employee.getSurname())) {
            throw new AlreadyExistsException("Данные о пользователе уже есть в системе");
        }
        Employee thisEmployee = mapper.toEmployee(employee);
        thisEmployee.setIsActive(true);
        thisEmployee.setIsAdmin(true);
        return mapper.toEmployeeShortDto(employeeRepository.save(thisEmployee));
    }

    @Override
    public EmployeeShortDto create(Long adminId, EmployeeDto employee) {
        adminValidation(adminId);
        if (employeeRepository.existsByPhoneAndFirstNameAndSurname(employee.getPhone(),
                employee.getFirstName(), employee.getSurname())) {
            throw new AlreadyExistsException("Данные о пользователе уже есть в системе");
        }
        Employee thisEmployee = mapper.toEmployee(employee);
        thisEmployee.setIsActive(true);
        thisEmployee.setIsAdmin(false);
        return mapper.toEmployeeShortDto(employeeRepository.save(thisEmployee));
    }

    @Override
    public EmployeeShortDto update(Long adminId, Long employeeId, EmployeeDto employee) {
        adminValidation(adminId);
        Employee thisEmployee = fieldsValidation(employeeId, employee);
        return mapper.toEmployeeShortDto(employeeRepository.save(thisEmployee));
    }

    @Override
    public EmployeeDto get(String phone) {
        Employee employee;
        try {
            employee = employeeRepository.findEmployeeByPhone(phone);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (employee == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return mapper.toEmployeeDto(employee);
    }

    @Override
    public EmployeeForAdminDto getByAdmin(Long adminId, Long employeeId) {
        adminValidation(adminId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return mapper.toEmployeeForAdminDto(employee);
    }

    @Override
    public void updateIsActive(Long adminId, Long employeeId, Boolean isActive) {
        adminValidation(adminId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        employee.setIsActive(isActive);
        employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeShortDto> getEmployees(Long adminId) {
        adminValidation(adminId);
        List<Employee> employees = employeeRepository.findEmployeesByIsActive(true);
        return employees.stream().filter(e -> !e.getIsAdmin())
                .map(mapper::toEmployeeShortDto).collect(Collectors.toList());
    }

    private Employee fieldsValidation(Long employeeId, EmployeeDto employee) {
        Employee thisEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (employee.getPhone() != null) {
            thisEmployee.setPhone(employee.getPhone());
        }
        if (employee.getFirstName() != null) {
            thisEmployee.setFirstName(employee.getFirstName());
        }
        if (employee.getSurname() != null) {
            thisEmployee.setSurname(employee.getSurname());
        }
        if (employee.getUserPassword() != null) {
            thisEmployee.setUserPassword(employee.getUserPassword());
        }
        return thisEmployee;
    }

    private void adminValidation(Long adminId) {
        Employee admin = employeeRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (!admin.getIsAdmin()) {
            throw new AccessViolationException("Нет доступа");
        }
    }
}
