package com.waiter.javawaiter.employee.repository;

import com.waiter.javawaiter.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByPhoneAndFirstNameAndSurname(String phone, String firstName, String surname);
    List<Employee> findEmployeesByIsActive(Boolean isActive);
    Employee findEmployeeByPhoneAndFirstNameAndSurname(String phone, String firstName, String surname);
    Employee findEmployeeByPhone(String phone);
}
