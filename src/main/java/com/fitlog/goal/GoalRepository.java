package com.fitlog.goal;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<GoalEntity, Long> {
    Optional<GoalEntity> findByUserId(Long userId);
}
