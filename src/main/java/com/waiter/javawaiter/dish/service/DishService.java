package com.waiter.javawaiter.dish.service;

import com.waiter.javawaiter.dish.dto.DishShortDto;

import java.util.List;

public interface DishService {
    DishShortDto create(DishShortDto dish);

    DishShortDto update(DishShortDto dish);

    void deleteById(Long dishId);

    void deleteDishes();

    DishShortDto getById(Long dishId);

    List<DishShortDto> getDishes();

    void addComments(Long orderId, Long dishId, String comment);

    void updateStatus(Long dishId, Integer statusId);
}
