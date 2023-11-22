package com.waiter.javawaiter.order.service;

import com.waiter.javawaiter.order.dto.OrderDto;
import com.waiter.javawaiter.order.dto.OrderShortDto;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderDto create(Long employeeId, OrderShortDto order, LocalDateTime localDateTime);
    OrderDto update(Long employeeId, Long orderId, OrderShortDto order);
    void deleteById(Long employeeId, Long orderId);
    void deleteOrders(Long employeeId);
    OrderDto getById(Long employeeId, Long orderId);
    List<OrderDto> getOrders(Long employeeId);
}
