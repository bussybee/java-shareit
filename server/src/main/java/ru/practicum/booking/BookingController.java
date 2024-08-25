package ru.practicum.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingResponseDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookingController {
    BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> create(@RequestBody BookingDto bookingDto,
                                                     @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        BookingResponseDto booking = bookingService.create(bookingDto, userId);
        log.info("Booking created {}", booking);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingResponseDto> approve(@PathVariable Long id, @RequestParam Boolean approved,
                                                      @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Booking approved with id {}", id);
        return new ResponseEntity<>(bookingService.approve(id, approved, userId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getById(@PathVariable Long id,
                                                      @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        BookingResponseDto booking = bookingService.getById(id, userId);
        log.info("Getting booking {}", booking);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<BookingResponseDto>> getAllByUser(
            @RequestHeader(name = "X-Sharer-User-Id") Long userId,
            @RequestParam(required = false) BookingState state) {
        log.info("Getting all bookings by user {}", userId);
        return new ResponseEntity<>(bookingService.getAllByUser(userId, state), HttpStatus.OK);
    }
}
