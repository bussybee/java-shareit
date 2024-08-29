package ru.practicum.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.request.dto.ResponseDtoWithItems;
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
public class RequestServiceTest {
    RequestService requestService;
    RequestRepository requestRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;

    @Test
    @DirtiesContext
    void getAllByRequester() {
        User user1 = User.builder().name("john").email("sina").build();
        User user2 = User.builder().name("carri").email("brad").build();
        User owner = userRepository.save(user1);
        User requester = userRepository.save(user2);

        ItemRequest itemRequest1 = ItemRequest.builder()
                .description("cool book")
                .requester(requester)
                .created(LocalDateTime.now())
                .build();
        ItemRequest savedRequest = requestRepository.save(itemRequest1);

        itemRepository.save(Item.builder()
                .name("book")
                .description("interesting")
                .available(true)
                .owner(owner)
                .request(savedRequest)
                .build());
        itemRepository.save(Item.builder()
                .name("another book")
                .description("beautiful")
                .available(true)
                .owner(owner)
                .request(savedRequest)
                .build());

        List<ResponseDtoWithItems> requests = requestService.getAllByRequester(requester.getId());

        assertThat(requests.size()).isEqualTo(1);
        assertEquals(requests.getFirst().getItems().get(1).getName(), "another book");
    }
}
