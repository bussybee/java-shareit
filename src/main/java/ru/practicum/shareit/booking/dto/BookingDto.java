package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
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
