package com.fitlog.goal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class GoalDTO {

    @NotNull(message = "Weekly workout target is required.")
    @Positive(message = "Weekly workout target must be greater than zero.")
    private Integer weeklyWorkoutTarget;
    @NotNull(message = "User is required.")
    private Long userId;

    public Integer getWeeklyWorkoutTarget() { return weeklyWorkoutTarget; }
    public void setWeeklyWorkoutTarget(Integer weeklyWorkoutTarget) { this.weeklyWorkoutTarget = weeklyWorkoutTarget; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
