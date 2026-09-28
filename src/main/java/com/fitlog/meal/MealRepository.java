package com.fitlog.meal;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<MealEntity, Long> {
    List<MealEntity> findByUserId(Long userId);
    List<MealEntity> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
