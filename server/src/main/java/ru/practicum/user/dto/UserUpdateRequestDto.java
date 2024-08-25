package ru.practicum.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequestDto {
    String name;
    String email;
}
