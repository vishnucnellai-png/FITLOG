package com.fitlog.meal;

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
@RequestMapping("/api/meals")
public class MealController {
    private final MealService mealService;
    public MealController(MealService mealService) { this.mealService = mealService; }
    @PostMapping public ResponseEntity<MealEntity> create(@Valid @RequestBody MealDTO dto) { MealEntity meal = mealService.create(dto); return ResponseEntity.created(URI.create("/api/meals/" + meal.getId())).body(meal); }
    @GetMapping public List<MealEntity> findAll() { return mealService.findAll(); }
    @GetMapping("/{id}") public MealEntity findById(@PathVariable Long id) { return mealService.findById(id); }
    @GetMapping("/user/{userId}") public List<MealEntity> findByUserId(@PathVariable Long userId) { return mealService.findByUserId(userId); }
    @PutMapping("/{id}") public MealEntity update(@PathVariable Long id, @Valid @RequestBody MealDTO dto) { return mealService.update(id, dto); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { mealService.delete(id); return ResponseEntity.noContent().build(); }
}
