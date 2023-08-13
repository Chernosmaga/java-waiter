package com.waiter.javawaiter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    private Integer dishId;
    private String title;
    private boolean isAvailable;
    private Integer quantity;
    private Integer statusId;
    private Double price;
    private String comment;
}