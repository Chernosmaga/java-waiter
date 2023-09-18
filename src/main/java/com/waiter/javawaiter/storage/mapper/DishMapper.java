package com.waiter.javawaiter.storage.mapper;

import com.waiter.javawaiter.model.Dish;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DishMapper implements RowMapper<Dish> {
    @Override
    public Dish mapRow(ResultSet rs, int rowNum) throws SQLException {
        Dish dish = new Dish();
        dish.setDishId(rs.getInt("dish_id"));
        dish.setTitle(rs.getString("title"));
        dish.setIsAvailable(rs.getBoolean("is_available"));
        dish.setQuantity(rs.getInt("quantity"));
        dish.setTimeLimit(rs.getLong("time_limit"));
        dish.setPrice(rs.getDouble("price"));
        return dish;
    }
}
