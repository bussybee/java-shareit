package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto user) {
        log.info("Creating user: {}", user);
        return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@RequestBody @Valid UserUpdateRequestDto user, @PathVariable Long id) {
        log.info("Updating user: {}", user);
        return new ResponseEntity<>(userService.update(user, id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("Getting all users");
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        log.info("Getting user by id: {}", id);
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        log.info("Deleted user: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
