package ru.practicum.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.booking.BookingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long itemId;
    Long bookerId;
    BookingStatus status;
}
