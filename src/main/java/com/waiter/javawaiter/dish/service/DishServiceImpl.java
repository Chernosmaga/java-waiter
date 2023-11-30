package com.waiter.javawaiter.dish.service;

import com.waiter.javawaiter.dish.dto.DishShortDto;
import com.waiter.javawaiter.dish.mapper.DishMapper;
import com.waiter.javawaiter.dish.model.Dish;
import com.waiter.javawaiter.dish.repository.DishRepository;
import com.waiter.javawaiter.exception.AlreadyExistsException;
import com.waiter.javawaiter.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final DishMapper mapper;

    @Override
    public DishShortDto create(DishShortDto dish) {
        log.debug("createDish({})", dish);
        if (dishRepository.existsByTitleAndType(dish.getTitle(), dish.getType())) {
            throw new AlreadyExistsException("Данные о блюде уже есть в системе");
        }
        Dish thisDish = mapper.toDish(dish);
        return mapper.toDishShortDto(dishRepository.save(thisDish));
    }

    @Override
    public DishShortDto update(DishShortDto dish) {
        log.debug("updateDish({})", dish);
        Dish newDish = validate(mapper.toDish(dish));
        Dish updatedDish = dishRepository.save(newDish);
        log.info("Обновлено блюдо {}", newDish);
        return mapper.toDishShortDto(updatedDish);
    }

    @Override
    public void deleteById(Long dishId) {
        log.debug("deleteDishById({})", dishId);
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new NotFoundException("Блюдо не найдено"));
        dishRepository.deleteById(dish.getDishId());
        log.info("Блюдо с идентификатором {} было удалено", dishId);
    }

    @Override
    public void deleteDishes() {
        log.debug("deleteDishes()");
        dishRepository.deleteAll();
        log.info("Вся информация о блюдах удалена");
    }

    @Override
    public DishShortDto getById(Long dishId) {
        log.debug("getDishById({})", dishId);
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new NotFoundException("Блюдо не найдено"));
        log.info("Возвращено блюдо с идентификатором {}", dishId);
        return mapper.toDishShortDto(dish);
    }

    @Override
    public List<DishShortDto> getDishes(int offset, int limit) {
        log.debug("getDishList()");
        PageRequest page = PageRequest.of(offset, limit);
        List<DishShortDto> dishes =
                dishRepository.findAll(page).stream().map(mapper::toDishShortDto).collect(Collectors.toList());
        log.info("Возвращён список всех блюд: {}", dishes);
        return dishes;
    }

    @Override
    public List<DishShortDto> search(String text, int offset, int limit) {
        log.debug("search({}, {}, {})", text, offset, limit);
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        PageRequest page = PageRequest.of(offset, limit);
        Page<Dish> list = dishRepository.findDishesByTitleContainingIgnoreCase(text, page);
        return list.stream().sorted(Comparator.reverseOrder()).map(mapper::toDishShortDto)
                .collect(Collectors.toList());
    }

    private Dish validate(Dish dish) {
        Dish newDish = dishRepository.findByDishId(dish.getDishId());
        newDish.setDishId(dish.getDishId());
        if (dish.getTitle() != null) {
            newDish.setTitle(dish.getTitle());
        }
        if (dish.getQuantity() != null) {
            newDish.setQuantity(dish.getQuantity());
        }
        if (dish.getTimeLimit() != null) {
            newDish.setTimeLimit(dish.getTimeLimit());
        }
        if (dish.getPrice() != null) {
            newDish.setPrice(dish.getPrice());
        }
        return newDish;
    }
}
