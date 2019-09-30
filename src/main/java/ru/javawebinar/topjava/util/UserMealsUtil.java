package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);
        System.out.println();
        getFilteredWithExceededByStreamApi(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);

    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        mealList.forEach(meal -> caloriesPerDayMap.merge(toLocalDate(meal), meal.getCalories(), Integer::sum));

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        mealList.forEach(meal -> {
            if (TimeUtil.isBetween(toLocalTime(meal), startTime, endTime)) {
                userMealWithExceedList.add(getUserMealWithExceed(meal, caloriesPerDayMap.get(toLocalDate(meal)) > caloriesPerDay));
            }
        });
        return userMealWithExceedList;
    }

    //Optional 1
    public static List<UserMealWithExceed> getFilteredWithExceededByStreamApi(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = mealList.stream()
                .collect(Collectors.groupingBy(
                        UserMealsUtil::toLocalDate,
                        Collectors.summingInt((UserMeal::getCalories))
                ));

        return mealList.stream()
                .filter(meal -> TimeUtil.isBetween(toLocalTime(meal), startTime, endTime))
                .map(meal -> getUserMealWithExceed(meal, caloriesPerDayMap.get(toLocalDate(meal)) > caloriesPerDay))
                .collect(Collectors.toList());
    }
    //*

    public static UserMealWithExceed getUserMealWithExceed(UserMeal userMeal, boolean exceed) {
        return new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), exceed);
    }

    public static LocalDate toLocalDate(UserMeal userMeal) {
        return userMeal.getDateTime().toLocalDate();
    }

    public static LocalTime toLocalTime(UserMeal userMeal) {
        return userMeal.getDateTime().toLocalTime();
    }


}