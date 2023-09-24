package com.waiter.javawaiter.storage.dish;

import com.waiter.javawaiter.model.Dish;
import com.waiter.javawaiter.storage.mapper.DishMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DishDaoImpl implements DishDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Dish createDish(Dish dish) {
        log.debug("Запрос на добавление блюда: {}", dish);
        String sql = "INSERT INTO dish (title, is_available, quantity, time_limit, price) " +
                "VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, dish.getTitle(), dish.getIsAvailable(), dish.getQuantity(), dish.getTimeLimit(),
                dish.getPrice());
        return jdbcTemplate.queryForObject("SELECT dish_id, title, is_available, quantity, time_limit, price " +
                        "FROM dish WHERE title=? AND is_available=? AND quantity=? AND time_limit=? AND price=?",
                new DishMapper(), dish.getTitle(),  dish.getIsAvailable(),  dish.getQuantity(), dish.getTimeLimit(),
                dish.getPrice());
    }

    @Override
    public Dish getDishById(Integer dishId) {
        log.debug("Запрос на получение блюда с идентификатором: {}", dishId);
        return jdbcTemplate.queryForObject("SELECT dish_id, title, is_available, quantity, time_limit, price " +
                "FROM dish WHERE dish_id=?", new DishMapper(), dishId);
    }

    @Override
    public void deleteDishById(Integer dishId) {
        log.debug("Запрос на удаление блюда с идентификатором: {}", dishId);
        jdbcTemplate.update("DELETE FROM dish WHERE dish_id=?", dishId);
    }

    @Override
    public Dish updateDish(Dish dish) {
        log.debug("Запрос на обновление блюда: {}", dish);
        String sql = "UPDATE dish SET title=?, is_available=?, quantity=?, time_limit=?, price=? " +
                "WHERE dish_id=?";
        jdbcTemplate.update(sql, dish.getTitle(), dish.getIsAvailable(), dish.getQuantity(), dish.getTimeLimit(),
                dish.getPrice(), dish.getDishId());
        return jdbcTemplate.queryForObject("SELECT dish_id, title, is_available, quantity, time_limit, price " +
                        "FROM dish WHERE title=? AND is_available=? AND quantity=? AND time_limit=? AND price=?",
                new DishMapper(), dish.getTitle(), dish.getIsAvailable(), dish.getQuantity(), dish.getTimeLimit(),
                dish.getPrice());
    }

    @Override
    public void deleteDishes() {
        log.debug("Запрос на удаление всех блюд");
        jdbcTemplate.update("DELETE FROM dish");
    }

    @Override
    public List<Dish> getDishList() {
        log.debug("Запрос на получение списка блюд");
        String sql = "SELECT dish_id, title, is_available, quantity, time_limit, price FROM dish";
        return jdbcTemplate.query(sql, new DishMapper());
    }

    @Override
    public void updateStatus(Integer dishId, Integer statusId) {
        log.debug("Запрос на обновление статуса {} блюда {}", statusId, dishId);
//        TODO
    }

    @Override
    public void addComments(Integer orderId, Integer dishId, String comment) {
        log.debug("Запрос на добавление комментария {} блюду {} у заказа {}", comment, dishId, orderId);
        String sql = "INSERT INTO comments (order_id, dish_id, comments) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, orderId, dishId, comment);
    }

    @Override
    public boolean isExists(Integer dishId) {
        log.debug("isExists({})", dishId);
        try {
            getDishById(dishId);
            log.info("Блюдо найдено в базе данных: {}", dishId);
            return true;
        } catch (EmptyResultDataAccessException exception) {
            log.info("Нет информации о блюде в базе данных: {}", dishId);
            return false;
        }
    }
}
