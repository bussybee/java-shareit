package ru.practicum.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.item.comment.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemGetAllResponseDto {
    Long id;
    String name;
    String description;
    Boolean available;
    LocalDateTime nextBooking;
    LocalDateTime lastBooking;
    List<Comment> comments;
}
