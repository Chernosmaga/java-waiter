package com.waiter.javawaiter.printer;

import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.employee.model.Employee;
import com.waiter.javawaiter.employee.repository.EmployeeRepository;
import com.waiter.javawaiter.exception.NotFoundException;
import com.waiter.javawaiter.order.model.Order;
import com.waiter.javawaiter.order.repository.OrderRepository;
import com.waiter.javawaiter.tip.model.Tip;
import com.waiter.javawaiter.tip.repository.TipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Printer {
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final TipRepository tipRepository;

    public String printBill(Long employeeId, Long orderId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Заказ не найден"));
        order.setBillTime(LocalDateTime.now());
        List<String> dishes = new ArrayList<>();
        Double total = 0.0;
        for (Dish dish : order.getDishes()) {
            total += dish.getPrice();
            dishes.add(dish.getTitle() + " " + dish.getPrice());
        }
        order.setTotal(total);
        orderRepository.save(order);
        String print = "";
        if (employee.getTip() != null) {
            Optional<Tip> tip = tipRepository.findById(employee.getTip().getTipId());
            if (tip.isPresent()) {
                print = "Заказ открыт " + order.getCreationTime() + "\nЗаказ закрыт " + order.getBillTime() +
                        "\nВас обслуживал: " + employee.getFirstName() + " " + employee.getSurname() +
                        "\nБлюда в заказе: " + dishes + "\nИтого: " + total +
                        "\nВы можете поблагодарить своего официанта, " +
                        "отсканировав QR-код ниже\n" + tip.get().getQrCode();
            }
        } else {
            print = "Заказ открыт " + order.getCreationTime() + "\nЗаказ закрыт " + order.getBillTime() +
                    "\nВас обслуживал: " + employee.getFirstName() + " " + employee.getSurname() +
                    "\nБлюда в заказе: " + dishes + "\nИтого: " + total;
        }
        return print;
    }
}
