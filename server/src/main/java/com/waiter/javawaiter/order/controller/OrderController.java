package com.waiter.javawaiter.order.controller;

import com.waiter.javawaiter.order.dto.OrderDto;
import com.waiter.javawaiter.order.dto.OrderShortDto;
import com.waiter.javawaiter.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderDto create(@RequestHeader("Employee-Id") Long employeeId,
                           @Valid @RequestBody OrderShortDto order) {
        return orderService.create(employeeId, order, LocalDateTime.now());
    }

    @PutMapping("/{orderId}")
    public OrderDto update(@RequestHeader("Employee-Id") Long employeeId,
                           @PathVariable Long orderId,
                           @Valid @RequestBody OrderShortDto order) {
        return orderService.update(employeeId, orderId, order);
    }

    @DeleteMapping("/{orderId}")
    public void deleteById(@RequestHeader("Employee-Id") Long employeeId,
                           @PathVariable Long orderId) {
        orderService.deleteById(employeeId, orderId);
    }

    @GetMapping("/{orderId}")
    public OrderDto getById(@RequestHeader("Employee-Id") Long employeeId,
                            @PathVariable Long orderId) {
        return orderService.getById(employeeId, orderId);
    }

    @GetMapping
    public List<OrderDto> getOrders(@RequestHeader("Employee-Id") Long employeeId,
                                    @RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(required = false, defaultValue = "10") int limit) {
        return orderService.getOrders(employeeId, offset, limit);
    }
}
