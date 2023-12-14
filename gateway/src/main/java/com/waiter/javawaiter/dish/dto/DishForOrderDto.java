package com.waiter.javawaiter.dish.dto;

import com.waiter.javawaiter.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishForOrderDto {
    private Long dishId;
    private String title;
    private Integer quantityForOrder;
    private Double price;
    private CommentShortDto comment;
    private Type type;
}