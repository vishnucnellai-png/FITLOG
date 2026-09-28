package com.fitlog.workout;

import com.fitlog.common.exception.ResourceNotFoundException;
import com.fitlog.user.UserEntity;
import com.fitlog.user.UserService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserService userService;

    public WorkoutService(WorkoutRepository workoutRepository, UserService userService) {
        this.workoutRepository = workoutRepository;
        this.userService = userService;
    }

    public WorkoutEntity create(WorkoutDTO dto) {
        return workoutRepository.save(toEntity(dto, userService.findById(dto.getUserId())));
    }

    public List<WorkoutEntity> findAll() {
        return workoutRepository.findAll();
    }
    public List<WorkoutEntity> findByUserId(Long userId) {
        userService.findById(userId);
        return workoutRepository.findByUserId(userId);
    }

    public WorkoutEntity findById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found."));
    }

    public WorkoutEntity update(Long id, WorkoutDTO dto) {
        WorkoutEntity workout = findById(id);
        workout.setType(dto.getType());
        workout.setDuration(dto.getDuration());
        workout.setCaloriesBurned(dto.getCaloriesBurned());
        workout.setDate(dto.getDate());
        workout.setUser(userService.findById(dto.getUserId()));
        return workoutRepository.save(workout);
    }

    public void delete(Long id) { workoutRepository.delete(findById(id)); }

    private WorkoutEntity toEntity(WorkoutDTO dto, UserEntity user) {
        return new WorkoutEntity(dto.getType(), dto.getDuration(), dto.getCaloriesBurned(), dto.getDate(), user);
    }
}
