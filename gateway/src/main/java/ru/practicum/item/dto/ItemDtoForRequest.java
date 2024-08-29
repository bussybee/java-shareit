package ru.practicum.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ItemDtoForRequest {
    Long id;
    String name;
    Long ownerId;
}
