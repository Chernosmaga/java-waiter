package com.waiter.javawaiter.employee.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;
    private String phone;
    @Column(name = "first_name")
    private String firstName;
    private String surname;
    @Column(name = "user_password")
    private String userPassword;
    @Column(nullable = false, name = "is_active")
    private Boolean isActive;
    @Column(nullable = false, name = "is_admin")
    private Boolean isAdmin;

    public Employee(Long employeeId, String phone, String firstName, String surname, String userPassword) {
        this.employeeId = employeeId;
        this.phone = phone;
        this.firstName = firstName;
        this.surname = surname;
        this.userPassword = userPassword;
    }
}
