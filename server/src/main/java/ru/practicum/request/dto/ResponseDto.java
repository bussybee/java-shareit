package ru.practicum.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDto {
    Long id;
    String description;
    User requester;
    LocalDateTime created;
}
