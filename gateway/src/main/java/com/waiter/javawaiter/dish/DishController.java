package com.waiter.javawaiter.dish;

import com.waiter.javawaiter.dish.dto.DishShortDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/dish")
public class DishController {
    private final RestTemplate restTemplate;
    private static final String URL = "http://localhost:9090/dish";

    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    public DishController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public Object create(@Valid @RequestBody DishShortDto dish) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<>(dish, headers);
        return restTemplate.postForEntity(URL, requestEntity, Object.class);
    }

    @PutMapping
    public void update(@Valid @RequestBody DishShortDto dish) {
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));
        HttpEntity<Object> requestEntity = new HttpEntity<>(dish, headers);
        restTemplate.put(URL, requestEntity);
    }

    @GetMapping
    public List<Object> getDishes(@RequestParam(defaultValue = "0") int offset,
                                        @RequestParam(required = false, defaultValue = "10") int limit) {
        String url = URL + "?page=" + offset + "&size=" + limit;
        ResponseEntity<List<Object>> responseEntity = restTemplate.exchange(url, GET, null,
                new ParameterizedTypeReference<List<Object>>() {});
        return responseEntity.getBody();
    }

    @GetMapping("/{dishId}")
    public Object getById(@PathVariable String dishId) {
        return restTemplate.getForObject(URL + dishId, Object.class);
    }

    @DeleteMapping("/{dishId}")
    public void deleteById(@PathVariable String dishId) {
        restTemplate.delete(URL + dishId);
    }

    @DeleteMapping
    public void deleteDishes() {
        restTemplate.delete(URL);
    }

    @GetMapping("/search")
    public List<Object> search(@RequestParam("text") String text,
                                     @RequestParam(name = "offset", defaultValue = "0") int offset,
                                     @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        String url = URL + "/search/?text=" + text + "?page=" + offset + "&size=" + limit;
        ResponseEntity<List<Object>> responseEntity = restTemplate.exchange(url, GET, null,
                new ParameterizedTypeReference<List<Object>>() {});
        return responseEntity.getBody();
    }
}
