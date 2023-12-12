package com.waiter.javawaiter.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeForAdminDto {
    private Long employeeId;
    private String phone;
    private String firstName;
    private String surname;
    private Boolean isActive;
    private Boolean isAdmin;
}
