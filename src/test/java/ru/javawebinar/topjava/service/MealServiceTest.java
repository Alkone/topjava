package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({"classpath:spring/spring-app.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql",
        config = @SqlConfig(encoding = "UTF8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        assertMatch(service.get(USER_MEAL_MRN_EARLY_ID, USER_ID), USER_MEAL_MRN_EARLY);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(USER_MEAL_MRN_EARLY_ID, ADMIN_ID);
    }

    @Test
    public void delete() {
        List<Meal> userMeals = new ArrayList<>(Arrays.asList(SORTED_USER_MEALS));
        service.delete(userMeals.get(0).getId(), USER_ID);
        userMeals.remove(0);
        assertMatch(service.getAll(USER_ID), userMeals);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(USER_MEAL_MRN_EARLY_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> mealsBetweenDates = service.getBetweenDates(
                USER_MEAL_MRN_EARLY.getDate(), USER_MEAL_EVN_EARLY.getDate(), USER_ID);
        assertMatch(mealsBetweenDates, Arrays.stream(SORTED_USER_MEALS).skip(3).collect(Collectors.toList()));
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), Arrays.asList(SORTED_USER_MEALS));
    }

    @Test
    public void update() {
        Meal meal = new Meal(USER_MEAL_MRN_EARLY);
        meal.setDescription("Updated meal");
        meal.setCalories(9999);
        service.update(meal, USER_ID);
        assertMatch(service.get(meal.getId(), USER_ID), meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal meal = new Meal(USER_MEAL_MRN_EARLY);
        service.update(meal, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.of(2019, Month.MAY, 11, 12, 0),
                "New", 9999);
        service.update(meal, USER_ID);
        assertMatch(service.get(meal.getId(), USER_ID), meal);
    }
}