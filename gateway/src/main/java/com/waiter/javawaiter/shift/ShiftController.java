package com.waiter.javawaiter.shift;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/shift")
public class ShiftController {
    private final RestTemplate restTemplate;

    private static final String URL = "http://localhost:9090/shift";
    private final String HEADER = "Employee-Id";
    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    public ShiftController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public Object open(@RequestHeader(HEADER) String employeeId) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.set(HEADER, employeeId);
        HttpEntity<Object> requestEntity = new HttpEntity<>(LocalDateTime.now(), headers);
        restTemplate.postForEntity(URL, requestEntity, Object.class);
        return requestEntity.getBody();
    }

    @PutMapping("/{shiftId}")
    public void close(@RequestHeader(HEADER) String employeeId, @RequestParam String shiftId) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.set(HEADER, employeeId);
        HttpEntity<Object> requestEntity = new HttpEntity<>(LocalDateTime.now(), headers);
        restTemplate.put(URL + "/" + shiftId, requestEntity);
    }

    @GetMapping("/date/{date}")
    public Object getByDate(@RequestHeader(HEADER) String employeeId, @PathVariable LocalDateTime date) {
        headers.set(HEADER, employeeId);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL + "/date/" + date, GET, null,
                Object.class, headers);
        return responseEntity.getBody();
    }

    @GetMapping("/report")
    public List<Object> getByPeriod(@RequestHeader(HEADER) String employeeId,
                                   @RequestParam(name = "start") LocalDateTime start,
                                   @RequestParam(name = "end") LocalDateTime end) {
        String url = URL + "/report/?start=" + start + "?end=" + end;
        ResponseEntity<List<Object>> responseEntity = restTemplate.exchange(url, GET, null,
                new ParameterizedTypeReference<List<Object>>() {});
        return responseEntity.getBody();
    }
}
