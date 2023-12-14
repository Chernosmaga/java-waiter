package com.waiter.javawaiter.dish;

import com.waiter.javawaiter.dish.dto.DishShortDto;
import com.waiter.javawaiter.dish.mapper.DishMapper;
import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.dish.service.DishService;
import com.waiter.javawaiter.enums.Status;
import com.waiter.javawaiter.enums.Type;
import com.waiter.javawaiter.exception.AlreadyExistsException;
import com.waiter.javawaiter.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DishServiceImplTest {
    private final DishService service;
    private final DishMapper mapper;

    private final DishShortDto dishShortDto =
            new DishShortDto(1L, "Свекольник", 5, 15L, 300.0, Type.KITCHEN);
    private final Dish dish = new Dish(1L, "Свекольник",  5, 1, 15L,
            300.0, Status.CREATED, Type.KITCHEN);

    @AfterEach
    void afterEach() {
        service.deleteDishes();
    }

    @Test
    void create_shouldCreateDish() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);

        assertThat(dish.getTitle(), equalTo(createdDish.getTitle()));
        assertThat(dish.getPrice(), equalTo(createdDish.getPrice()));
    }

    @Test
    void create_shouldNotCreateIfDishExists() {
        service.create(dishShortDto);

        assertThrows(AlreadyExistsException.class,
                () -> service.create(dishShortDto));
    }

    @Test
    void update_shouldUpdateDish() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);
        createdDish.setQuantity(10);
        DishShortDto updatedDish = service.update(mapper.toDishShortDto(createdDish));

        assertThat(updatedDish.getDishId(), equalTo(createdDish.getDishId()));
        assertThat(updatedDish.getQuantity(), equalTo(10));
        assertThat(updatedDish.getTitle(), equalTo(createdDish.getTitle()));
    }

    @Test
    void update_shouldUpdateDishIfTitleIsNull() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);
        createdDish.setQuantity(10);
        createdDish.setTitle(" ");
        DishShortDto updatedDish = service.update(mapper.toDishShortDto(createdDish));

        assertThat(updatedDish.getDishId(), equalTo(createdDish.getDishId()));
        assertThat(updatedDish.getQuantity(), equalTo(10));
        assertThat(updatedDish.getTitle(), equalTo(createdDish.getTitle()));
    }

    @Test
    void update_shouldUpdateDishIfQuantityIsNull() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);
        createdDish.setPrice(400.0);
        createdDish.setQuantity(null);
        DishShortDto updatedDish = service.update(mapper.toDishShortDto(createdDish));

        assertThat(updatedDish.getDishId(), equalTo(createdDish.getDishId()));
        assertThat(updatedDish.getQuantity(), equalTo(5));
        assertThat(updatedDish.getTitle(), equalTo(createdDish.getTitle()));
    }

    @Test
    void update_shouldUpdateDishIfTimeLimitIsNull() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);
        createdDish.setQuantity(10);
        createdDish.setTimeLimit(null);
        DishShortDto updatedDish = service.update(mapper.toDishShortDto(createdDish));

        assertThat(updatedDish.getDishId(), equalTo(createdDish.getDishId()));
        assertThat(updatedDish.getQuantity(), equalTo(10));
        assertThat(updatedDish.getTitle(), equalTo(createdDish.getTitle()));
    }

    @Test
    void update_shouldUpdateDishIfPriceIsNull() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);
        createdDish.setQuantity(10);
        createdDish.setPrice(null);
        DishShortDto updatedDish = service.update(mapper.toDishShortDto(createdDish));

        assertThat(updatedDish.getDishId(), equalTo(createdDish.getDishId()));
        assertThat(updatedDish.getQuantity(), equalTo(10));
        assertThat(updatedDish.getTitle(), equalTo(createdDish.getTitle()));
    }

    @Test
    void update_shouldUpdateDishIfTypeIsNull() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);
        createdDish.setQuantity(10);
        createdDish.setType(null);
        DishShortDto updatedDish = service.update(mapper.toDishShortDto(createdDish));

        assertThat(updatedDish.getDishId(), equalTo(createdDish.getDishId()));
        assertThat(updatedDish.getQuantity(), equalTo(10));
        assertThat(updatedDish.getTitle(), equalTo(createdDish.getTitle()));
    }

    @Test
    void deleteById_shouldDeleteById() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);
        service.deleteById(createdDish.getDishId());

        assertTrue(service.getDishes(1, 10).isEmpty());
    }

    @Test
    void deleteById_shouldThrowExceptionIfIdIsIncorrect() {
        assertThrows(NotFoundException.class, () -> service.deleteById(999L));
    }

    @Test
    void delete_shouldDeleteDishes() {
        DishShortDto savedDish = service.create(dishShortDto);
        mapper.toDish(savedDish);
        service.deleteDishes();

        assertTrue(service.getDishes(0, 10).isEmpty());
    }

    @Test
    void getById_shouldReturnDishById() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);
        DishShortDto returnedDish = service.getById(createdDish.getDishId());

        assertThat(returnedDish, equalTo(mapper.toDishShortDto(createdDish)));
    }

    @Test
    void getById_shouldThrowExceptionIfIdIsIncorrect() {
        assertThrows(NotFoundException.class, () -> service.getById(999L));
    }

    @Test
    void getDishes_shouldReturnListOfDishes() {
        DishShortDto savedDish = service.create(dishShortDto);
        Dish createdDish = mapper.toDish(savedDish);
        List<DishShortDto> dishList = service.getDishes(0, 10);

        assertFalse(dishList.isEmpty());
        assertTrue(dishList.contains(mapper.toDishShortDto(createdDish)));
    }

    @Test
    void getDishes_shouldReturnEmptyListOfDishes() {
        List<DishShortDto> dishList = service.getDishes(0, 10);

        assertTrue(dishList.isEmpty());
    }

    @Test
    void search_shouldFindADish() {
        DishShortDto savedDish = service.create(dishShortDto);
        List<DishShortDto> dishes = service.search("свеко", 0, 10);

        assertFalse(dishes.isEmpty());
        assertTrue(dishes.contains(savedDish));
    }

    @Test
    void search_shouldReturnEmptyListIfDishTextIsNull() {
        List<DishShortDto> dishes = service.search(" ", 0, 10);

        assertTrue(dishes.isEmpty());
    }

    @Test
    void search_shouldReturnEmptyListIfDishDoesntExists() {
        List<DishShortDto> dishes = service.search("Свекольник", 0, 10);

        assertTrue(dishes.isEmpty());
    }
}
