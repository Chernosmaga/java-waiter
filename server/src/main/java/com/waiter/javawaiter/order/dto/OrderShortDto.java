package com.waiter.javawaiter.order.dto;

import com.waiter.javawaiter.dish.dto.DishForOrderDto;
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
    private Integer tableNumber;
    private Integer guests;
    private List<DishForOrderDto> dishes;
    private LocalDateTime creationTime;
}
