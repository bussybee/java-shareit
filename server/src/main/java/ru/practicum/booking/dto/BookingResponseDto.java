package ru.practicum.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.booking.BookingStatus;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponseDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    User booker;
    BookingStatus status;
}
