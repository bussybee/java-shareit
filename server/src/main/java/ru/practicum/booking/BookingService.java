package ru.practicum.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingResponseDto;
import ru.practicum.error.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.ItemService;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserService;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class BookingService {
    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    UserService userService;
    UserMapper userMapper;
    ItemMapper itemMapper;
    ItemService itemService;

    public BookingResponseDto create(BookingDto bookingDto, Long userId) {
        User booker = userMapper.toUser(userService.getById(userId));
        Item item = itemMapper.toItem(itemService.getById(bookingDto.getItemId(), userId));

        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        if (booking.getStart().isEqual(booking.getEnd())) {
            throw new ValidationException("Время начала и окончания не могут совпадать");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для аренды");
        }

        return bookingMapper.toResponseDto(bookingRepository.save(booking));
    }

    public BookingResponseDto approve(Long id, Boolean approved, Long userId) {
        Booking booking = bookingMapper.toBooking(getById(id, userId));

        if (approved.equals(true)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return bookingMapper.toResponseDto(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getById(Long id, Long userId) {
        Optional<Booking> booking = bookingRepository.findById(id);

        if (booking.isPresent() && (booking.get().getItem().getOwner().getId().equals(userId)
                || booking.get().getBooker().getId().equals(userId))) {
            return bookingMapper.toResponseDto(booking.get());
        } else {
            throw new ValidationException("Бронирование не найдено");
        }
    }

    @Transactional
    public List<BookingResponseDto> getAllByUser(Long userId, BookingState state) {
        List<Booking> allBookings = new ArrayList<>();

        switch (Optional.ofNullable(state).orElse(BookingState.ALL)) {
            case ALL:
                allBookings =  bookingRepository.findAllByBooker_IdOrderByStart(userId);
                break;
            case CURRENT:
                allBookings = bookingRepository.findAllByBooker_IdAndEndIsAfterOrderByStart(userId, LocalDateTime.now());
                break;
            case PAST:
                allBookings = bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStart(userId, LocalDateTime.now());
                break;
            case FUTURE:
                allBookings = bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStart(userId, LocalDateTime.now());
                break;
            case WAITING:
                allBookings = bookingRepository.findAllByBooker_IdAndStatusOrderByStart(userId, BookingStatus.WAITING);
                break;
            case REJECTING:
                allBookings = bookingRepository.findAllByBooker_IdAndStatusOrderByStart(userId, BookingStatus.REJECTED);
        }

        return allBookings.stream()
                .map(bookingMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
