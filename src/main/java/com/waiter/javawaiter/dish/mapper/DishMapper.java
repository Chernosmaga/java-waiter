package com.waiter.javawaiter.dish.mapper;

import com.waiter.javawaiter.comment.dto.CommentShortDto;
import com.waiter.javawaiter.comment.model.Comment;
import com.waiter.javawaiter.comment.repository.CommentRepository;
import com.waiter.javawaiter.dish.dto.DishForOrderDto;
import com.waiter.javawaiter.dish.dto.DishShortDto;
import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.dish.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DishMapper {
    private final CommentRepository repository;
    private final DishRepository dishRepository;

    public Dish toDish(DishShortDto dish) {
        return new Dish(
                dish.getDishId(),
                dish.getTitle(),
                dish.getQuantity(),
                dish.getTimeLimit(),
                dish.getPrice(),
                dish.getType());
    }

    public DishShortDto toDishShortDto(Dish dish) {
        return new DishShortDto(
                dish.getDishId(),
                dish.getTitle(),
                dish.getQuantity(),
                dish.getTimeLimit(),
                dish.getPrice(),
                dish.getType());
    }

    public DishForOrderDto toDishForOrderDto(Dish dish) {
        Comment comment = repository.findByDishId(dish.getDishId());
        CommentShortDto thisComment =
                comment != null ? new CommentShortDto(comment.getCommentId(), comment.getComment()) : null;
        return new DishForOrderDto(
                dish.getDishId(),
                dish.getTitle(),
                dish.getPrice(),
                thisComment,
                dish.getType());
    }

    public Dish toDish(DishForOrderDto dish) {
        return dishRepository.findByTitleAndPriceAndType(dish.getTitle(), dish.getPrice(), dish.getType());
    }
}
