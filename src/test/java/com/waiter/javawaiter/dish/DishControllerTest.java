package com.waiter.javawaiter.dish;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waiter.javawaiter.dish.controller.DishController;
import com.waiter.javawaiter.dish.dto.DishShortDto;
import com.waiter.javawaiter.dish.service.DishService;
import com.waiter.javawaiter.enums.Type;
import jakarta.validation.ValidationException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DishController.class)
public class DishControllerTest {
    @MockBean
    private DishService dishService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private final DishShortDto dishShortDto =
            new DishShortDto(1L, "Свекольник", 5, 15L, 300.0, Type.KITCHEN);
    private final DishShortDto updatedDishShortDto =
            new DishShortDto(1L, "Свекольник", 10, 15L, 300.0, Type.KITCHEN);

    @Test
    @SneakyThrows
    void create_shouldCreateDish() {
        when(dishService.create(any()))
                .thenReturn(dishShortDto);

        mvc.perform(post("/dish")
                        .content(mapper.writeValueAsString(dishShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishId", is(dishShortDto.getDishId()), Long.class))
                .andExpect(jsonPath("$.title", is(dishShortDto.getTitle())))
                .andExpect(jsonPath("$.quantity", is(dishShortDto.getQuantity())))
                .andExpect(jsonPath("$.timeLimit", is(dishShortDto.getTimeLimit()), Long.class))
                .andExpect(jsonPath("$.price", is(dishShortDto.getPrice()), Double.class))
                .andExpect(jsonPath("$.type", is(dishShortDto.getType().toString())));
    }


    @Test
    @SneakyThrows
    void create_shouldThrowValidationExceptionIfTitleIsNull() {
        DishShortDto dishShortDto =
                new DishShortDto(1L, " ", 5, 15L, 300.0, Type.KITCHEN);
        when(dishService.create(any()))
                .thenThrow(new ValidationException());

        MockHttpServletResponse response = mvc.perform(post("/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dishShortDto)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(400)));
    }

    @Test
    @SneakyThrows
    void create_shouldThrowValidationExceptionIfQuantityIsNegative() {
        DishShortDto dishShortDto =
                new DishShortDto(1L, "Свекольник", -1, 15L, 300.0, Type.KITCHEN);
        when(dishService.create(any()))
                .thenThrow(new ValidationException());

        MockHttpServletResponse response = mvc.perform(post("/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dishShortDto)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(400)));
    }

    @Test
    @SneakyThrows
    void create_shouldThrowValidationExceptionIfTimeLimitIsNegative() {
        DishShortDto dishShortDto =
                new DishShortDto(1L, "Свекольник", 5, -1L, 300.0, Type.KITCHEN);
        when(dishService.create(any()))
                .thenThrow(new ValidationException());

        MockHttpServletResponse response = mvc.perform(post("/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dishShortDto)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(400)));
    }

    @Test
    @SneakyThrows
    void create_shouldThrowValidationExceptionIfPriceIsNegative() {
        DishShortDto dishShortDto =
                new DishShortDto(1L, "Свекольник", 5, 15L, -300.0, Type.KITCHEN);
        when(dishService.create(any()))
                .thenThrow(new ValidationException());

        MockHttpServletResponse response = mvc.perform(post("/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dishShortDto)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse();

        assertThat(response.getStatus(), is(equalTo(400)));
    }

    @Test
    @SneakyThrows
    void update_shouldUpdateDish() {
        when(dishService.update(any()))
                .thenReturn(updatedDishShortDto);

        mvc.perform(put("/dish")
                        .content(mapper.writeValueAsString(dishShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishId", is(updatedDishShortDto.getDishId()), Long.class))
                .andExpect(jsonPath("$.title", is(updatedDishShortDto.getTitle())))
                .andExpect(jsonPath("$.quantity", is(updatedDishShortDto.getQuantity())))
                .andExpect(jsonPath("$.timeLimit", is(updatedDishShortDto.getTimeLimit()), Long.class))
                .andExpect(jsonPath("$.price", is(updatedDishShortDto.getPrice()), Double.class))
                .andExpect(jsonPath("$.type", is(updatedDishShortDto.getType().toString())));
    }

    @Test
    @SneakyThrows
    void getById_shouldReturnDishById() {
        when(dishService.getById(any(Long.class)))
                .thenReturn(dishShortDto);

        mvc.perform(get("/dish/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishId", is(dishShortDto.getDishId()), Long.class))
                .andExpect(jsonPath("$.title", is(dishShortDto.getTitle())))
                .andExpect(jsonPath("$.quantity", is(dishShortDto.getQuantity())))
                .andExpect(jsonPath("$.timeLimit", is(dishShortDto.getTimeLimit()), Long.class))
                .andExpect(jsonPath("$.price", is(dishShortDto.getPrice()), Double.class))
                .andExpect(jsonPath("$.type", is(dishShortDto.getType().toString())));
    }

    @Test
    @SneakyThrows
    void getDishes_shouldReturnListOfDishes() {
        when(dishService.getDishes(any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(dishShortDto));

        mvc.perform(get("/dish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].dishId", is(dishShortDto.getDishId()), Long.class))
                .andExpect(jsonPath("$.[0].title", is(dishShortDto.getTitle())))
                .andExpect(jsonPath("$.[0].quantity", is(dishShortDto.getQuantity())))
                .andExpect(jsonPath("$.[0].timeLimit", is(dishShortDto.getTimeLimit()), Long.class))
                .andExpect(jsonPath("$.[0].price", is(dishShortDto.getPrice()), Double.class))
                .andExpect(jsonPath("$.[0].type", is(dishShortDto.getType().toString())));
    }

    @Test
    @SneakyThrows
    void deleteById_shouldDeleteDishById() {
        mvc.perform(delete("/dish/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void deleteDishes_shouldDeleteDishes() {
        mvc.perform(delete("/dish").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
