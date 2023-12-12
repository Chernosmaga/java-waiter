package com.waiter.javawaiter.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waiter.javawaiter.comment.dto.CommentShortDto;
import com.waiter.javawaiter.dish.dto.DishForOrderDto;
import com.waiter.javawaiter.employee.dto.EmployeeShortDto;
import com.waiter.javawaiter.enums.Type;
import com.waiter.javawaiter.order.controller.OrderController;
import com.waiter.javawaiter.order.dto.OrderDto;
import com.waiter.javawaiter.order.dto.OrderShortDto;
import com.waiter.javawaiter.order.service.OrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {
    @MockBean
    private OrderService orderService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private final DishForOrderDto dish = new DishForOrderDto(1L, "Свекольник", 1, 300.0,
            new CommentShortDto(1L, "Без сметаны"), Type.KITCHEN);
    private final EmployeeShortDto employeeDto = new EmployeeShortDto(2L, "89601234567",
            "Maria", "Makarova");
    private final OrderShortDto orderShortDto = new OrderShortDto(1L, 3, 1,
            new ArrayList<>(List.of(dish)), LocalDateTime.now());
    private final OrderDto orderDto = new OrderDto(1L, 3, 1,
            new ArrayList<>(List.of(dish)), employeeDto, LocalDateTime.now(), null, null);

    @Test
    @SneakyThrows
    void create_shouldCreateOrder() {
        when(orderService.create(any(Long.class), any(), any()))
                .thenReturn(orderDto);

        mvc.perform(post("/order")
                        .content(mapper.writeValueAsString(orderShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Employee-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(orderDto.getOrderId()), Long.class))
                .andExpect(jsonPath("$.guests", is(orderDto.getGuests())))
                .andExpect(jsonPath("$.dishes.[0].dishId", is(dish.getDishId()), Long.class))
                .andExpect(jsonPath("$.employee.employeeId",
                        is(orderDto.getEmployee().getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.employee.phone", is(orderDto.getEmployee().getPhone())))
                .andExpect(jsonPath("$.employee.firstName", is(orderDto.getEmployee().getFirstName())))
                .andExpect(jsonPath("$.employee.surname", is(orderDto.getEmployee().getSurname())))
                .andExpect(jsonPath("$.creationTime",
                        is(orderDto.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.billTime", is(orderDto.getBillTime())))
                .andExpect(jsonPath("$.total", is(orderDto.getTotal())));
    }

    @Test
    @SneakyThrows
    void update_shouldUpdateOrder() {
        OrderShortDto thisOrderShortDto = new OrderShortDto(1L, 5, 1,
                new ArrayList<>(List.of(dish)), LocalDateTime.now());
        OrderDto thisOrderDto = new OrderDto(1L, 5, 2,
                new ArrayList<>(List.of(dish)), employeeDto, LocalDateTime.now(), null, null);

        when(orderService.update(any(Long.class), any(Long.class), any()))
                .thenReturn(thisOrderDto);

        mvc.perform(put("/order/1")
                        .content(mapper.writeValueAsString(thisOrderShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Employee-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(thisOrderDto.getOrderId()), Long.class))
                .andExpect(jsonPath("$.guests", is(thisOrderDto.getGuests())))
                .andExpect(jsonPath("$.dishes.[0].dishId", is(dish.getDishId()), Long.class))
                .andExpect(jsonPath("$.employee.employeeId",
                        is(orderDto.getEmployee().getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.employee.phone", is(orderDto.getEmployee().getPhone())))
                .andExpect(jsonPath("$.employee.firstName", is(orderDto.getEmployee().getFirstName())))
                .andExpect(jsonPath("$.employee.surname", is(orderDto.getEmployee().getSurname())))
                .andExpect(jsonPath("$.creationTime",
                        is(thisOrderDto.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.billTime", is(thisOrderDto.getBillTime())))
                .andExpect(jsonPath("$.total", is(thisOrderDto.getTotal())));
    }

    @Test
    @SneakyThrows
    void deleteById_shouldDeleteById() {
        mvc.perform(delete("/order/1")
                        .header("Employee-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getById_shouldReturnOrder() {
        when(orderService.getById(any(Long.class), any(Long.class)))
                .thenReturn(orderDto);

        mvc.perform(get("/order/1")
                        .content(mapper.writeValueAsString(orderShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Employee-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(orderDto.getOrderId()), Long.class))
                .andExpect(jsonPath("$.guests", is(orderDto.getGuests())))
                .andExpect(jsonPath("$.dishes.[0].dishId", is(dish.getDishId()), Long.class))
                .andExpect(jsonPath("$.employee.employeeId",
                        is(orderDto.getEmployee().getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.employee.phone", is(orderDto.getEmployee().getPhone())))
                .andExpect(jsonPath("$.employee.firstName", is(orderDto.getEmployee().getFirstName())))
                .andExpect(jsonPath("$.employee.surname", is(orderDto.getEmployee().getSurname())))
                .andExpect(jsonPath("$.creationTime",
                        is(orderDto.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.billTime", is(orderDto.getBillTime())))
                .andExpect(jsonPath("$.total", is(orderDto.getTotal())));
    }

    @Test
    @SneakyThrows
    void getOrders_shouldReturnOrders() {
        when(orderService.getOrders(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(orderDto));

        mvc.perform(get("/order")
                        .content(mapper.writeValueAsString(orderShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Employee-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].orderId", is(orderDto.getOrderId()), Long.class))
                .andExpect(jsonPath("$.[0].guests", is(orderDto.getGuests())))
                .andExpect(jsonPath("$.[0].dishes.[0].dishId", is(dish.getDishId()), Long.class))
                .andExpect(jsonPath("$.[0].employee.employeeId",
                        is(orderDto.getEmployee().getEmployeeId()), Long.class))
                .andExpect(jsonPath("$.[0].employee.phone", is(orderDto.getEmployee().getPhone())))
                .andExpect(jsonPath("$.[0].employee.firstName", is(orderDto.getEmployee().getFirstName())))
                .andExpect(jsonPath("$.[0].employee.surname", is(orderDto.getEmployee().getSurname())))
                .andExpect(jsonPath("$.[0].creationTime",
                        is(orderDto.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].billTime", is(orderDto.getBillTime())))
                .andExpect(jsonPath("$.[0].total", is(orderDto.getTotal())));
    }
}
