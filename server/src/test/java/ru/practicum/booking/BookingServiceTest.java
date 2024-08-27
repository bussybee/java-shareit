package ru.practicum.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.dto.BookingResponseDto;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceTest {
    BookingService bookingService;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    ItemRepository itemRepository;

    @Test
    void getAllUserBookings() {
        User booker = userRepository.save(User.builder().name("john").email("sina").build());
        User owner = userRepository.save(User.builder().name("carri").email("brad").build());
        Item item = itemRepository.save(Item.builder()
                .name("book")
                .description("interesting")
                .available(true)
                .owner(owner)
                .build());

        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusHours(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build());
        bookingRepository.save(Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(8))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build());

        List<BookingResponseDto> allBookings = bookingService.getAllByUser(1L, null);
        assertThat(allBookings).hasSize(2);
        assertEquals(allBookings.getFirst().getStatus(), BookingStatus.APPROVED);

        List<BookingResponseDto> futureBookings = bookingService.getAllByUser(1L, BookingState.FUTURE);
        assertThat(futureBookings).hasSize(1);
        assertEquals(futureBookings.getFirst().getStatus(), BookingStatus.WAITING);
    }
}
