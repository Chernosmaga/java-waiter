package com.waiter.javawaiter.shift.model;

import com.waiter.javawaiter.employee.model.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shift")
@AllArgsConstructor
@NoArgsConstructor
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private Long shiftId;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(name = "shift_start")
    private LocalDateTime shiftStart;
    @Column(name = "shift_end")
    private LocalDateTime shiftEnd;
}
