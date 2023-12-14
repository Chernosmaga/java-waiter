package com.waiter.javawaiter.shift;

import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.employee.repository.EmployeeRepository;
import com.waiter.javawaiter.employee.service.EmployeeService;
import com.waiter.javawaiter.exception.NotFoundException;
import com.waiter.javawaiter.shift.model.Shift;
import com.waiter.javawaiter.shift.repository.ShiftRepository;
import com.waiter.javawaiter.shift.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ShiftServiceImplTest {
    private final ShiftService service;
    private final ShiftRepository shiftRepository;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    private final EmployeeDto adminDto = new EmployeeDto(1L, "89996600000", "Alex",
            "Alexandrov", "alex.alexandrov");
    private final Employee admin = new Employee(1L, "89996600000", "Alex",
            "Alexandrov", "alex.alexandrov", true, true);
    private final EmployeeDto employeeDto = new EmployeeDto(2L, "89601234567", "Maria",
            "Makarova", "mashamasha1998");

    @AfterEach
    void afterEach() {
        shiftRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void open_shouldOpenEmployeeShift() {
        EmployeeShortDto admin = employeeService.createAdmin(adminDto);
        Shift shift = service.open(admin.getEmployeeId(),
                LocalDateTime.of(2023, 12, 9, 12, 25, 0));

        assertThat(shift.getShiftStart(),
                equalTo(LocalDateTime.of(2023, 12, 9, 12, 25, 0)));
    }

    @Test
    void open_shouldNotOpenIdEmployeeIdIsIncorrect() {
        assertThrows(NotFoundException.class,
                () -> service.open(999L,
                        LocalDateTime.of(2023, 12, 9, 12, 25, 0)));
    }

    @Test
    void close_shouldCloseShift() {
        EmployeeShortDto admin = employeeService.createAdmin(adminDto);
        Shift opened = service.open(admin.getEmployeeId(),
                LocalDateTime.of(2023, 12, 9, 12, 25, 0));
        Shift closed = service.close(admin.getEmployeeId(), opened.getShiftId(),
                LocalDateTime.of(2023, 12, 9, 12, 30, 0));

        assertThat(closed.getShiftStart(),
                equalTo(LocalDateTime.of(2023, 12, 9, 12, 25, 0)));
        assertThat(closed.getShiftEnd(),
                equalTo(LocalDateTime.of(2023, 12, 9, 12, 30, 0)));
    }

    @Test
    void close_shouldThrowExceptionIfNoOpenShifts() {
        EmployeeShortDto admin = employeeService.createAdmin(adminDto);

        assertThrows(NotFoundException.class,
                () -> service.close(admin.getEmployeeId(), 999L, LocalDateTime.now()));
    }

    @Test
    void getByDate_shouldReturnShiftByDate() {
        EmployeeShortDto admin = employeeService.createAdmin(adminDto);
        Shift opened = service.open(admin.getEmployeeId(),
                LocalDateTime.of(2023, 10, 9, 13, 25, 0));
        Shift closed = service.close(admin.getEmployeeId(), opened.getShiftId(),
                LocalDateTime.of(2023, 10, 9, 18, 30, 0));
        Shift found = service.getByDate(admin.getEmployeeId(),
                LocalDateTime.of(2023, 10, 9, 14, 0, 0));

        assertThat(opened.getShiftStart(), equalTo(found.getShiftStart()));
        assertThat(closed.getShiftEnd(), equalTo(found.getShiftEnd()));
    }

    @Test
    void getForPeriod_shouldReturnShiftForPeriod() {
        EmployeeShortDto admin = employeeService.createAdmin(adminDto);
        Shift opened = service.open(admin.getEmployeeId(),
                LocalDateTime.of(2023, 10, 9, 9, 25, 0));
        Shift closed = service.close(admin.getEmployeeId(), opened.getShiftId(),
                LocalDateTime.of(2023, 10, 9, 18, 30, 0));
        List<Shift> found = service.getForPeriod(admin.getEmployeeId(),
                LocalDateTime.of(2023, 10, 9, 8, 0, 0),
                LocalDateTime.of(2023, 10, 9, 12, 25, 0));

        assertFalse(found.isEmpty());
        assertTrue(found.contains(closed));
    }
}
