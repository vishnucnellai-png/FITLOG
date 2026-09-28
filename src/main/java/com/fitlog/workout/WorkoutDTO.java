package com.fitlog.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public class WorkoutDTO {

    @NotBlank(message = "Workout type is required.")
    private String type;
    @NotNull(message = "Duration is required.")
    @Positive(message = "Duration must be greater than zero.")
    private Integer duration;
    @NotNull(message = "Calories burned is required.")
    @PositiveOrZero(message = "Calories burned cannot be negative.")
    private Double caloriesBurned;
    @NotNull(message = "Date is required.")
    private LocalDate date;
    @NotNull(message = "User is required.")
    private Long userId;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Double getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(Double caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
