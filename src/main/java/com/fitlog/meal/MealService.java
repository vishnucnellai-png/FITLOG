package com.fitlog.meal;

import com.fitlog.common.exception.ResourceNotFoundException;
import com.fitlog.user.UserEntity;
import com.fitlog.user.UserService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final UserService userService;

    public MealService(MealRepository mealRepository, UserService userService) {
        this.mealRepository = mealRepository;
        this.userService = userService;
    }

    public MealEntity create(MealDTO dto) {
        return mealRepository.save(toEntity(dto, userService.findById(dto.getUserId())));
    }

    public List<MealEntity> findAll() { return mealRepository.findAll(); }
    public List<MealEntity> findByUserId(Long userId) {
        userService.findById(userId);
        return mealRepository.findByUserId(userId);
    }

    public MealEntity findById(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found."));
    }

    public MealEntity update(Long id, MealDTO dto) {
        MealEntity meal = findById(id);
        meal.setFoodItem(dto.getFoodItem());
        meal.setQuantity(dto.getQuantity());
        meal.setCalories(dto.getCalories());
        meal.setDate(dto.getDate());
        meal.setUser(userService.findById(dto.getUserId()));
        return mealRepository.save(meal);
    }

    public void delete(Long id) { mealRepository.delete(findById(id)); }

    private MealEntity toEntity(MealDTO dto, UserEntity user) {
        return new MealEntity(dto.getFoodItem(), dto.getQuantity(), dto.getCalories(), dto.getDate(), user);
    }
}
