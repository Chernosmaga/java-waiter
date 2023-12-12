package com.waiter.javawaiter.order.service;

import com.waiter.javawaiter.comment.mapper.CommentMapper;
import com.waiter.javawaiter.comment.model.Comment;
import com.waiter.javawaiter.comment.repository.CommentRepository;
import com.waiter.javawaiter.dish.dto.DishForOrderDto;
import com.waiter.javawaiter.dish.mapper.DishMapper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DishMapper dishMapper;
    private final CommentRepository commentRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderMapper orderMapper;
    private final CommentMapper commentMapper;
    private final DishRepository dishRepository;

    @Override
    public OrderDto create(Long employeeId, OrderShortDto order, LocalDateTime localDateTime) {
        log.info("createOrder({}, {}, {})", employeeId, order, localDateTime);
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        var thisOrder = orderMapper.toOrder(order);
        decreaseQuantity(thisOrder.getDishes());
        thisOrder.setEmployee(employee);
        thisOrder.setCreationTime(localDateTime);
        thisOrder.setBillTime(null);
        thisOrder.setTotal(null);
        saveCommentsForDishes(order.getDishes());
        var orderForReturn = orderMapper.toOrderDto(orderRepository.save(thisOrder));
        List<DishForOrderDto> list = squashDishes(orderForReturn.getDishes());
        orderForReturn.setDishes(list);
        log.info("Добавлен заказ: {}", thisOrder);
        return orderForReturn;
    }

    @Override
    public OrderDto update(Long employeeId, Long orderId, OrderShortDto order) {
        log.info("updateOrder({}, {}, {})", employeeId, order, orderId);
        isAcceptable(employeeId, orderId);
        var thisOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (thisOrder.getBillTime() != null || thisOrder.getTotal() != null) {
            throw new ValidationViolationException("Заказ закрыт, нельзя изменить данные");
        }
        if (order.getTableNumber() != null) {
            thisOrder.setTableNumber(order.getTableNumber());
        }
        if (order.getGuests() != null) {
            thisOrder.setGuests(order.getGuests());
        }
        if (!order.getDishes().isEmpty()) {
            List<Dish> dishes = order.getDishes().stream().map(dishMapper::toDish).toList();
            thisOrder.setDishes(dishes.stream()
                            .peek(dish -> {
                                if (thisOrder.getDishes().stream().anyMatch(dishes::contains)) {
                                    thisOrder.getDishes().add(dish);
                }
            }).collect(Collectors.toList()));
            decreaseQuantity(thisOrder.getDishes());
        }
        if (order.getCreationTime() != thisOrder.getCreationTime()) {
            throw new ValidationViolationException("Нет доступа к изменению времени заказа");
        }
        var orderForReturn = orderMapper.toOrderDto(orderRepository.save(thisOrder));
        saveCommentsForDishes(orderForReturn.getDishes());
        List<DishForOrderDto> dishes = orderForReturn.getDishes();
        orderForReturn.setDishes(getCommentsForDishes(dishes));
        log.info("Обновлен заказ: {}", orderForReturn);
        return orderForReturn;
    }

    @Override
    public void deleteById(Long employeeId, Long orderId) {
        log.info("deleteOrderById({}, {})", employeeId, orderId);
        var order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (order.getBillTime() != null || order.getTotal() != null) {
            throw new ValidationViolationException("Заказ закрыт, нельзя изменить данные");
        }
        List<Long> dishes = order.getDishes().stream().map(Dish::getDishId).toList();
        for (Long dish: dishes) {
            commentRepository.deleteCommentsByDishId(dish);
        }
        isAcceptable(employeeId, orderId);
        orderRepository.deleteById(orderId);
        log.info("Заказ с идентификатором {} удален пользователем с идентификатором {}", orderId, employeeId);
    }

    @Override
    public OrderDto getById(Long employeeId, Long orderId) {
        log.info("getOrderById({}, {})", employeeId, orderId);
        isAcceptable(employeeId, orderId);
        Order thisOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        List<DishForOrderDto> dishes = thisOrder.getDishes().stream()
                .map(dishMapper::toDishForOrderDto).collect(Collectors.toList());
        var order = orderMapper.toOrderDto(thisOrder);
        order.setDishes(getCommentsForDishes(dishes));
        log.info("Возвращено блюдо: {}, по запросу пользователя с идентификатором {}", order, employeeId);
        return order;
    }

    @Override
    public List<OrderDto> getOrders(Long employeeId, int offset, int limit) {
        log.info("Запрос на получение списка заказов от пользователя: {}", employeeId);
        PageRequest page = PageRequest.of(offset, limit);
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<OrderDto> orders;
        if (employee.getIsAdmin()) {
            orders = orderRepository.findAll(page).stream().map(orderMapper::toOrderDto).collect(Collectors.toList());
        } else {
            orders = orderRepository.findAllByEmployee(employee, page).stream()
                    .map(orderMapper::toOrderDto).collect(Collectors.toList());
        }
        for (OrderDto o: orders) {
            o.setDishes(getCommentsForDishes(o.getDishes()));
        }
        log.info("Возвращён список заказов: {}, для пользователя: {}", orders, employee);
        return orders;
    }

    private void isAcceptable(Long employeeId, Long orderId) {
        log.info("isAcceptable: {}, {}", employeeId, orderId);
        var employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        var thisOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Заказ не найден"));
        if (!thisOrder.getEmployee().getEmployeeId().equals(employeeId) || !employee.getIsAdmin()) {
            throw new AccessViolationException("Нет доступа к изменению заказа");
        }
    }

    private List<Dish> updateStatus(Order order) {
        return order.getDishes().stream().peek(dish -> {
            if (order.getCreationTime().equals(order.getCreationTime().plusMinutes(1L))) {
                dish.setStatus(Status.IN_PROGRESS);
            }
            if (LocalDateTime.now().equals(order.getCreationTime().plusMinutes(dish.getTimeLimit() + 1L))) {
                dish.setStatus(Status.DONE);
            }
            if (order.getCreationTime().equals(order.getCreationTime().plusMinutes(dish.getTimeLimit() + 1L + 10L))) {
                dish.setStatus(Status.SERVED);
            }
        }).collect(Collectors.toList());
    }

    private List<DishForOrderDto> getCommentsForDishes(List<DishForOrderDto> dishes) {
        log.debug("getCommentsForDishes({})", dishes);
        List<DishForOrderDto> list = new ArrayList<>();
        for (DishForOrderDto dish: dishes) {
            Comment comment = commentRepository.findByDishId(dish.getDishId());
            dish.setComment(commentMapper.toCommentShortDto(comment));
            list.add(dish);
        }
        log.info("Возвращен список блюд с комментариями: {}", list);
        return list;
    }

    private void saveCommentsForDishes(List<DishForOrderDto> dishes) {
        log.debug("saveCommentsForDishes({})", dishes);
        for (DishForOrderDto dish: dishes) {
            if (dish.getComment() == null) {
                continue;
            }
            commentRepository.save(new Comment(dish.getComment().getCommentId(),
                    dish.getDishId(), dish.getComment().getComment()));
        }
        log.info("Сохранены комментарии для блюд: {}", dishes);
    }

    private void decreaseQuantity(List<Dish> dishes) {
        log.debug("decreaseQuantity({})", dishes);
        for (Dish dish: dishes) {
            if (dish.getQuantity() != 0) {
                dish.setQuantity(dish.getQuantity() - 1);
                dishRepository.save(dish);
            } else {
                throw new ValidationViolationException("Блюда " + dish + " нет в наличии");
            }
        }
        log.info("Уменьшено количество для каждого блюда в заказе: {}", dishes);
    }

    private List<DishForOrderDto> squashDishes(List<DishForOrderDto> dishes) {
        log.debug("squashDishes({})", dishes);
        Map<DishForOrderDto, Integer> repeats = new HashMap<>();
        Iterator<DishForOrderDto> iterator = dishes.iterator();
        while (iterator.hasNext()) {
            DishForOrderDto dish = iterator.next();
            if (repeats.containsKey(dish) && dish.getComment() == null) {
                repeats.put(dish, repeats.get(dish) + 1);
                iterator.remove();
            } else {
                repeats.put(dish, 1);
            }
        }
        List<DishForOrderDto> list = new ArrayList<>();
        for (Map.Entry<DishForOrderDto, Integer> map: repeats.entrySet()) {
            map.getKey().setQuantityForOrder(map.getValue());
            list.add(map.getKey());
        }
        log.info("Возвращён список блюд после объединения: {}", list);
        return list;
    }
}
