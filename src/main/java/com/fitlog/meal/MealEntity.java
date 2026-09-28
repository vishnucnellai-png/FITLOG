package com.fitlog.meal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fitlog.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "meals")
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String foodItem;
    private double quantity;
    private double calories;
    private LocalDate date;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    protected MealEntity() { }

    public MealEntity(String foodItem, double quantity, double calories, LocalDate date, UserEntity user) {
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.calories = calories;
        this.date = date;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getFoodItem() { return foodItem; }
    public void setFoodItem(String foodItem) { this.foodItem = foodItem; }
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
    @JsonProperty("userId")
    public Long getUserId() { return user.getId(); }
}
