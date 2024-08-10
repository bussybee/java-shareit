package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingDto toDTO(Booking booking);

    Booking toBooking(BookingDto bookingDto);

    BookingResponseDto toResponseDto(Booking booking);

    Booking toBooking(BookingResponseDto responseDto);
}
