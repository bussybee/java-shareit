package ru.practicum.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserUpdateRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto user) {
        UserDto createdUser = userService.create(user);
        log.info("User created: {}", createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@RequestBody UserUpdateRequestDto user, @PathVariable Long id) {
        UserDto updatedUser = userService.update(user, id);
        log.info("User updated: {}", updatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> allUsers = userService.getAll();
        log.info("Getting {} users", allUsers.size());
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        UserDto user = userService.getById(id);
        log.info("Getting user: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        log.info("Deleted user: {}", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
