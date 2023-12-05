package com.waiter.javawaiter.dish.mapper;

import com.waiter.javawaiter.comment.dto.CommentShortDto;
import com.waiter.javawaiter.comment.model.Comment;
import com.waiter.javawaiter.comment.repository.CommentRepository;
import com.waiter.javawaiter.dish.dto.DishForOrderDto;
import com.waiter.javawaiter.dish.dto.DishShortDto;
import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.dish.repository.DishRepository;
import com.waiter.javawaiter.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DishMapper {
    private final CommentRepository repository;
    private final DishRepository dishRepository;

    public Dish toDish(DishShortDto dish) {
        log.info("Из DishShortDto в Dish: {}", dish);
        return new Dish(
                dish.getDishId(),
                dish.getTitle(),
                dish.getQuantity(),
                dish.getTimeLimit(),
                dish.getPrice(),
                dish.getType());
    }

    public DishShortDto toDishShortDto(Dish dish) {
        log.info("Из Dish в DishShortDto: {}", dish);
        return new DishShortDto(
                dish.getDishId(),
                dish.getTitle(),
                dish.getQuantity(),
                dish.getTimeLimit(),
                dish.getPrice(),
                dish.getType());
    }

    public DishForOrderDto toDishForOrderDto(Dish dish) {
        log.info("Из Dish в DishForOrderDto: {}", dish);
        Comment comment = repository.findByDishId(dish.getDishId());
        CommentShortDto thisComment =
                comment != null ? new CommentShortDto(comment.getCommentId(), comment.getComment()) : null;
        return new DishForOrderDto(
                dish.getDishId(),
                dish.getTitle(),
                dish.getQuantityForTable(),
                dish.getPrice(),
                thisComment,
                dish.getType());
    }

    public Dish toDish(DishForOrderDto dish) {
        log.info("Из DishForOrderDto в Dish: {}", dish);
        return dishRepository.findById(dish.getDishId()).orElseThrow(() -> new NotFoundException("Блюдо не найдено"));
    }
}
