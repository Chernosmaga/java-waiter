package com.waiter.javawaiter.order.dto;

import com.waiter.javawaiter.dish.dto.DishForOrderDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long orderId;
    private Integer tableNumber;
    private Integer guests;
    private List<DishForOrderDto> dishes;
    private EmployeeShortDto employee;
    private LocalDateTime creationTime;
    private LocalDateTime billTime;
    private Double total;
}
