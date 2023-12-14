package com.waiter.javawaiter.shift.service;

import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.employee.repository.EmployeeRepository;
import com.waiter.javawaiter.exception.AccessViolationException;
import com.waiter.javawaiter.exception.NotFoundException;
import com.waiter.javawaiter.shift.model.Shift;
import com.waiter.javawaiter.shift.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Shift open(Long employeeId, LocalDateTime start) {
        log.debug("open({}, {})", employeeId, start);
        Employee employee = getEmployee(employeeId);
        Shift shift = new Shift();
        shift.setEmployee(employee);
        shift.setShiftStart(start);
        Shift saved = shiftRepository.save(shift);
        log.info("Смена пользователя: {}, открыта: {}", employee, saved);
        return saved;
    }

    @Override
    public Shift close(Long employeeId, Long shiftId, LocalDateTime end) {
        log.debug("close({}, {})", employeeId, end);
        Employee employee = getEmployee(employeeId);
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Смена не открыта"));
        if (shift.getEmployee() != employee || !employee.getIsAdmin()) {
            throw new AccessViolationException("Смену может закрыть владелец, либо администратор");
        }
        shift.setShiftEnd(end);
        Shift saved = shiftRepository.save(shift);
        log.info("Смена пользователя: {}, закрыта: {}", employee, saved);
        return saved;
    }

    @Override
    public Shift getByDate(Long employeeId, LocalDateTime date) {
        log.debug("getByDate({}, {})", employeeId, date);
        Employee employee = getEmployee(employeeId);
        Long shiftId = shiftRepository.findByDateAndEmployee(date, employee);
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Некорректно указана дата"));
        log.info("Возвращена смена: {}, по запросу от пользователя: {}", shift, employee);
        return shift;
    }

    @Override
    public List<Shift> getForPeriod(Long employeeId, LocalDateTime start, LocalDateTime end) {
        log.debug("getForPeriod({}, {}, {})", employeeId, start, end);
        Employee employee = getEmployee(employeeId);
        List<Shift> shifts = shiftRepository.findAllByEmployee(employee).stream()
                .filter(shift -> shift.getShiftStart().isAfter(start) && shift.getShiftEnd().isAfter(end) ||
                                shift.getShiftStart().isAfter(start) && shift.getShiftEnd().isBefore(end)
                        ).toList();
        log.info("Пользователь {} запросил смены за период с {} по {}", employee, start, end);
        return shifts;
    }

    private Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Сотрудник не найден"));
    }
}
