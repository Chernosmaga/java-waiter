package com.waiter.javawaiter.order.repository;

import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByEmployee(Employee employee, Pageable page);
    void deleteAllByTotalIsNullAndBillTimeIsNull();
}
