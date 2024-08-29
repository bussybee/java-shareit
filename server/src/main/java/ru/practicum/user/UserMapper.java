package ru.practicum.user;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserUpdateRequestDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(User user);

    User toUser(UserDto userDto);

    User toUser(UserUpdateRequestDto userUpdateRequestDto);
}
