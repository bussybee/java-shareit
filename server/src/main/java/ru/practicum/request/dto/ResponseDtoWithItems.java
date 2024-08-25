package ru.practicum.request.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.dto.ItemDtoForRequest;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDtoWithItems {
    Long id;
    String description;
    User requester;
    LocalDateTime created;
    List<ItemDtoForRequest> items;
}
