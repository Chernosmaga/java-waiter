package com.waiter.javawaiter.shift.repository;

import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.shift.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    @Query("select s.shiftId from Shift as s where s.shiftStart < :date and s.employee = :employee")
    Long findByDateAndEmployee(LocalDateTime date, Employee employee);
    List<Shift> findAllByEmployee(Employee employee);
}
