package com.waiter.javawaiter.storage.dish;

import com.waiter.javawaiter.model.Dish;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface DishDao {
    /**
     * Создание и добавление сущности dish в базу данных
     *
     * @param dish блюдо, которое передаётся методу
     * @return dish возвращается объект из базы данных, которому присвоен идентификатор
     * и все поля прошли валидацию
     */
    Dish createDish(Dish dish);

    /**
     * Получение сущности dish по идентификатору
     *
     * @param dishId идентификатор сущности, который передаётся методу
     * @exception EmptyResultDataAccessException, может возникнуть, если запрос на получение данных о сущности
     * в базе данных вернётся пустой
     * @return возвращается данные о сущности dish, если данные не найдены, вернётся null
     */
    Dish getDishById(Integer dishId);

    /**
     * Удаление сущности dish по идентификатору в базе данных
     *
     * @param dishId идентификатор блюда, передаваемый методу
     * @exception EmptyResultDataAccessException, может возникнуть, если запрос на получение данных о сущности
     * в базе данных вернётся пустой
     */
    void deleteDishById(Integer dishId);

    /**
     * Обновление сущности dish в базе данных, если сущности не существует, она создастся
     *
     * @param dish блюдо, которое передаётся методу
     * @return dish возвращается объект из базы данных, которому присвоен идентификатор
     * и все поля прошли валидацию
     */
    Dish updateDish(Dish dish);

    /**
     * Удаление всех ранее добавленных данных в таблице сущности dish
     */
    void deleteDishes();

    /**
     * Получение списка всех сущностей dish
     * @return возвращается список сущностей dish, если данные не найдены, вернётся пустой список
     */
    List<Dish> getDishList();

    /**
     * Обновление статуса готовности блюда исходя из поля time_limit
     * @param dishId идентификатор блюда
     * @param statusId идентификатор статуса
     */
    void updateStatus(Integer dishId, Integer statusId);

    /**
     * Добавления комментария к сущности dish
     * @param orderId идентификатор заказа, в котором находится сущность dish
     * @param dishId идентификатор сущности блюда
     * @param comment добавляемый комментарий
     */
    void addComments(Integer orderId, Integer dishId, String comment);

    /**
     * Проверка на существование сущности в базе данных
     *
     * @param dishId идентификатор блюда, которое передаётся методу
     * @return boolean метод возвращает true или false в зависимости от найденных данных
     * @exception EmptyResultDataAccessException может возникнуть, если данные о блюде не найдены в базе данных
     */
    boolean isExists(Integer dishId);
}