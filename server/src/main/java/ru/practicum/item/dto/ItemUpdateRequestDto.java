package ru.practicum.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemUpdateRequestDto {
    Long id;
    String name;
    String description;
    Boolean available;
}
