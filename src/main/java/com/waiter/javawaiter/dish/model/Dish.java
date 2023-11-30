package com.waiter.javawaiter.dish.model;

import com.waiter.javawaiter.enums.Status;
import com.waiter.javawaiter.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "dish", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Comparable<Dish> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "dish_id")
    private Long dishId;
    private String title;
    private Integer quantity;
    @Column(name = "time_limit")
    private Long timeLimit;
    private Double price;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Type type;

    public Dish(Long dishId,
                String title,
                Integer quantity,
                Long timeLimit,
                Double price,
                Type type) {
        this.dishId = dishId;
        this.title = title;
        this.quantity = quantity;
        this.timeLimit = timeLimit;
        this.price = price;
        this.type = type;
    }

    @Override
    public int compareTo(Dish dish) {
        return this.title.compareTo(dish.title);
    }
}