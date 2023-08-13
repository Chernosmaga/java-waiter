package com.waiter.javawaiter.storage.dish;

import com.waiter.javawaiter.model.Dish;

import java.util.List;

public interface DishDao {

    Dish createDish(Dish dish);

    Dish getDishById(Integer dishId);

    void deleteDishById(Integer dishId);

    Dish updateDish(Dish dish);

    void deleteDishes();

    List<Dish> getDishList();
}