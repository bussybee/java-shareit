package ru.practicum.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.booking.BookingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;
    @FutureOrPresent
    @NotNull
    LocalDateTime start;
    @FutureOrPresent
    @NotNull
    LocalDateTime end;
    Long itemId;
    Long bookerId;
    BookingStatus status;
}
