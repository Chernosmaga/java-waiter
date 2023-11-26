package com.waiter.javawaiter.order.dto;

import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.enums.Status;
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
    private Integer guests;
    private List<Long> dishes;
    private EmployeeShortDto employee;
    private Status status;
    private LocalDateTime creationTime;
    private LocalDateTime billTime;
    private Integer total;
}
