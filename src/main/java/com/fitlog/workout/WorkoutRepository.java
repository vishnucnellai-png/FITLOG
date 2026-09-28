package com.fitlog.workout;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
    List<WorkoutEntity> findByUserId(Long userId);
    List<WorkoutEntity> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
