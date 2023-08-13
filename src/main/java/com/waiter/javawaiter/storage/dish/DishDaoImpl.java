package com.waiter.javawaiter.storage.dish;

import com.waiter.javawaiter.model.Dish;
import com.waiter.javawaiter.storage.mapper.DishMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DishDaoImpl implements DishDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Dish createDish(Dish dish) {
        log.debug("Запрос на добавление блюда: {}", dish);
        String sql = "INSERT INTO dish (title, is_available, quantity, status_id, price, comment) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, dish.getTitle(), dish.isAvailable(), dish.getQuantity(), dish.getStatusId(),
                dish.getPrice(), dish.getComment());
        String dishSql = String.format("SELECT title, is_available, quantity, status_id, price, comment FROM dish " +
                        "WHERE title=%s AND is_available=%b AND quantity=%d AND status_id=%d AND price=%f AND comment=%s",
                dish.getTitle(),  dish.isAvailable(), dish.getQuantity(), dish.getStatusId(),
                dish.getPrice(), dish.getComment());
        return jdbcTemplate.queryForObject(dishSql, new DishMapper());
    }

    @Override
    public Dish getDishById(Integer dishId) {
        log.debug("Запрос на получение блюда с идентификатором: {}", dishId);
        String sql = "SELECT title, is_available, quantity, status_id, price, comment FROM dish " +
                "WHERE dish_id=" + dishId;
        return jdbcTemplate.queryForObject(sql, new DishMapper());
    }

    @Override
    public void deleteDishById(Integer dishId) {
        log.debug("Запрос на удаление блюда с идентификатором: {}", dishId);
        jdbcTemplate.update("DELETE FROM dish WHERE dish_id=" + dishId);
    }

    @Override
    public Dish updateDish(Dish dish) {
        log.debug("Запрос на обновление блюда: {}", dish);
        String sql = "INSERT INTO dish (title, is_available, quantity, status_id, price, comment) " +
                "VALUES(?, ?, ?, ?, ?, ?) WHERE dish_id=" + dish.getDishId();
        jdbcTemplate.update(sql, dish.getTitle(), dish.isAvailable(), dish.getQuantity(), dish.getStatusId(),
                dish.getPrice(), dish.getComment());
        String dishSql = String.format("SELECT title, is_available, quantity, status_id, price, comment FROM dish " +
                        "WHERE title=%s AND is_available=%b AND quantity=%d AND status_id=%d AND price=%f AND comment=%s",
                dish.getTitle(),  dish.isAvailable(), dish.getQuantity(), dish.getStatusId(),
                dish.getPrice(), dish.getComment());
        return jdbcTemplate.queryForObject(dishSql, new DishMapper());
    }

    @Override
    public void deleteDishes() {
        log.debug("Запрос на удаление всех блюд");
        jdbcTemplate.update("DELETE * FROM dish");
    }

    @Override
    public List<Dish> getDishList() {
        log.debug("Запрос на получение списка блюд");
        String sql = "SELECT  title, is_available, quantity, status_id, price, comment FROM dish ";
        return jdbcTemplate.query(sql, new DishMapper());
    }
}
