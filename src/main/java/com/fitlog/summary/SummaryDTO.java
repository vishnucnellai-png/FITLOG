package com.fitlog.summary;

import java.time.LocalDate;

public class SummaryDTO {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double caloriesConsumed;
    private final double caloriesBurned;
    private final double netCalories;
    private final int workoutsCompleted;
    private final int weeklyWorkoutGoal;

    public SummaryDTO(LocalDate startDate, LocalDate endDate, double caloriesConsumed,
                      double caloriesBurned, int workoutsCompleted, int weeklyWorkoutGoal) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.caloriesConsumed = caloriesConsumed;
        this.caloriesBurned = caloriesBurned;
        this.netCalories = caloriesConsumed - caloriesBurned;
        this.workoutsCompleted = workoutsCompleted;
        this.weeklyWorkoutGoal = weeklyWorkoutGoal;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getCaloriesConsumed() { return caloriesConsumed; }
    public double getCaloriesBurned() { return caloriesBurned; }
    public double getNetCalories() { return netCalories; }
    public int getWorkoutsCompleted() { return workoutsCompleted; }
    public int getWeeklyWorkoutGoal() { return weeklyWorkoutGoal; }
}
