package com.waiter.javawaiter.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeShortDto {
    private Long employeeId;
    private String phone;
    private String firstName;
    private String surname;
}
