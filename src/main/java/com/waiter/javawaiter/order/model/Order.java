package com.waiter.javawaiter.order.model;

import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.employee.model.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    private Integer guests;
    @ManyToMany
    @JoinTable(name = "dishes",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Dish> dishes;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
    @Column(name = "bill_time")
    private LocalDateTime billTime;
    private Double total;

    public Order(Long orderId, Integer guests, List<Dish> dishes, LocalDateTime creationTime) {
        this.orderId = orderId;
        this.guests = guests;
        this.dishes = dishes;
        this.creationTime = creationTime;
    }
}
