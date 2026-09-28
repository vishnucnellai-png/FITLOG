package com.fitlog.user;

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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping
    public ResponseEntity<UserEntity> create(@Valid @RequestBody UserDTO dto) {
        UserEntity user = userService.create(dto);
        return ResponseEntity.created(URI.create("/api/users/" + user.getId())).body(user);
    }
    @GetMapping public List<UserEntity> findAll() { return userService.findAll(); }
    @GetMapping("/{id}") public UserEntity findById(@PathVariable Long id) { return userService.findById(id); }
    @PutMapping("/{id}") public UserEntity update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) { return userService.update(id, dto); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { userService.delete(id); return ResponseEntity.noContent().build(); }
}
