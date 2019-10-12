package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(1, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            log.info("user id {}, save {}", userId, meal);
            return repository.put(meal.getId(), meal);
        }
        // treat case: update, but not present in storage
        if (meal.getId() >= 0
                && meal.getId() < counter.get()
                && repository.get(meal.getId()).getUserId() == userId) {
            meal.setUserId(userId);
            log.info("user id {}, update {}", userId, meal);
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        } else return null;
    }

    @Override
    public boolean delete(int userId, int id) {
        if (userId != repository.get(id).getUserId()) return false;
        log.info("user id {}, delete {}", userId, id);
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        Meal meal = repository.get(id);
        if (Objects.isNull(meal) || meal.getUserId() != userId) return null;
        log.info("user id {}, get {}", userId, id);
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate,
                             LocalTime startTime, LocalTime endTime) {
        log.info("user id {}, getAll startDate {}, endDate {}, startTime {}, endTime {}",
                userId, startDate, endDate, startTime, endTime);
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

