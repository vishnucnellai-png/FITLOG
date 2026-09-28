package com.fitlog.workout;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {
    private final WorkoutService workoutService;
    public WorkoutController(WorkoutService workoutService) { this.workoutService = workoutService; }
    @PostMapping public ResponseEntity<WorkoutEntity> create(@Valid @RequestBody WorkoutDTO dto) { WorkoutEntity workout = workoutService.create(dto); return ResponseEntity.created(URI.create("/api/workouts/" + workout.getId())).body(workout); }
    @GetMapping public List<WorkoutEntity> findAll() {
        return workoutService.findAll();
    }
    @GetMapping("/{id}") public WorkoutEntity findById(@PathVariable Long id) { return workoutService.findById(id); }
    @GetMapping("/user/{userId}") public List<WorkoutEntity> findByUserId(@PathVariable Long userId) { return workoutService.findByUserId(userId); }
    @PutMapping("/{id}") public WorkoutEntity update(@PathVariable Long id, @Valid @RequestBody WorkoutDTO dto) { return workoutService.update(id, dto); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { workoutService.delete(id); return ResponseEntity.noContent().build(); }
}
