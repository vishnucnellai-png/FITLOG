package com.fitlog.user;

import com.fitlog.common.exception.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity create(UserDTO dto) {
        return userRepository.save(new UserEntity(dto.getName(), dto.getEmail()));
    }

    public List<UserEntity> findAll() { return userRepository.findAll(); }

    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    public UserEntity update(Long id, UserDTO dto) {
        UserEntity user = findById(id);
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    public void delete(Long id) { userRepository.delete(findById(id)); }
}
