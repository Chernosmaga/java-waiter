package com.waiter.javawaiter.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waiter.javawaiter.employee.controller.EmployeeController;
import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.EmployeeForAdminDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.employee.service.EmployeeService;
import com.waiter.javawaiter.exception.ValidationViolationException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private final EmployeeDto adminDto = new EmployeeDto(1L, "89996600000", "Alex",
            "Alexandrov", "alex.alexandrov");
    private final EmployeeShortDto adminShortDto = new EmployeeShortDto(1L, "89996600000",
            "Alex", "Alexandrov");
    private final EmployeeDto employeeDto = new EmployeeDto(2L, "89601234567", "Maria",
            "Makarova", "mashamasha1998");
    private final EmployeeForAdminDto employeeForAdminDto = new EmployeeForAdminDto(2L, "89601234567",
            "Maria", "Makarova", true, false);
    private final EmployeeShortDto employeeShortDto = new EmployeeShortDto(2L, "89601234567",
            "Maria", "Makarova");

    @Test
    @SneakyThrows
    void createAdmin_shouldCreateAdmin() {
        when(employeeService.createAdmin(any()))
                .thenReturn(adminShortDto);

        mvc.perform(post("/employee")
                        .content(mapper.writeValueAsString(adminDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(adminDto.getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.phone", is(adminDto.getPhone())))
                .andExpect(jsonPath("$.firstName", is(adminDto.getFirstName())))
                .andExpect(jsonPath("$.surname", is(adminDto.getSurname())));
    }

    @Test
    @SneakyThrows
    void create_shouldCreateWaiter() {
        when(employeeService.create(any(Long.class), any()))
                .thenReturn(employeeShortDto);

        mvc.perform(post("/employee/1")
                        .content(mapper.writeValueAsString(adminDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(employeeDto.getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.phone", is(employeeDto.getPhone())))
                .andExpect(jsonPath("$.firstName", is(employeeDto.getFirstName())))
                .andExpect(jsonPath("$.surname", is(employeeDto.getSurname())));
    }

    @Test
    @SneakyThrows
    void create_shouldThrowExceptionIfPhoneIsIncorrect() {
        when(employeeService.create(any(Long.class), any()))
                .thenThrow(new ValidationViolationException("Неверный номер телефона"));
    }

    @Test
    @SneakyThrows
    void update_shouldUpdateEmployeeData() {
        EmployeeShortDto thisEmployeeShortDto = new EmployeeShortDto(2L, "89601234567",
                "Maria", "Andreeva");

        when(employeeService.update(any(Long.class), any(Long.class), any()))
                .thenReturn(thisEmployeeShortDto);

        mvc.perform(put("/employee/1")
                        .content(mapper.writeValueAsString(employeeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Employee-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(thisEmployeeShortDto.getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.phone", is(thisEmployeeShortDto.getPhone())))
                .andExpect(jsonPath("$.firstName", is(thisEmployeeShortDto.getFirstName())))
                .andExpect(jsonPath("$.surname", is(thisEmployeeShortDto.getSurname())));
    }

    @Test
    @SneakyThrows
    void get_shouldReturnEmployeeData() {
        when(employeeService.get(any()))
                .thenReturn(employeeDto);

        mvc.perform(get("/employee/89601234567")
                        .content(mapper.writeValueAsString(employeeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId", is(employeeDto.getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.phone", is(employeeDto.getPhone())))
                .andExpect(jsonPath("$.firstName", is(employeeDto.getFirstName())))
                .andExpect(jsonPath("$.surname", is(employeeDto.getSurname())))
                .andExpect(jsonPath("$.userPassword", is(employeeDto.getUserPassword())));
    }

    @Test
    @SneakyThrows
    void getByAdmin_shouldReturnDataForAdmin() {
        when(employeeService.getByAdmin(any(Long.class), any(Long.class)))
                .thenReturn(employeeForAdminDto);

        mvc.perform(get("/employee/admin/1")
                        .content(mapper.writeValueAsString(employeeDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Employee-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId", is(employeeForAdminDto.getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.phone", is(employeeForAdminDto.getPhone())))
                .andExpect(jsonPath("$.firstName", is(employeeForAdminDto.getFirstName())))
                .andExpect(jsonPath("$.surname", is(employeeForAdminDto.getSurname())))
                .andExpect(jsonPath("$.isActive", is(employeeForAdminDto.getIsActive())))
                .andExpect(jsonPath("$.isAdmin", is(employeeForAdminDto.getIsAdmin())));
    }

    @Test
    @SneakyThrows
    void updateIsActive_shouldDeactivateEmployeeAccount() {
        employeeService.updateIsActive(1L, 1L, false);

        verify(employeeService, Mockito.times(1))
                .updateIsActive(1L, 1L, false);
    }

    @Test
    @SneakyThrows
    void getEmployees_shouldReturnEmployeesData() {
        when(employeeService.getEmployees(any(Long.class)))
                .thenReturn(List.of(employeeShortDto));

        mvc.perform(get("/employee")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Employee-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].employeeId", is(employeeDto.getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.[0].phone", is(employeeDto.getPhone())))
                .andExpect(jsonPath("$.[0].firstName", is(employeeDto.getFirstName())))
                .andExpect(jsonPath("$.[0].surname", is(employeeDto.getSurname())));
    }
}
