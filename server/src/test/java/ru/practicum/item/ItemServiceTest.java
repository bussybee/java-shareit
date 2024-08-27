package ru.practicum.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.BookingStatus;
import ru.practicum.item.dto.ItemGetAllResponseDto;
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
public class ItemServiceTest {
    ItemService itemService;
    UserRepository userRepository;
    ItemRepository itemRepository;
    BookingRepository bookingRepository;

    @Test
    void getAll() {
        User user1 = User.builder().name("john").email("sina").build();
        User user2 = User.builder().name("carri").email("brad").build();
        User owner = userRepository.save(user1);
        User booker = userRepository.save(user2);

        Item item1 = Item.builder().name("book").description("interesting").available(true).owner(owner).build();
        Item item2 = Item.builder().name("chair").description("comfortable").available(true).owner(owner).build();
        Item saved1Item = itemRepository.save(item1);
        itemRepository.save(item2);

        Booking booking = Booking.builder()
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusMinutes(30))
                .item(saved1Item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        Booking savedBook = bookingRepository.save(booking);

        List<ItemGetAllResponseDto> items = itemService.getAll(owner.getId());

        assertThat(items).hasSize(2);
        assertEquals(items.get(0).getName(), "book");
        assertEquals(items.get(1).getDescription(),"comfortable");
        assertEquals(items.get(0).getLastBooking(), savedBook.getEnd());
    }
}
