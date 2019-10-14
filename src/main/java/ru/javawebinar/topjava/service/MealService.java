package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        return checkNotFoundWithId(repository.save(userId, meal), userId);
    }

    public void delete(int userId, int mealId) {
        checkNotFoundWithId(repository.delete(userId, mealId), userId);
    }

    public Meal get(int userId, int mealId) {
        return checkNotFoundWithId(repository.get(userId, mealId), userId);
    }

    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        return checkNotFoundWithId(repository.getAll(userId, startDate, endDate), userId);
    }

    public List<Meal> getAll(int userId) {
        return checkNotFoundWithId(repository.getAll(userId), userId);
    }

    public Meal update(int userId, Meal meal) {
        return checkNotFoundWithId(repository.save(userId, meal), userId);
    }

}