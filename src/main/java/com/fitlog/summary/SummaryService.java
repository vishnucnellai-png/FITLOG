package com.fitlog.summary;

import com.fitlog.common.exception.BusinessRuleException;
import com.fitlog.goal.GoalRepository;
import com.fitlog.meal.MealEntity;
import com.fitlog.meal.MealRepository;
import com.fitlog.user.UserService;
import com.fitlog.workout.WorkoutEntity;
import com.fitlog.workout.WorkoutRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    private final WorkoutRepository workoutRepository;
    private final MealRepository mealRepository;
    private final GoalRepository goalRepository;
    private final UserService userService;

    public SummaryService(WorkoutRepository workoutRepository, MealRepository mealRepository,
                          GoalRepository goalRepository, UserService userService) {
        this.workoutRepository = workoutRepository;
        this.mealRepository = mealRepository;
        this.goalRepository = goalRepository;
        this.userService = userService;
    }

    public SummaryDTO daily(Long userId, LocalDate date) {
        userService.findById(userId);
        List<WorkoutEntity> workouts = workoutRepository.findByUserIdAndDateBetween(userId, date, date);
        List<MealEntity> meals = mealRepository.findByUserIdAndDateBetween(userId, date, date);
        return build(date, date, workouts, meals, userId);
    }

    public SummaryDTO weekly(Long userId, LocalDate date) {
        userService.findById(userId);
        LocalDate start = date.with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(6);
        List<WorkoutEntity> workouts = workoutRepository.findByUserIdAndDateBetween(userId, start, end);
        List<MealEntity> meals = mealRepository.findByUserIdAndDateBetween(userId, start, end);
        if (workouts.isEmpty() && meals.isEmpty()) {
            throw new BusinessRuleException("No entries found for this week.");
        }
        return build(start, end, workouts, meals, userId);
    }

    private SummaryDTO build(LocalDate startDate, LocalDate endDate, List<WorkoutEntity> workouts,
                             List<MealEntity> meals, Long userId) {
        double caloriesBurned = workouts.stream().mapToDouble(WorkoutEntity::getCaloriesBurned).sum();
        double caloriesConsumed = meals.stream().mapToDouble(MealEntity::getCalories).sum();
        int goal = goalRepository.findByUserId(userId)
                .map(value -> value.getWeeklyWorkoutTarget()).orElse(0);
        return new SummaryDTO(startDate, endDate, caloriesConsumed, caloriesBurned, workouts.size(), goal);
    }
}
