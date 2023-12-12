package com.waiter.javawaiter.shift.service;

import com.waiter.javawaiter.shift.model.Shift;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftService {
    Shift open(Long employeeId, LocalDateTime start);
    Shift close(Long employeeId, Long shiftId, LocalDateTime end);
    Shift getByDate(Long employeeId, LocalDateTime date);
    List<Shift> getForPeriod(Long employeeId, LocalDateTime start, LocalDateTime end);
}
