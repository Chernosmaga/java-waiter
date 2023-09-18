package com.waiter.javawaiter.service;

import com.waiter.javawaiter.exception.NotFoundException;
import com.waiter.javawaiter.model.Dish;
import com.waiter.javawaiter.storage.dish.DishDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class DishService {
    private final DishDao dishDao;

    /**
     * Создание и добавление сущности dish в базу данных
     * @param dish блюдо, которое передаётся методу
     * @return dish возвращается объект из базы данных, которому присвоен идентификатор
     * и все поля прошли валидацию
     */
    public Dish createDish(Dish dish) {
        log.debug("createDish({})", dish);
        Dish newDish = new Dish();
        if (dish.getDishId() == null || !dishDao.isExists(dish.getDishId())) {
            newDish = dishDao.createDish(dish);
            log.info("Добавлено новое блюдо {}", newDish);
        }
        return newDish;
    }

    /**
     * Обновление сущности dish в базе данных
     * @param dish блюдо, которое передаётся методу
     * @return dish возвращается объект из базы данных, которому присвоен идентификатор
     * и все поля прошли валидацию
     */
    public Dish updateDish(Dish dish) {
        log.debug("updateDish({})", dish);
        if (!dishDao.isExists(dish.getDishId())) {
            throw new NotFoundException(format("Блюда %s нет в системе", dish));
        }
        Dish newDish = validate(dish);
        log.info("Обновлено блюдо {}", newDish);
        return dishDao.updateDish(newDish);
    }

    /**
     * Удаление сущности dish по идентификатору в базе данных
     * @param dishId идентификатор блюда, передаваемый методу
     * @exception EmptyResultDataAccessException может возникнуть, если запрос на удаление данных о сущности
     * в базе данных вернётся пустой
     */
    public void deleteDishById(Integer dishId) {
        log.debug("deleteDishById({})", dishId);
        try {
            Dish dish = dishDao.getDishById(dishId);
            dishDao.deleteDishById(dish.getDishId());
            log.info("Блюдо с идентификатором {} было удалено", dishId);
        } catch (EmptyResultDataAccessException exception) {
            log.info("Блюдо с идентификатором {} не найдено", dishId);
        }
    }

    /**
     * Удаление всех ранее добавленных данных в таблице сущности dish
     */
    public void deleteDishes() {
        log.debug("deleteDishes()");
        dishDao.deleteDishes();
        log.info("Вся информация о блюдах удалена");
    }

    /**
     * Получение сущности dish по идентификатору
     * @param dishId идентификатор сущности, который передаётся методу
     * @exception EmptyResultDataAccessException может возникнуть, если запрос на получение данных о сущности
     * в базе данных вернётся пустой
     * @return возвращается данные о сущности dish
     */
    public Dish getDishById(Integer dishId) {
        log.debug("getDishById({})", dishId);
        if (!dishDao.isExists(dishId)) {
            throw new NotFoundException("Информация по блюду с идентификатором " + dishId + " не найдена");
        }
        Dish dish = dishDao.getDishById(dishId);
        log.info("Возвращено блюдо с идентификатором {}", dishId);
        return dish;
    }

    /**
     * Получение списка всех сущностей dish
     * @return возвращается список сущностей dish, если данные не найдены, вернётся пустой список
     */
    public List<Dish> getDishList() {
        log.debug("getDishList()");
        List<Dish> dishes = dishDao.getDishList();
        log.info("Возвращён список всех блюд: {}", dishes);
        return dishes;
    }

    /**
     * Добавления комментария к сущности dish
     * @param dishId идентификатор сущности блюда
     * @param comment добавляемый комментарий
     */
    public void addComments(Integer orderId, Integer dishId, String comment) {
        log.debug("addComments({}, {})", dishId, comment);
        dishDao.addComments(orderId, dishId, comment);
        log.info("Добавлен комментарий {} к блюду {} у заказа {}", comment, dishId, orderId);
    }

    /**
     * Обновление статуса готовности блюда исходя из поля time_limit
     * @param dishId идентификатор блюда
     * @param statusId идентификатор статуса
     */
    public void updateStatus(Integer dishId, Integer statusId) {
//        TODO
    }

    private Dish validate(Dish dish) {
        Dish newDish = new Dish();
        newDish.setDishId(dish.getDishId());
        if (dish.getTitle() != null) {
            newDish.setTitle(dish.getTitle());
        }
        if (dish.getIsAvailable() != null) {
            newDish.setIsAvailable(dish.getIsAvailable());
        }
        if (dish.getIsAvailable() != null) {
            newDish.setIsAvailable(dish.getIsAvailable());
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
