package com.waiter.javawaiter.order.mapper;

import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.dish.repository.DishRepository;
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
    private final DishRepository dishRepository;
    private final EmployeeMapper mapper;

    public OrderDto toOrderDto(Order order) {
        return new OrderDto(order.getOrderId(),
                order.getGuests(),
                order.getDishes().stream().map(Dish::getDishId).collect(Collectors.toList()),
                mapper.toEmployeeShortDto(order.getEmployee()),
                order.getStatus(),
                order.getCreationTime(),
                order.getBillTime(),
                order.getTotal());
    }

    public Order toOrder(OrderShortDto order) {
        return new Order(order.getOrderId(),
                order.getGuests(),
                order.getDishes().stream().map(dishRepository::findByDishId).collect(Collectors.toList()),
                order.getCreationTime());
    }

    public OrderShortDto toOrderShortDto(OrderDto order) {
        return new OrderShortDto(order.getOrderId(),
                order.getGuests(),
                order.getDishes(),
                order.getCreationTime());
    }
}
