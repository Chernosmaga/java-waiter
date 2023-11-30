package com.waiter.javawaiter.order.mapper;

import com.waiter.javawaiter.dish.mapper.DishMapper;
import com.waiter.javawaiter.employee.mapper.EmployeeMapper;
import com.waiter.javawaiter.order.dto.OrderDto;
import com.waiter.javawaiter.order.dto.OrderShortDto;
import com.waiter.javawaiter.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final EmployeeMapper mapper;
    private final DishMapper dishMapper;

    public OrderDto toOrderDto(Order order) {
        return new OrderDto(
                order.getOrderId(),
                order.getGuests(),
                order.getDishes().stream().map(dishMapper::toDishForOrderDto).collect(Collectors.toList()),
                mapper.toEmployeeShortDto(order.getEmployee()),
                order.getCreationTime(),
                order.getBillTime(),
                order.getTotal());
    }

    public Order toOrder(OrderShortDto order) {
        return new Order(
                order.getOrderId(),
                order.getGuests(),
                order.getDishes().stream().map(dishMapper::toDish).collect(Collectors.toList()),
                order.getCreationTime());
    }

    public OrderShortDto toOrderShortDto(OrderDto order) {
        return new OrderShortDto(
                order.getOrderId(),
                order.getGuests(),
                order.getDishes(),
                order.getCreationTime());
    }
}
