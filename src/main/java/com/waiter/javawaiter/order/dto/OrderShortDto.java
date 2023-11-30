package com.waiter.javawaiter.order.dto;

import com.waiter.javawaiter.dish.dto.DishForOrderDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderShortDto {
    private Long orderId;
    @NotNull(message = "Для создания заказа за столом должен быть хотя бы один гость")
    private Integer guests;
    private List<DishForOrderDto> dishes;
    private LocalDateTime creationTime;
}
