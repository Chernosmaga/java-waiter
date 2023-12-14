package com.waiter.javawaiter.employee;

import com.waiter.javawaiter.employee.dto.EmployeeDto;
import com.waiter.javawaiter.employee.dto.Tip;
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
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final RestTemplate restTemplate;
    private static final String URL = "http://localhost:9090/employee";
    private static final String HEADER = "Employee-Id";
    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    public EmployeeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public Object createAdmin(@RequestBody EmployeeDto employee) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<>(employee, headers);
        return restTemplate.postForEntity(URL, requestEntity, Object.class);
    }

    @PostMapping("/{adminId}")
    public Object create(@PathVariable String adminId, @RequestBody EmployeeDto employee) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<>(employee, headers);
        return restTemplate.postForEntity(URL + adminId, requestEntity, Object.class);
    }

    @PutMapping("/{employeeId}")
    public void update(@RequestHeader(HEADER) String adminId,
                                   @PathVariable String employeeId,
                                   @RequestBody EmployeeDto employee) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.set(HEADER, adminId);
        HttpEntity<Object> requestEntity = new HttpEntity<>(employee, headers);
        restTemplate.put(URL + "/" + employeeId, requestEntity);
    }

    @GetMapping("/{phone}")
    public Object get(@PathVariable String phone)  {
        return restTemplate.getForObject(URL + "/" + phone, Object.class);
    }

    @GetMapping("/admin/{employeeId}")
    public Object getByAdmin(@RequestHeader(HEADER) String adminId,
                                          @PathVariable String employeeId) {
        headers.set(HEADER, adminId);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL + "/admin/" + employeeId, GET,
                null, Object.class, headers);
        return responseEntity.getBody();
    }

    @PutMapping("/update/{employeeId}")
    public void updateIsActive(@RequestHeader(HEADER) String adminId,
                               @PathVariable String employeeId,
                               @RequestBody Boolean isActive) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.set(HEADER, adminId);
        HttpEntity<Object> requestEntity = new HttpEntity<>(isActive, headers);
        restTemplate.put(URL + "/update/" + employeeId, requestEntity);
    }

    @GetMapping
    public ResponseEntity<Object> getEmployees(@RequestHeader(HEADER) String adminId) {
        ResponseEntity<List<Object>> responseEntity = restTemplate.exchange(URL, GET, null,
                new ParameterizedTypeReference<>() {});
        headers.set(HEADER, adminId);
        return new ResponseEntity<>(responseEntity.getBody(), headers, OK);
    }

    @PostMapping("/tip")
    public Object addTip(@RequestHeader(HEADER) String employeeId,
                      @RequestBody Tip tip) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.set(HEADER, employeeId);
        HttpEntity<Object> requestEntity = new HttpEntity<>(tip, headers);
        return restTemplate.postForEntity(URL + "/tip", requestEntity, Object.class);
    }

    @GetMapping("/tip")
    public Object getTip(@RequestHeader(HEADER) String employeeId) {
        headers.set(HEADER, employeeId);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL + "/tip", GET, null,
                Object.class, headers);
        return responseEntity.getBody();
    }

    @DeleteMapping("/tip")
    public void deleteTip(@RequestHeader(HEADER) String employeeId) {
        headers.set(HEADER, employeeId);
        restTemplate.exchange(URL + "/tip", DELETE, null, Void.class, headers);
    }
}
