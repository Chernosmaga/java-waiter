package com.waiter.javawaiter.printer;

import com.waiter.javawaiter.comment.dto.CommentShortDto;
import com.waiter.javawaiter.dish.dto.DishForOrderDto;
import com.waiter.javawaiter.dish.dto.DishShortDto;
import com.waiter.javawaiter.dish.service.DishService;
import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.employee.service.EmployeeService;
import com.waiter.javawaiter.enums.Type;
import com.waiter.javawaiter.order.dto.OrderDto;
import com.waiter.javawaiter.order.dto.OrderShortDto;
import com.waiter.javawaiter.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PrinterTest {
    private final Printer printer;
    private final EmployeeService employeeService;
    private final DishService dishService;
    private final OrderService orderService;
    private final EmployeeDto employee = new EmployeeDto(2L, "89601234567", "Maria",
            "Makarova", "mashamasha1998");
    private final DishShortDto lemonadeShortDto = new DishShortDto(2L, "Лимонад", 5, 15L,
            400.0, Type.BAR);
    private final DishShortDto coffeeShortDto = new DishShortDto(3L, "Капучино", 3, 5L,
            400.0, Type.BAR);

    @Test
    void printBill_shouldPrintBill() {
        EmployeeShortDto thisEmployee = employeeService.createAdmin(employee);
        DishShortDto thisLemonade = dishService.create(lemonadeShortDto);
        DishShortDto thisCoffee = dishService.create(coffeeShortDto);
        DishForOrderDto lemonadeForOrderDto = new DishForOrderDto(thisLemonade.getDishId(), "Лимонад",
                1, 400.0, new CommentShortDto(1L, "Без льда"), Type.BAR);
        DishForOrderDto coffeeForOrderDto = new DishForOrderDto(thisCoffee.getDishId(), "Капучино",
                1, 400.0, new CommentShortDto(3L, "С корицей"), Type.BAR);
        OrderShortDto orderShortDto = new OrderShortDto(1L, 3, 1,
                new ArrayList<>(List.of(coffeeForOrderDto, lemonadeForOrderDto)),
                LocalDateTime.now());
        OrderDto thisOrder = orderService.create(thisEmployee.getEmployeeId(), orderShortDto,
                LocalDateTime.now().minusMinutes(5));
        String receipt = printer.printBill(thisEmployee.getEmployeeId(), thisOrder.getOrderId());

        assertFalse(receipt.isEmpty());
        assertTrue(receipt.startsWith("Заказ открыт"));
        assertTrue(receipt.endsWith("Итого: 800.0"));
    }
}
