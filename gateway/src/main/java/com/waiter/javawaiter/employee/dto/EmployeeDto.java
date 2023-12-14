package com.waiter.javawaiter.employee.dto;

import com.waiter.javawaiter.annotation.Phone;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private Long employeeId;
    @Phone
    private String phone;
    @NotBlank
    private String firstName;
    @NotBlank
    private String surname;
    @NotBlank
    private String userPassword;
}