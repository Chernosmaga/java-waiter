package com.waiter.javawaiter.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Dish {
    private Integer dishId;
    @NotBlank(message = "Название блюда не может быть пустым")
    private String title;
    @NotNull(message = "Статус наличия не может быть пустым")
    private Boolean isAvailable;
    @PositiveOrZero(message = "Количество блюда не должно быть отрицательным")
    private Integer quantity;
    @PositiveOrZero(message = "Ограничение по времени не может быть отрицательным")
    @NotNull(message = "Ограничение по времени не может быть пустым")
    private Long timeLimit;
    @PositiveOrZero(message = "Цена блюда не должна быть отрицательной")
    @NotNull(message = "Цена блюда не может быть пустой")
    private Double price;
}