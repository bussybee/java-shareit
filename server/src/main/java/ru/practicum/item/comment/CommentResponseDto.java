package ru.practicum.item.comment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.Item;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponseDto {
    Long id;
    String text;
    Item item;
    String authorName;
    LocalDateTime created;
}
