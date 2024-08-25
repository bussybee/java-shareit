package ru.practicum.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
    LocalDateTime nextBooking;
    LocalDateTime lastBooking;
    List<Comment> comments;
}
