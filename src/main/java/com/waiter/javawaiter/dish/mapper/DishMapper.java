package com.waiter.javawaiter.dish.mapper;

import com.waiter.javawaiter.dish.dto.DishShortDto;
import com.waiter.javawaiter.dish.model.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishMapper {

    public Dish toDish(DishShortDto dish) {
        return new Dish(
                dish.getDishId(),
                dish.getTitle(),
                dish.getIsAvailable(),
                dish.getQuantity(),
                dish.getTimeLimit(),
                dish.getPrice(),
                dish.getType());
    }

    public DishShortDto toDishShortDto(Dish dish) {
        return new DishShortDto(
                dish.getDishId(),
                dish.getTitle(),
                dish.getIsAvailable(),
                dish.getQuantity(),
                dish.getTimeLimit(),
                dish.getPrice(),
                dish.getType());
    }
}
