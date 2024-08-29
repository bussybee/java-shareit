package ru.practicum.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookingDto bookingDto,
                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Create booking: {}", bookingDto);
        return bookingClient.create(bookingDto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> approve(@PathVariable Long id, @RequestParam Boolean approved,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Approve booking: {}", id);
        return bookingClient.approve(id, approved, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get booking: {}", id);
        return bookingClient.getById(id, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllByBooker(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                               @RequestParam(required = false) BookingState state) {
        log.info("Get bookings by booker: {}", userId);
        return bookingClient.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                 @RequestParam(required = false) BookingState state) {
        log.info("Get bookings by owner: {}", userId);
        return bookingClient.getAllByOwner(userId, state);
    }
}
