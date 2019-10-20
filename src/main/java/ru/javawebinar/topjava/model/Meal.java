package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractBaseEntity {
    private LocalDateTime eatenDateTime;

    private String description;

    private int calories;

    public Meal() {
    }

    public Meal(LocalDateTime eatenDateTime, String description, int calories) {
        this(null, eatenDateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime eatenDateTime, String description, int calories) {
        super(id);
        this.eatenDateTime = eatenDateTime;
        this.description = description;
        this.calories = calories;
    }

    public void setEatenDateTime(LocalDateTime eatenDateTime) {
        this.eatenDateTime = eatenDateTime;
    }

    public LocalDateTime getEatenDateTime() {
        return eatenDateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return eatenDateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return eatenDateTime.toLocalTime();
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + eatenDateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
