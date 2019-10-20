package ru.javawebinar.topjava.service;

import org.junit.Assert;
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

import static ru.javawebinar.topjava.MealTestData.SORTED_USER_MEALS;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF8"))
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
        assertMatch(service.get(SORTED_USER_MEALS[0].getId(), USER_ID), SORTED_USER_MEALS[0]);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(SORTED_USER_MEALS[0].getId(), ADMIN_ID);
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
        service.delete(SORTED_USER_MEALS[0].getId(), ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> mealsBetweenDates = service.getBetweenDates(
                SORTED_USER_MEALS[0].getDate(), SORTED_USER_MEALS[2].getDate(), USER_ID);
        assertMatch(mealsBetweenDates, Arrays.asList(SORTED_USER_MEALS[0], SORTED_USER_MEALS[1], SORTED_USER_MEALS[2]));
    }

    @Test
    public void getAll() {
        Assert.assertEquals(service.getAll(USER_ID), Arrays.asList(SORTED_USER_MEALS));
    }

    @Test
    public void update() {
        Meal meal = SORTED_USER_MEALS[1];
        meal.setDescription("Updated meal");
        meal.setCalories(9999);
        service.update(meal, USER_ID);
        assertMatch(service.get(meal.getId(), USER_ID), meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal meal = SORTED_USER_MEALS[1];
        service.update(meal, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal createdMeal = new Meal(LocalDateTime.of(2019, Month.MAY, 11, 12, 0),
                "New", 9999);
        service.update(createdMeal, USER_ID);
        assertMatch(service.get(createdMeal.getId(), USER_ID), createdMeal);
    }
}