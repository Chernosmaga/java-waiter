package com.waiter.javawaiter.controller;

import com.waiter.javawaiter.model.Dish;
import com.waiter.javawaiter.service.DishService;
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
    public Dish createDish(@Valid @RequestBody Dish dish) {
        return dishService.createDish(dish);
    }

    @PutMapping
    public Dish updateDish(@Valid @RequestBody Dish dish) {
        return dishService.updateDish(dish);
    }

    @GetMapping
    public List<Dish> getDishes() {
        return dishService.getDishList();
    }

    @GetMapping("/{dishId}")
    public Dish getDishById(@PathVariable Integer dishId) {
        return dishService.getDishById(dishId);
    }

    @DeleteMapping("/{dishId}")
    public void deleteDishById(@PathVariable Integer dishId) {
        dishService.deleteDishById(dishId);
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
