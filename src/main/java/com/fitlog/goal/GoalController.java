package com.fitlog.goal;

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
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalService goalService;
    public GoalController(GoalService goalService) { this.goalService = goalService; }
    @PostMapping public ResponseEntity<GoalEntity> create(@Valid @RequestBody GoalDTO dto) { GoalEntity goal = goalService.create(dto); return ResponseEntity.created(URI.create("/api/goals/" + goal.getId())).body(goal); }
    @GetMapping public List<GoalEntity> findAll() { return goalService.findAll(); }
    @GetMapping("/{id}") public GoalEntity findById(@PathVariable Long id) { return goalService.findById(id); }
    @GetMapping("/user/{userId}") public GoalEntity findByUserId(@PathVariable Long userId) { return goalService.findByUserId(userId); }
    @PutMapping("/{id}") public GoalEntity update(@PathVariable Long id, @Valid @RequestBody GoalDTO dto) { return goalService.update(id, dto); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { goalService.delete(id); return ResponseEntity.noContent().build(); }
}
