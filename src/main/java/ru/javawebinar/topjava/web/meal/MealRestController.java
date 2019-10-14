package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        return getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAll(LocalDate startDate, LocalDate endDate,
                               LocalTime startTime, LocalTime endTime) {
        log.info("getAll with startDate{}, endDate{}, startTime{}, endTime{}",
                startDate, endDate, startTime, endTime);
        List<MealTo> filteredByDate = getTos(service.getAll(authUserId(),
                startDate != null ? startDate : LocalDate.MIN,
                endDate != null ? endDate : LocalDate.MAX), authUserCaloriesPerDay());
        //filtering by local time
        return filteredByDate.stream()
                .filter(mealTo -> DateTimeUtil.isBetween(mealTo.getLocalTime(),
                        startTime != null ? startTime : LocalTime.MIN,
                        endTime != null ? endTime : LocalTime.MAX))
                .collect(Collectors.toList());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(authUserId(), id);
    }

    public void create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        service.create(authUserId(), meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(authUserId(), id);
    }

    public void update(Meal meal, int id) {
        log.info("update {}", id);
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }


}