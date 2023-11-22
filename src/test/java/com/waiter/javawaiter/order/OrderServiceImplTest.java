package com.waiter.javawaiter.order;

import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.dish.storage.DishRepository;
import com.waiter.javawaiter.enums.Status;
import com.waiter.javawaiter.enums.Type;
import com.waiter.javawaiter.exception.NotFoundException;
import com.waiter.javawaiter.exception.ValidationViolationException;
import com.waiter.javawaiter.order.dto.OrderDto;
import com.waiter.javawaiter.order.dto.OrderShortDto;
import com.waiter.javawaiter.order.mapper.OrderMapper;
import com.waiter.javawaiter.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderServiceImplTest {
    private final DishRepository repository;
    private final OrderService orderService;
    private final OrderMapper mapper;

    private final Dish kitchen = new Dish(1L, "Свекольник", true, 5, 15L,
            300.0, Status.CREATED, Type.KITCHEN);
    private final Dish lemonade = new Dish(2L, "Лимонад", true, 10, 5L,
            400.0, Status.CREATED, Type.BAR);
    private final Dish coffee = new Dish(3L, "Капучино", true, 3, 5L, 400.0,
            Status.CREATED, Type.BAR);

    @AfterEach
    void afterEach() {
        orderService.deleteOrders(1L);
        repository.deleteAll();
    }

    @Test
    void create_shouldCreateOrder() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(1L, orderShortDto, LocalDateTime.now());

        assertTrue(savedOrder.getDishes().containsAll(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())));
    }

    @Test
    void create_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        assertThrows(NotFoundException.class, () ->
                orderService.create(-1L, orderShortDto, LocalDateTime.now()));
    }

    @Test
    void update_shouldUpdateOrder() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(1L, orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        order.setGuests(10);
        OrderDto updatedOrder = orderService.update(1L, savedOrder.getOrderId(), order);

        assertThat(updatedOrder.getOrderId(), equalTo(savedOrder.getOrderId()));
        assertThat(updatedOrder.getGuests(), equalTo(10));
        assertThat(updatedOrder.getCreationTime(), equalTo(savedOrder.getCreationTime()));
    }

    @Test
    void update_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(1L, orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);

        assertThrows(NotFoundException.class, () ->
                orderService.update(-1L, order.getOrderId(), order));
    }

    @Test
    void update_shouldUpdateIfGuestsIsNull() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(1L, orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        order.setGuests(null);
        OrderDto updatedOrder = orderService.update(1L, savedOrder.getOrderId(), order);

        assertThat(updatedOrder.getOrderId(), equalTo(savedOrder.getOrderId()));
        assertThat(updatedOrder.getGuests(), equalTo(3));
        assertThat(updatedOrder.getCreationTime(), equalTo(savedOrder.getCreationTime()));
    }

    @Test
    void update_shouldUpdateIfDishesAreEmpty() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(1L, orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        order.setDishes(new ArrayList<>());
        OrderDto updatedOrder = orderService.update(1L, savedOrder.getOrderId(), order);

        assertThat(updatedOrder.getOrderId(), equalTo(savedOrder.getOrderId()));
        assertThat(updatedOrder.getGuests(), equalTo(3));
        assertThat(updatedOrder.getCreationTime(), equalTo(savedOrder.getCreationTime()));
        assertFalse(updatedOrder.getDishes().isEmpty());
    }

    @Test
    void update_shouldUpdateIfAddingNewDishToOrder() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        Dish thisCoffee = repository.save(coffee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(1L, orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        List<Long> dishes =
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId(), thisCoffee.getDishId()));
        order.setDishes(dishes);
        OrderDto updatedOrder = orderService.update(1L, savedOrder.getOrderId(), order);

        assertThat(updatedOrder.getOrderId(), equalTo(savedOrder.getOrderId()));
        assertThat(updatedOrder.getGuests(), equalTo(savedOrder.getGuests()));
        assertThat(updatedOrder.getCreationTime(), equalTo(savedOrder.getCreationTime()));
        assertEquals(3, updatedOrder.getDishes().size());
    }

    @Test
    void update_shouldThrowExceptionIfChangingCreationTime() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(1L, orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        order.setCreationTime(LocalDateTime.now().plusSeconds(10));

        assertThrows(ValidationViolationException.class, () ->
                orderService.update(1L, order.getOrderId(), order));
    }

    @Test
    void deleteById_shouldDeleteById() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(1L, orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        orderService.deleteById(1L, order.getOrderId());

        assertTrue(orderService.getOrders(1L).isEmpty());
    }

    @Test
    void deleteById_shouldThrowExceptionIfIdIsIncorrect() {
        assertThrows(NotFoundException.class, () ->
                orderService.deleteById(1L, 999L));
    }

    @Test
    void deleteById_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        assertThrows(NotFoundException.class, () ->
                orderService.deleteById(-1L, 1L));
    }

    @Test
    void deleteOrders_shouldDeleteOrders() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        orderService.create(1L, orderShortDto, LocalDateTime.now());
        orderService.deleteOrders(1L);

        assertTrue(orderService.getOrders(1L).isEmpty());
    }

    @Test
    void deleteOrders_shouldReturnEmptyList() {
        assertTrue(orderService.getOrders(1L).isEmpty());
    }

    @Test
    void getById_shouldReturnOrder() {
        Dish thisKitchen = repository.save(kitchen);
        Dish thisLemonade = repository.save(lemonade);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(thisKitchen.getDishId(), thisLemonade.getDishId())),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(1L, orderShortDto, LocalDateTime.now());
        OrderDto returnedOrder = orderService.getById(1L, savedOrder.getOrderId());

        assertThat(savedOrder, equalTo(returnedOrder));
    }

    @Test
    void getById_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        assertThrows(NotFoundException.class, () ->
                orderService.getById(-1L, 1L));
    }

    @Test
    void getById_shouldThrowExceptionIfIdIsIncorrect() {
        assertThrows(NotFoundException.class, () ->
                orderService.getById(1L, 999L));
    }
}
