package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.getTestData().forEach(meal -> save(1, meal));
        MealsUtil.getTestData().forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        meal.setUserId(userId);

        if (meal.isNew()) {
            meal.setId(counter.getAndIncrement());
            getMapByUser(userId).put(meal.getId(), meal);
            log.info("user id {}, save {}", userId, meal);
            return meal;
        }
        // treat case: update, but not present in storage
        log.info("user id {}, update {}", userId, meal);
        return getMapByUser(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        return getMapByUser(userId).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("user id {}, get {}", userId, id);
        return getMapByUser(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("user id {}, getAll ", userId);
        return getMapByUser(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("user id {}, getAll startDate {}, endDate {}",
                userId, startDate, endDate);
        return getMapByUser(userId).values().stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMapByUser(int userId) {
        Map<Integer, Meal> map = repository.putIfAbsent(userId, new HashMap<>());
        if (map == null) {
            map = repository.get(userId);
        }
        return map;
    }
}

