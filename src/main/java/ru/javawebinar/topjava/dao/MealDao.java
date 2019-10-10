package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal add(Meal meal);

    Meal get(long id);

    List<Meal> getAll();

    void update(Meal meal);

    void delete(long id);
}
