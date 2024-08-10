package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetAllResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemService {
    ItemRepository itemRepository;
    ItemMapper itemMapper;
    UserService userService;
    UserMapper userMapper;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;

    public ItemDto create(ItemDto itemDto, Long userId) {
        User owner = userMapper.toUser(userService.getById(userId));
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        return itemMapper.toDTO(itemRepository.save(item));
    }

    public ItemDto update(ItemUpdateRequestDto itemDto, Long id, Long userId) {
        User owner = userMapper.toUser(userService.getById(userId));
        Optional<Item> itemRep = itemRepository.findById(id);
        Item item = itemMapper.toItem(itemDto);

        item.setId(id);
        item.setOwner(owner);
        item.setName(itemDto.getName() == null ? itemRep.orElseThrow().getName() : itemDto.getName());
        item.setDescription(itemDto.getDescription() == null ? itemRep.orElseThrow().getDescription() : itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable() == null ? itemRep.orElseThrow().getAvailable() : itemDto.getAvailable());

        if (!itemRep.orElseThrow().getOwner().equals(owner)) {
            throw new NotFoundException("Введен некорректный пользователь");
        }

        return itemMapper.toDTO(itemRepository.save(item));
    }

    public ItemDto getById(Long id, Long userId) {
        userService.getById(userId);
        ItemDto itemDto = itemMapper.toDTO(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена")));
        itemDto.setComments(commentRepository.findByAuthor_IdAndItem_Id(userId, id));
        return itemDto;
    }

    public List<ItemGetAllResponseDto> getAll(Long userId) {
        userService.getById(userId);

        List<ItemGetAllResponseDto> items = new ArrayList<>();
        List<Item> allOwnerItems = itemRepository.findAllByOwner_Id(userId);

        for (Item item : allOwnerItems) {
            ItemGetAllResponseDto itemDto = itemMapper.toResponseDTO(item);
            itemDto.setComments(commentRepository.findByAuthor_IdAndItem_Id(userId, item.getId()));

            List<Booking> bookings = bookingRepository.findAllByItem_Id(item.getId());

            Booking lastBooking = bookings.stream()
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                    .max(Comparator.comparing(Booking::getStart))
                    .orElse(null);

            Booking nextBooking = bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .min(Comparator.comparing(Booking::getStart))
                    .orElse(null);

            itemDto.setLastBooking(lastBooking != null ? lastBooking.getEnd() : null);
            itemDto.setNextBooking(nextBooking != null ? nextBooking.getStart() : null);

            items.add(itemDto);
        }

        return items;
    }

    public List<ItemDto> searchByText(String text, Long userId) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        } else {
            userService.getById(userId);
            return itemRepository.searchByText(text).stream()
                    .filter(item -> item.getAvailable().equals(true))
                    .map(itemMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public CommentDto addComment(Long id, Long userId, CommentDto commentDto) {
        bookingRepository.findAllByItem_Id(id).stream()
                .filter(booking -> booking.getItem().getOwner().getId().equals(userId))
                .findFirst()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                .orElseThrow();

        Comment comment = commentMapper.toComment(commentDto);
        Item item = itemRepository.findById(id).orElseThrow();
        User author = userMapper.toUser(userService.getById(userId));

        comment.setItem(item);
        comment.setAuthor(author);

        return commentMapper.toDTO(commentRepository.save(comment));
    }
}
