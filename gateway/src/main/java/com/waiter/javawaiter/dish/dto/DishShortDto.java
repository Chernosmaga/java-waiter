package com.waiter.javawaiter.dish.dto;

import com.waiter.javawaiter.enums.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishShortDto {
    private Long dishId;
    @NotBlank(message = "Название блюда не может быть пустым")
    private String title;
    @PositiveOrZero(message = "Количество блюда не должно быть отрицательным")
    private Integer quantity;
    @PositiveOrZero(message = "Ограничение по времени не может быть отрицательным")
    @NotNull(message = "Ограничение по времени не может быть пустым")
    private Long timeLimit;
    @PositiveOrZero(message = "Цена блюда не должна быть отрицательной")
    @NotNull(message = "Цена блюда не может быть пустой")
    private Double price;
    private Type type;
}