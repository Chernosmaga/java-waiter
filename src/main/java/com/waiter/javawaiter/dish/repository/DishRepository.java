package com.waiter.javawaiter.dish.repository;

import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    Dish findByDishId(Long dishId);

    boolean existsByTitleAndType(String title, Type type);

    Page<Dish> findDishesByTitleContainingIgnoreCase(String title, Pageable page);
}