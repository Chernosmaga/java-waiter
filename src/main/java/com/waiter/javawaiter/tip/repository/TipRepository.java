package com.waiter.javawaiter.tip.repository;

import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.tip.model.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipRepository extends JpaRepository<Tip, Long> {
    List<Tip> findAllByEmployee(Employee employee);
    void deleteAllByEmployee(Employee employee);
}
