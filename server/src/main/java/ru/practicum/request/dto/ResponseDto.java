package ru.practicum.request.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDto {
    Long id;
    String description;
    User requester;
    LocalDateTime created;
}
