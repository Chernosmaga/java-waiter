package com.waiter.javawaiter.order.dto;

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
    private List<Long> dishes;
    private String employee;
}
