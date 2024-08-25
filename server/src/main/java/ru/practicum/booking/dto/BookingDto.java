package ru.practicum.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long itemId;
    Long bookerId;
    BookingStatus status;
}
