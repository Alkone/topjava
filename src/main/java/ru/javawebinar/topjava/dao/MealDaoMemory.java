package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealDaoMemory implements MealDao {
    private final AtomicLong nextId = new AtomicLong(1);

    private final Map<Long, Meal> data = new ConcurrentHashMap<>();

    @Override
    public Meal add(Meal meal) {
        Meal mealWithId = new Meal(nextId.getAndIncrement(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        return data.putIfAbsent(mealWithId.getId(), mealWithId);
    }

    @Override
    public Meal get(long id) {
        return data.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(Meal meal) {
        if (meal.getId() > 0 && meal.getId() < nextId.get()) {
            data.put(meal.getId(), meal);
        }
    }

    @Override
    public void delete(long id) {
        data.remove(id);
    }
}
