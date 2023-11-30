package com.waiter.javawaiter.order.dto;

import com.waiter.javawaiter.dish.dto.DishForOrderDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderForAdminDto {
    private Long orderId;
    private Integer guests;
    private List<DishForOrderDto> dishes;
    private EmployeeShortDto employee;
}
