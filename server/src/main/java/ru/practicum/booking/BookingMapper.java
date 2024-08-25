package ru.practicum.booking;

import org.mapstruct.Mapper;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingResponseDto;


@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toBooking(BookingDto bookingDto);

    BookingResponseDto toResponseDto(Booking booking);

    Booking toBooking(BookingResponseDto responseDto);
}
