package com.fitlog.goal;

import com.fitlog.common.exception.BusinessRuleException;
import com.fitlog.common.exception.ResourceNotFoundException;
import com.fitlog.user.UserService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserService userService;

    public GoalService(GoalRepository goalRepository, UserService userService) {
        this.goalRepository = goalRepository;
        this.userService = userService;
    }

    public GoalEntity create(GoalDTO dto) {
        if (goalRepository.findByUserId(dto.getUserId()).isPresent()) {
            throw new BusinessRuleException("This user already has a goal.");
        }
        return goalRepository.save(new GoalEntity(dto.getWeeklyWorkoutTarget(), userService.findById(dto.getUserId())));
    }

    public List<GoalEntity> findAll() { return goalRepository.findAll(); }
    public GoalEntity findById(Long id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found."));
    }
    public GoalEntity findByUserId(Long userId) {
        userService.findById(userId);
        return goalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found."));
    }

    public GoalEntity update(Long id, GoalDTO dto) {
        GoalEntity goal = findById(id);
        if (!goal.getUser().getId().equals(dto.getUserId()) && goalRepository.findByUserId(dto.getUserId()).isPresent()) {
            throw new BusinessRuleException("This user already has a goal.");
        }
        goal.setWeeklyWorkoutTarget(dto.getWeeklyWorkoutTarget());
        goal.setUser(userService.findById(dto.getUserId()));
        return goalRepository.save(goal);
    }

    public void delete(Long id) { goalRepository.delete(findById(id)); }
}
