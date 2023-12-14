package com.waiter.javawaiter.employee.dto;

import com.waiter.javawaiter.employee.model.Employee;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tip {
    private Long tipId;
    @NotBlank(message = "Укажите сотрудника")
    private Employee employee;
    @NotBlank(message = "Укажите QR-код")
    private String qrCode;
}