package ru.practicum.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemUpdateRequestDto {
    Long id;
    String name;
    String description;
    Boolean available;
}
