package com.waiter.javawaiter.storage.mapper;

import com.waiter.javawaiter.model.Dish;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DishMapper implements RowMapper<Dish> {
    @Override
    public Dish mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Dish(rs.getInt("dish_id"), rs.getString("title"),
                rs.getBoolean("is_available"), rs.getInt("quantity"),
                rs.getInt("status_id"), rs.getDouble("price"),
                rs.getString("comment"));
    }
}
