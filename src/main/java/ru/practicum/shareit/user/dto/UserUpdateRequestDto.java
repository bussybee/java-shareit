package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequestDto {
    Long id;
    String name;
    @Email
    String email;
}
