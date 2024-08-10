package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookingController {
    BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> create(@RequestBody @Valid BookingDto bookingDto,
                                                     @RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId) {
        return new ResponseEntity<>(bookingService.create(bookingDto, userId), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingResponseDto> approve(@PathVariable Long id, @RequestParam Boolean approved,
                                                      @RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId) {
        return new ResponseEntity<>(bookingService.approve(id, approved, userId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getById(@PathVariable Long id,
                                                  @RequestHeader(name = "X-Sharer-User-Id", required = false ) Long userId) {
        return new ResponseEntity<>(bookingService.getById(id, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllByUser(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId,
                                                                  @RequestParam(required = false, defaultValue = "ALL") String state) {
        return new ResponseEntity<>(bookingService.getAllByUser(userId, state), HttpStatus.OK);
    }
}
