package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final Meal[] SORTED_USER_MEALS = {
            new Meal(START_SEQ + 7, LocalDateTime.of(2019, Month.NOVEMBER, 16, 18, 0), "Ужин юзера", 600),
            new Meal(START_SEQ + 6, LocalDateTime.of(2019, Month.NOVEMBER, 16, 14, 0), "Обед юзера", 800),
            new Meal(START_SEQ + 5, LocalDateTime.of(2019, Month.NOVEMBER, 16, 8, 0), "Завтрак юзера", 500),
            new Meal(START_SEQ + 4, LocalDateTime.of(2019, Month.OCTOBER, 16, 18, 0), "Ужин юзера", 600),
            new Meal(START_SEQ + 3, LocalDateTime.of(2019, Month.OCTOBER, 16, 14, 0), "Обед юзера", 1000),
            new Meal(START_SEQ + 2, LocalDateTime.of(2019, Month.OCTOBER, 16, 8, 0), "Завтрак юзера", 500)};

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
