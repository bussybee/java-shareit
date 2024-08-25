package ru.practicum.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto userDto) {
        log.info("Creating user: {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody UserDto userDto, @PathVariable Long id) {
        log.info("Updating user: {}", userDto);
        return userClient.update(userDto, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Getting all users");
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        log.info("Getting user by id: {}", id);
        return userClient.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting user by id: {}", id);
        userClient.delete(id);
    }
}
