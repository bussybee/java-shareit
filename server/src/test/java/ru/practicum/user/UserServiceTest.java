package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;

    @Test
    void saveUser() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email");

        UserDto createdUser = userService.create(userDto);

        assertEquals(userDto.getName(), createdUser.getName());
    }
}
