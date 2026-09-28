package com.fitlog.goal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fitlog.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "goals")
public class GoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int weeklyWorkoutTarget;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    protected GoalEntity() { }

    public GoalEntity(int weeklyWorkoutTarget, UserEntity user) {
        this.weeklyWorkoutTarget = weeklyWorkoutTarget;
        this.user = user;
    }

    public Long getId() { return id; }
    public int getWeeklyWorkoutTarget() { return weeklyWorkoutTarget; }
    public void setWeeklyWorkoutTarget(int weeklyWorkoutTarget) { this.weeklyWorkoutTarget = weeklyWorkoutTarget; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    @JsonProperty("userId")
    public Long getUserId() { return user.getId(); }
}
