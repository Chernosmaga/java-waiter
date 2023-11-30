package com.waiter.javawaiter.order;

import com.waiter.javawaiter.comment.dto.CommentShortDto;
import com.waiter.javawaiter.dish.dto.DishForOrderDto;
import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.dish.repository.DishRepository;
import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.employee.repository.EmployeeRepository;
import com.waiter.javawaiter.enums.Status;
import com.waiter.javawaiter.enums.Type;
import com.waiter.javawaiter.exception.AccessViolationException;
import com.waiter.javawaiter.exception.NotFoundException;
import com.waiter.javawaiter.exception.ValidationViolationException;
import com.waiter.javawaiter.order.dto.OrderDto;
import com.waiter.javawaiter.order.dto.OrderShortDto;
import com.waiter.javawaiter.order.mapper.OrderMapper;
import com.waiter.javawaiter.order.repository.OrderRepository;
import com.waiter.javawaiter.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderServiceImplTest {
    private final DishRepository repository;
    private final EmployeeRepository employeeRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    private final Employee employee = new Employee(2L, "89601234567", "Maria",
            "Makarova", "mashamasha1998", true, true);
    private final Dish kitchen = new Dish(1L, "Свекольник", 5, 15L,
            300.0, Status.CREATED, Type.KITCHEN);
    private final DishForOrderDto kitchenForOrderDto = new DishForOrderDto(1L, "Свекольник", 300.0,
            null, Type.KITCHEN);
    private final Dish lemonade = new Dish(2L, "Лимонад", 10, 5L,
            400.0, Status.CREATED, Type.BAR);
    private final DishForOrderDto lemonadeForOrderDto = new DishForOrderDto(2L, "Лимонад", 400.0,
            new CommentShortDto(1L, "Без льда"), Type.BAR);
    private final Dish coffee = new Dish(3L, "Капучино", 3, 5L, 400.0,
            Status.CREATED, Type.BAR);
    private final DishForOrderDto coffeeForOrderDto = new DishForOrderDto(3L, "Капучино", 400.0,
            new CommentShortDto(3L, "С корицей"), Type.BAR);

    @BeforeEach
    void beforeEach() {
        repository.save(kitchen);
        repository.save(lemonade);
        repository.save(coffee);
    }

    @AfterEach
    void afterEach() {
        orderRepository.deleteAll();
        repository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void create_shouldCreateOrder() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto, LocalDateTime.now());

        assertTrue(savedOrder.getDishes().containsAll(List.of(kitchenForOrderDto, lemonadeForOrderDto)));
    }

    @Test
    void create_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        assertThrows(NotFoundException.class, () ->
                orderService.create(999L, orderShortDto, LocalDateTime.now()));
    }

    @Test
    void update_shouldUpdateOrder() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        order.setGuests(10);
        OrderDto updatedOrder = orderService.update(thisEmployee.getEmployeeId(), savedOrder.getOrderId(), order);

        assertThat(updatedOrder.getOrderId(), equalTo(savedOrder.getOrderId()));
        assertThat(updatedOrder.getGuests(), equalTo(10));
        assertThat(updatedOrder.getCreationTime(), equalTo(savedOrder.getCreationTime()));
    }

    @Test
    void update_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);

        assertThrows(NotFoundException.class, () ->
                orderService.update(999L, order.getOrderId(), order));
    }

    @Test
    void update_shouldUpdateIfGuestsIsNull() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        order.setGuests(null);
        OrderDto updatedOrder = orderService.update(thisEmployee.getEmployeeId(), savedOrder.getOrderId(), order);

        assertThat(updatedOrder.getOrderId(), equalTo(savedOrder.getOrderId()));
        assertThat(updatedOrder.getGuests(), equalTo(3));
        assertThat(updatedOrder.getCreationTime(), equalTo(savedOrder.getCreationTime()));
    }

    @Test
    void update_shouldUpdateIfDishesAreEmpty() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        order.setDishes(new ArrayList<>());
        OrderDto updatedOrder = orderService.update(thisEmployee.getEmployeeId(), savedOrder.getOrderId(), order);

        assertThat(updatedOrder.getOrderId(), equalTo(savedOrder.getOrderId()));
        assertThat(updatedOrder.getGuests(), equalTo(3));
        assertThat(updatedOrder.getCreationTime(), equalTo(savedOrder.getCreationTime()));
        assertFalse(updatedOrder.getDishes().isEmpty());
    }

    @Test
    void update_shouldUpdateIfAddingNewDishToOrder() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        List<DishForOrderDto> dishes =
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto, coffeeForOrderDto));
        order.setDishes(dishes);
        OrderDto updatedOrder = orderService.update(thisEmployee.getEmployeeId(), savedOrder.getOrderId(), order);

        assertThat(updatedOrder.getOrderId(), equalTo(savedOrder.getOrderId()));
        assertThat(updatedOrder.getGuests(), equalTo(savedOrder.getGuests()));
        assertThat(updatedOrder.getCreationTime(), equalTo(savedOrder.getCreationTime()));
        assertEquals(3, updatedOrder.getDishes().size());
    }

    @Test
    void update_shouldThrowExceptionIfChangingCreationTime() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        order.setCreationTime(LocalDateTime.now().plusSeconds(10));

        assertThrows(ValidationViolationException.class, () ->
                orderService.update(thisEmployee.getEmployeeId(), order.getOrderId(), order));
    }

    @Test
    void deleteById_shouldDeleteById() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto, LocalDateTime.now());
        OrderShortDto order = mapper.toOrderShortDto(savedOrder);
        orderService.deleteById(thisEmployee.getEmployeeId(), order.getOrderId());

        assertTrue(orderService.getOrders(thisEmployee.getEmployeeId(), 0, 10).isEmpty());
    }

    @Test
    void deleteById_shouldThrowExceptionIfIdIsIncorrect() {
        Employee thisEmployee = employeeRepository.save(employee);

        assertThrows(NotFoundException.class, () ->
                orderService.deleteById(thisEmployee.getEmployeeId(), 999L));
    }

    @Test
    void deleteById_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        assertThrows(NotFoundException.class, () ->
                orderService.deleteById(999L, 1L));
    }

    @Test
    void getById_shouldReturnOrder() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto savedOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto, LocalDateTime.now());
        OrderDto returnedOrder = orderService.getById(thisEmployee.getEmployeeId(), savedOrder.getOrderId());
        List<DishForOrderDto> dishes = savedOrder.getDishes();
        List<DishForOrderDto> returnedDishes = savedOrder.getDishes();

        assertThat(savedOrder.getDishes().size(), equalTo(returnedOrder.getDishes().size()));
        assertThat(dishes.size(), equalTo(returnedDishes.size()));
        assertThat(dishes.get(0), equalTo(returnedDishes.get(0)));
    }

    @Test
    void getById_shouldThrowExceptionIfEmployeeIdIsIncorrect() {
        assertThrows(NotFoundException.class, () ->
                orderService.getById(999L, 1L));
    }

    @Test
    void getById_shouldThrowExceptionIfIdIsIncorrect() {
        Employee thisEmployee = employeeRepository.save(employee);

        assertThrows(NotFoundException.class, () ->
                orderService.getById(thisEmployee.getEmployeeId(), 999L));
    }

    @Test
    void getOrders_shouldReturnListOfOrders() {
        Employee thisEmployee = employeeRepository.save(employee);
        OrderShortDto firstOrder = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderShortDto secondOrder = new OrderShortDto(2L, 1,
                new ArrayList<>(List.of(lemonadeForOrderDto)), LocalDateTime.now());
        OrderDto first = orderService.create(thisEmployee.getEmployeeId(), firstOrder, LocalDateTime.now());
        OrderDto second = orderService.create(thisEmployee.getEmployeeId(), secondOrder,
                LocalDateTime.now().plusMinutes(2));
        List<OrderDto> orders = orderService.getOrders(thisEmployee.getEmployeeId(), 0, 10);

        assertFalse(orders.isEmpty());
        assertEquals(orders.size(), List.of(first, second).size());
    }

    @Test
    void deleteById_shouldThrowExceptionIfAccessViolated() {
        Employee admin = employeeRepository.save(employee);
        Employee thisEmployee = employeeRepository.save(new Employee(3L, "89991112233",
                "Ivan", "Ivanov", "ivanovivan10", true, false));
        OrderShortDto thisOrder = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto first = orderService.create(admin.getEmployeeId(), thisOrder, LocalDateTime.now());

        assertThrows(AccessViolationException.class,
                () -> orderService.deleteById(thisEmployee.getEmployeeId(), first.getOrderId()));
    }

    @Test
    void getOrders_shouldReturnOrdersByEmployee() {
        Employee admin = employeeRepository.save(employee);
        Employee thisEmployee = employeeRepository.save(new Employee(3L, "89991112233",
                "Ivan", "Ivanov", "ivanovivan10", true, false));
        OrderShortDto thisOrder = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto first = orderService.create(thisEmployee.getEmployeeId(), thisOrder, LocalDateTime.now());
        List<OrderDto> orders = orderService.getOrders(thisEmployee.getEmployeeId(), 0, 10);

        assertFalse(orders.isEmpty());
    }

    @Test
    void getById_shouldThrowExceptionIfNeitherAdminOrEmployeeGettingDish() {
        employeeRepository.save(employee);
        Employee ivan = employeeRepository.save(new Employee(3L, "89991112233",
                "Ivan", "Ivanov", "ivanovivan10", true, false));
        Employee andrew = employeeRepository.save(new Employee(5L, "89001110022", "Andrew",
                "Ivanov", "andrewIvanov", true, false));
        OrderShortDto thisOrder = new OrderShortDto(1L, 3,
                new ArrayList<>(List.of(kitchenForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto createdOrder = orderService.create(ivan.getEmployeeId(), thisOrder, LocalDateTime.now());

        assertThrows(AccessViolationException.class,
                () -> orderService.getById(andrew.getEmployeeId(), createdOrder.getOrderId()));
    }
}
