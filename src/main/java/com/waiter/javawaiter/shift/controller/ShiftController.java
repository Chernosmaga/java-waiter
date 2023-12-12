package com.waiter.javawaiter.shift.controller;

import com.waiter.javawaiter.shift.model.Shift;
import com.waiter.javawaiter.shift.service.ShiftService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shift")
@RequiredArgsConstructor
public class ShiftController {
    private final String HEAD = "Employee-Id";
    private final ShiftService service;

    @PostMapping
    public Shift open(@RequestHeader(HEAD) Long employeeId) {
        return service.open(employeeId, LocalDateTime.now());
    }

    @PutMapping("/{shiftId}")
    public Shift close(@RequestHeader(HEAD) Long employeeId, @RequestParam Long shiftId) {
        return service.close(employeeId, shiftId, LocalDateTime.now());
    }

    @GetMapping
    public Shift getByDate(@RequestHeader(HEAD) Long employeeId, @NotBlank @RequestBody LocalDateTime date) {
        return service.getByDate(employeeId, date);
    }

    @GetMapping("/report")
    public List<Shift> getByPeriod(@RequestHeader(HEAD) Long employeeId,
                                   @NotBlank @RequestBody LocalDateTime start,
                                   @NotBlank @RequestBody LocalDateTime end) {
        return service.getForPeriod(employeeId, start, end);
    }
}
