package com.waiter.javawaiter.order.service;

import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.dish.repository.DishRepository;
import com.waiter.javawaiter.employee.repository.EmployeeRepository;
import com.waiter.javawaiter.enums.Status;
import com.waiter.javawaiter.exception.AccessViolationException;
import com.waiter.javawaiter.exception.NotFoundException;
import com.waiter.javawaiter.exception.ValidationViolationException;
import com.waiter.javawaiter.order.dto.OrderDto;
import com.waiter.javawaiter.order.dto.OrderShortDto;
import com.waiter.javawaiter.order.mapper.OrderMapper;
import com.waiter.javawaiter.order.model.Order;
import com.waiter.javawaiter.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto create(Long employeeId, OrderShortDto order, LocalDateTime localDateTime) {
        log.info("createOrder({}, {}, {})", employeeId, order, localDateTime);
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        var thisOrder = orderMapper.toOrder(order);
        List<Optional<Dish>> dishes = order.getDishes().stream().map(dishRepository::findById).toList();
        thisOrder.setDishes(dishes.stream().flatMap(Optional::stream).filter(Objects::nonNull).toList());
        thisOrder.setEmployee(employee);
        thisOrder.setStatus(Status.CREATED);
        thisOrder.setBillTime(null);
        thisOrder.setTotal(null);
        log.info("Добавлен заказ: {}", thisOrder);
        return orderMapper.toOrderDto(orderRepository.save(thisOrder));
    }

    @Override
    public OrderDto update(Long employeeId, Long orderId, OrderShortDto order) {
        log.info("updateOrder({}, {}, {})", employeeId, order, orderId);
        isAcceptable(employeeId, orderId);
        var thisOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (order.getGuests() != null) {
            thisOrder.setGuests(order.getGuests());
        }
        if (!order.getDishes().isEmpty()) {
            List<Dish> dishes = order.getDishes().stream().map(dishRepository::findById)
                    .flatMap(Optional::stream)
                    .filter(Objects::nonNull).toList();
            thisOrder.setDishes(dishes.stream()
                            .peek(dish -> {
                                if (thisOrder.getDishes().stream().anyMatch(dishes::contains)) {
                                    thisOrder.getDishes().add(dish);
                }
            }).collect(Collectors.toList()));
        }
        if (order.getCreationTime() != thisOrder.getCreationTime()) {
            throw new ValidationViolationException("Нет доступа к изменению времени заказа");
        }
        log.info("Обновлен заказ: {}", thisOrder);
        return orderMapper.toOrderDto(orderRepository.save(thisOrder));
    }

    @Override
    public void deleteById(Long employeeId, Long orderId) {
        log.info("deleteOrderById({}, {})", employeeId, orderId);
        isAcceptable(employeeId, orderId);
        orderRepository.deleteById(orderId);
        log.info("Заказ с идентификатором {} удален пользователем с идентификатором {}", orderId, employeeId);
    }

    @Override
    public void deleteOrders(Long employeeId) {
        log.info("deleteOrders({})", employeeId);
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (!employee.getIsAdmin()) {
            throw new AccessViolationException("Нет доступа");
        }
        orderRepository.deleteAll();
        log.info("Список заказов очищен пользователем с идентификатором: {}", employeeId);
    }

    @Override
    public OrderDto getById(Long employeeId, Long orderId) {
        log.info("getOrderById({}, {})", employeeId, orderId);
        isAcceptable(employeeId, orderId);
        Order thisOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        log.info("Возвращено блюдо: {}, по запросу пользователя с идентификатором {}", orderId, employeeId);
        return orderMapper.toOrderDto(thisOrder);
    }

    @Override
    public List<OrderDto> getOrders(Long employeeId) {
        log.info("Запрос на получение списка заказов от пользователя: {}", employeeId);
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (!employee.getIsAdmin()) {
            throw new AccessViolationException("Нет доступа к изменению заказа");
        }
        List<Order> orders = orderRepository.findAll();
        log.info("Возвращён список заказов: {}", orders);
        return orders.stream().filter(order -> !Objects.equals(order.getEmployee().getEmployeeId(), employeeId))
                .map(orderMapper::toOrderDto).collect(Collectors.toList());
    }

    private void isAcceptable(Long employeeId, Long orderId) {
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        var thisOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (!thisOrder.getEmployee().getEmployeeId().equals(employeeId) || !employee.getIsAdmin()) {
            throw new AccessViolationException("Нет доступа к изменению заказа");
        }
    }
}
