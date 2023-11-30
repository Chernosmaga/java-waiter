package com.waiter.javawaiter.dish.controller;

import com.waiter.javawaiter.dish.dto.DishShortDto;
import com.waiter.javawaiter.dish.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;

    @PostMapping
    public DishShortDto create(@Valid @RequestBody DishShortDto dish) {
        return dishService.create(dish);
    }

    @PutMapping
    public DishShortDto update(@Valid @RequestBody DishShortDto dish) {
        return dishService.update(dish);
    }

    @GetMapping
    public List<DishShortDto> getDishes() {
        return dishService.getDishes();
    }

    @GetMapping("/{dishId}")
    public DishShortDto getById(@PathVariable Long dishId) {
        return dishService.getById(dishId);
    }

    @DeleteMapping("/{dishId}")
    public void deleteById(@PathVariable Long dishId) {
        dishService.deleteById(dishId);
    }

    @DeleteMapping
    public void deleteDishes() {
        dishService.deleteDishes();
    }

    @PostMapping("/{orderId}")
    public void addComments() {
//        TODO
    }
}
