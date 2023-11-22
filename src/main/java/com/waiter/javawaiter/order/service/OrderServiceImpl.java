package com.waiter.javawaiter.order.service;

import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.dish.storage.DishRepository;
import com.waiter.javawaiter.enums.Status;
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
    private final OrderMapper orderMapper;

    @Override
    public OrderDto create(Long employeeId, OrderShortDto order, LocalDateTime localDateTime) {
//        после добавления employee, сделать запрос на поиск в БД
        log.info("createOrder({}, {}, {})", employeeId, order, localDateTime);
        if (employeeId <= 0) {
            throw new NotFoundException("Пользователя не существует");
        }
        Order thisOrder = orderMapper.toOrder(order);
        log.info("Заказ выглядит вот так: {}", thisOrder);
        List<Optional<Dish>> dishes = order.getDishes().stream().map(dishRepository::findById).toList();
        log.info("Список блюд выглядит вот так: {}", dishes);
        thisOrder.setDishes(dishes.stream().flatMap(Optional::stream).filter(Objects::nonNull).toList());
        thisOrder.setEmployee("Работник");
        thisOrder.setStatus(Status.CREATED);
        thisOrder.setBillTime(null);
        thisOrder.setTotal(null);
        log.info("Добавлен заказ: {}", thisOrder);
        return orderMapper.toOrderDto(orderRepository.save(thisOrder));
    }

    @Override
    public OrderDto update(Long employeeId, Long orderId, OrderShortDto order) {
        log.info("updateOrder({}, {}, {})", employeeId, order, orderId);
        Order thisOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Заказ не найден"));
//        после добавления employee, сделать запрос на поиск в БД
        if (employeeId <= 0) {
            throw new NotFoundException("Пользователь не найден");
        }
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
//        после добавления employee, сделать запрос на поиск в БД
        if (employeeId == 0) {
            throw new NotFoundException("Пользователя не существует");
        }
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException("Заказ не найден");
        }
        orderRepository.deleteById(orderId);
        log.info("Заказ с идентификатором {} удален пользователем с идентификатором {}", orderId, employeeId);
    }

    @Override
    public void deleteOrders(Long employeeId) {
        log.info("deleteOrders({})", employeeId);
        orderRepository.deleteAll();
        log.info("Список заказов очищен пользователем с идентификатором: {}", employeeId);
    }

    @Override
    public OrderDto getById(Long employeeId, Long orderId) {
        log.info("getOrderById({}, {})", employeeId, orderId);
//        после добавления employee, сделать запрос на поиск в БД
        if (employeeId == 0) {
            throw new NotFoundException("Пользователя не существует");
        }
        Order thisOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        log.info("Возвращено блюдо: {}, по запросу пользователя с идентификатором {}", orderId, employeeId);
        return orderMapper.toOrderDto(thisOrder);
    }

    @Override
    public List<OrderDto> getOrders(Long employeeId) {
        log.info("Запрос на получение списка заказов от пользователя: {}", employeeId);
//        после добавления employee, сделать запрос на поиск в БД
        if (employeeId == 0) {
            throw new NotFoundException("Пользователя не существует");
        }
        List<Order> orders = orderRepository.findAll();
        log.info("Возвращён список заказов: {}", orders);
        return orders.stream().map(orderMapper::toOrderDto).collect(Collectors.toList());
    }
}
