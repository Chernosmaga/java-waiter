package com.waiter.javawaiter.employee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Long employeeId;
    private String phone;
    private String firstName;
    private String surname;
    private String userPassword;
    private Boolean isActive;
    private Boolean isAdmin;
}
