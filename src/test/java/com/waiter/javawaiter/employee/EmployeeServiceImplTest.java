package com.waiter.javawaiter.employee;

import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.EmployeeForAdminDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.employee.mapper.EmployeeMapper;
import com.waiter.javawaiter.employee.repository.EmployeeRepository;
import com.waiter.javawaiter.employee.service.EmployeeService;
import com.waiter.javawaiter.exception.AccessViolationException;
import com.waiter.javawaiter.exception.AlreadyExistsException;
import com.waiter.javawaiter.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EmployeeServiceImplTest {
    private final EmployeeService service;
    private final EmployeeMapper mapper;
    private final EmployeeRepository repository;

    private final EmployeeDto adminDto = new EmployeeDto(1L, "89996600000", "Alex",
            "Alexandrov", "alex.alexandrov");
    private final EmployeeDto employeeDto = new EmployeeDto(2L, "89601234567", "Maria",
            "Makarova", "mashamasha1998");
    private final EmployeeDto employeeDto1 = new EmployeeDto(3L, "89601234550", "Anna",
            "Ivanova", "ivanova1996");

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

    @Test
    void createAdmin_shouldCreateAdmin() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);

        assertThat(thisAdmin.getPhone(), equalTo(adminDto.getPhone()));
        assertThat(thisAdmin.getFirstName(), equalTo(adminDto.getFirstName()));
        assertThat(thisAdmin.getSurname(), equalTo(adminDto.getSurname()));
    }

    @Test
    void createAdmin_shouldThrowExceptionIfAdminExists() {
        service.createAdmin(adminDto);

        assertThrows(AlreadyExistsException.class,
                () -> service.createAdmin(adminDto));
    }

    @Test
    void create_shouldCreateUser() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThat(thisEmployee.getPhone(), equalTo(employeeDto.getPhone()));
        assertThat(thisEmployee.getSurname(), equalTo(employeeDto.getSurname()));
    }

    @Test
    void create_shouldThrowExceptionIfNotAdminCreating() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(AccessViolationException.class,
                () -> service.create(thisEmployee.getEmployeeId(), employeeDto1));
    }

    @Test
    void create_shouldThrowExceptionIfUserExists() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(AlreadyExistsException.class,
                () -> service.create(thisAdmin.getEmployeeId(), employeeDto));
    }

    @Test
    void create_shouldThrowExceptionIfAdminIdIsIncorrect() {
        assertThrows(NotFoundException.class,
                () -> service.create(999L, employeeDto));
    }

    @Test
    void update_shouldUpdateUser() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);
        thisEmployee.setSurname("Andreevna");
        EmployeeShortDto updatedEmployee = service.update(thisAdmin.getEmployeeId(),
                thisEmployee.getEmployeeId(), mapper.toEmployeeDto(thisEmployee));

        assertThat(thisEmployee.getFirstName(), equalTo(updatedEmployee.getFirstName()));
        assertThat(thisEmployee.getPhone(), equalTo(updatedEmployee.getPhone()));
    }

    @Test
    void update_shouldThrowExceptionIfAdminIdIsIncorrect() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(NotFoundException.class,
                () -> service.update(999L, thisEmployee.getEmployeeId(), mapper.toEmployeeDto(thisEmployee)));
    }

    @Test
    void update_shouldThrowExceptionIfNotAdminUpdating() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(AccessViolationException.class,
                () -> service.update(thisEmployee.getEmployeeId(), thisEmployee.getEmployeeId(),
                        mapper.toEmployeeDto(thisEmployee)));
    }

    @Test
    void update_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(NotFoundException.class,
                () -> service.update(thisAdmin.getEmployeeId(), 999L, mapper.toEmployeeDto(thisEmployee)));
    }

    @Test
    void get_shouldReturnEmployeeInfo() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);
        EmployeeDto returnedEmployee = service.get(thisEmployee.getPhone());

        assertThat(thisEmployee.getPhone(), equalTo(returnedEmployee.getPhone()));
        assertThat(thisEmployee.getFirstName(), equalTo(returnedEmployee.getFirstName()));
        assertThat(thisEmployee.getSurname(), equalTo(returnedEmployee.getSurname()));
    }

    @Test
    void get_shouldThrowExceptionIfIdIsIncorrect() {
        assertThrows(NotFoundException.class,
                () -> service.get("89999999999"));
    }

    @Test
    void getByAdmin_shouldReturnEmployee() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);
        EmployeeForAdminDto returnedEmployee = service.getByAdmin(thisAdmin.getEmployeeId(),
                thisEmployee.getEmployeeId());

        assertThat(thisEmployee.getFirstName(), equalTo(returnedEmployee.getFirstName()));
        assertThat(thisEmployee.getSurname(), equalTo(returnedEmployee.getSurname()));
        assertTrue(returnedEmployee.getIsActive());
        assertFalse(returnedEmployee.getIsAdmin());
    }

    @Test
    void getByAdmin_shouldThrowExceptionIfAdminIdIsIncorrect() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(NotFoundException.class,
                () -> service.getByAdmin(999L, thisEmployee.getEmployeeId()));
    }

    @Test
    void getByAdmin_shouldThrowExceptionIfNotAdmin() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(AccessViolationException.class,
                () -> service.getByAdmin(thisEmployee.getEmployeeId(), thisAdmin.getEmployeeId()));
    }

    @Test
    void getByAdmin_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);

        assertThrows(NotFoundException.class,
                () -> service.getByAdmin(thisAdmin.getEmployeeId(), 999L));
    }

    @Test
    void updateIsActive_shouldUpdate() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);
        service.updateIsActive(thisAdmin.getEmployeeId(), thisEmployee.getEmployeeId(), false);

        assertFalse(service.getByAdmin(thisAdmin.getEmployeeId(), thisEmployee.getEmployeeId()).getIsActive());
    }

    @Test
    void updateIsActive_shouldThrowExceptionIfAdminIdIsIncorrect() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(NotFoundException.class,
                () -> service.updateIsActive(999L, thisEmployee.getEmployeeId(), false));
    }

    @Test
    void updateIsActive_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);

        assertThrows(NotFoundException.class,
                () -> service.updateIsActive(thisAdmin.getEmployeeId(), 999L, false));
    }

    @Test
    void updateIsActive_shouldThrowExceptionIfNotAdminUpdating() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(AccessViolationException.class,
                () -> service.updateIsActive(thisEmployee.getEmployeeId(), thisEmployee.getEmployeeId(), false));
    }

    @Test
    void getEmployees_shouldReturnEmployees() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto firstEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);
        EmployeeShortDto secondEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto1);
        List<EmployeeShortDto> employees = service.getEmployees(thisAdmin.getEmployeeId());

        assertFalse(employees.isEmpty());
        assertTrue(employees.contains(firstEmployee));
        assertTrue(employees.contains(secondEmployee));
    }

    @Test
    void getEmployees_shouldThrowExceptionIfAdminIdIsIncorrect() {
        assertThrows(NotFoundException.class,
                () -> service.getEmployees(999L));
    }

    @Test
    void getEmployees_shouldThrowExceptionIfNotAdmin() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);
        EmployeeShortDto thisEmployee = service.create(thisAdmin.getEmployeeId(), employeeDto);

        assertThrows(AccessViolationException.class,
                () -> service.getEmployees(thisEmployee.getEmployeeId()));
    }

    @Test
    void getEmployees_shouldReturnAnEmptyList() {
        EmployeeShortDto thisAdmin = service.createAdmin(adminDto);

        assertTrue(service.getEmployees(thisAdmin.getEmployeeId()).isEmpty());
    }
}
