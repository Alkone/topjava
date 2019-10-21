package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    // MRN - MORNING, EVN - EVENING
    public static final int USER_MEAL_MRN_EARLY_ID = START_SEQ + 2;
    public static final int USER_MEAL_DAY_EARLY_ID = START_SEQ + 3;
    public static final int USER_MEAL_EVN_EARLY_ID = START_SEQ + 4;
    public static final int USER_MEAL_MRN_LATE_ID = START_SEQ + 5;
    public static final int USER_MEAL_DAY_LATE_ID = START_SEQ + 6;
    public static final int USER_MEAL_EVN_LATE_ID = START_SEQ + 7;


    public static final Meal USER_MEAL_MRN_EARLY = new Meal(USER_MEAL_MRN_EARLY_ID, LocalDateTime.of(2019, Month.OCTOBER, 16, 8, 0), "Завтрак юзера", 500);
    public static final Meal USER_MEAL_DAY_EARLY = new Meal(USER_MEAL_DAY_EARLY_ID, LocalDateTime.of(2019, Month.OCTOBER, 16, 14, 0), "Обед юзера", 1000);
    public static final Meal USER_MEAL_EVN_EARLY = new Meal(USER_MEAL_EVN_EARLY_ID, LocalDateTime.of(2019, Month.OCTOBER, 16, 18, 0), "Ужин юзера", 600);
    public static final Meal USER_MEAL_MRN_LATE = new Meal(USER_MEAL_MRN_LATE_ID, LocalDateTime.of(2019, Month.NOVEMBER, 16, 8, 0), "Завтрак юзера", 500);
    public static final Meal USER_MEAL_DAY_LATE = new Meal(USER_MEAL_DAY_LATE_ID, LocalDateTime.of(2019, Month.NOVEMBER, 16, 14, 0), "Обед юзера", 800);
    public static final Meal USER_MEAL_EVN_LATE = new Meal(USER_MEAL_EVN_LATE_ID, LocalDateTime.of(2019, Month.NOVEMBER, 16, 18, 0), "Ужин юзера", 600);

    public static final Meal[] SORTED_USER_MEALS = {USER_MEAL_EVN_LATE, USER_MEAL_DAY_LATE, USER_MEAL_MRN_LATE, USER_MEAL_EVN_EARLY, USER_MEAL_DAY_EARLY, USER_MEAL_MRN_EARLY};

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
