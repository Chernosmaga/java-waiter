package com.waiter.javawaiter.order;

import com.waiter.javawaiter.order.dto.OrderShortDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final RestTemplate restTemplate;
    private static final String URL = "http://localhost:9090/order";
    private static final String HEADER = "Employee-Id";
    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public Object create(@RequestHeader(HEADER) String employeeId,
                           @Valid @RequestBody OrderShortDto order) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.set(HEADER, employeeId);
        HttpEntity<Object> requestEntity = new HttpEntity<>(order, headers);
        return restTemplate.postForEntity(URL, requestEntity, Object.class);
    }

    @PutMapping("/{orderId}")
    public void update(@RequestHeader(HEADER) String employeeId,
                           @PathVariable String orderId,
                           @Valid @RequestBody OrderShortDto order) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.set(HEADER, employeeId);
        HttpEntity<Object> requestEntity = new HttpEntity<>(order, headers);
        restTemplate.postForEntity(URL + orderId, requestEntity, Object.class);
    }

    @DeleteMapping("/{orderId}")
    public void deleteById(@RequestHeader(HEADER) String employeeId,
                           @PathVariable String orderId) {
        headers.set(HEADER, employeeId);
        restTemplate.exchange(URL + "/" + orderId, DELETE, null, Void.class, headers);
    }

    @GetMapping("/{orderId}")
    public Object getById(@RequestHeader(HEADER) String employeeId,
                            @PathVariable String orderId) {
        headers.set(HEADER, employeeId);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL + "/" + orderId, GET, null,
                Object.class, headers);
        return responseEntity.getBody();
    }

    @GetMapping
    public List<Object> getOrders(@RequestHeader(HEADER) String employeeId,
                                    @RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(required = false, defaultValue = "10") int limit) {
        headers.set(HEADER, employeeId);
        String url = URL + "/?page=" + offset + "&size=" + limit;
        ResponseEntity<List<Object>> responseEntity = restTemplate.exchange(url, GET, null,
                new ParameterizedTypeReference<List<Object>>() {}, headers);
        return responseEntity.getBody();
    }
}
