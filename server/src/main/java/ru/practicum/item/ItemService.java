package ru.practicum.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.error.NotFoundException;
import ru.practicum.item.comment.*;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemGetAllResponseDto;
import ru.practicum.item.dto.ItemUpdateRequestDto;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.RequestRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserService;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class ItemService {
    ItemRepository itemRepository;
    ItemMapper itemMapper;
    UserService userService;
    UserMapper userMapper;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    CommentMapper commentMapper;
    RequestRepository requestRepository;

    public ItemDto create(ItemDto itemDto, Long userId) {
        User owner = userMapper.toUser(userService.getById(userId));
        Item item = itemMapper.toItem(itemDto);

        Optional<Long> requestId = Optional.ofNullable(itemDto.getRequestId());
        if (requestId.isPresent()) {
            ItemRequest request = requestRepository.findById(requestId.get()).orElseThrow();
            item.setRequest(request);
        }

        item.setOwner(owner);

        return itemMapper.toDTO(itemRepository.save(item));
    }

    public ItemDto update(ItemUpdateRequestDto itemDto, Long id, Long userId) {
        User owner = userMapper.toUser(userService.getById(userId));
        Item itemRep = itemRepository.findById(id).orElseThrow();
        Item item = itemMapper.toItem(itemDto);

        item.setId(id);
        item.setOwner(owner);
        item.setName(itemDto.getName() == null ? itemRep.getName() : itemDto.getName());
        item.setDescription(itemDto.getDescription() == null ? itemRep.getDescription() : itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable() == null ? itemRep.getAvailable() : itemDto.getAvailable());

        if (!itemRep.getOwner().equals(owner)) {
            throw new NotFoundException("Введен некорректный пользователь");
        }

        return itemMapper.toDTO(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public ItemDto getById(Long id, Long userId) {
        userService.getById(userId);
        ItemDto itemDto = itemMapper.toDTO(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена")));
        itemDto.setComments(commentRepository.findByAuthor_IdAndItem_Id(userId, id));
        return itemDto;
    }

    @Transactional
    public List<ItemGetAllResponseDto> getAll(Long userId) {
        userService.getById(userId);

        List<Item> ownerItems = itemRepository.findAllByOwner_Id(userId);
        Map<Long, List<Comment>> commentsByItem = commentRepository.findByItemIn(ownerItems).stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));
        Map<Long, List<Booking>> bookingsByItem = bookingRepository.findAllByItemIn(ownerItems).stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        return ownerItems.stream()
                .map(item -> {
                    ItemGetAllResponseDto itemDto = itemMapper.toResponseDTO(item);
                    itemDto.setComments(commentsByItem.getOrDefault(item.getId(), Collections.emptyList()));

                    List<Booking> bookings = bookingsByItem.getOrDefault(item.getId(), Collections.emptyList());

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

                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
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

    public CommentResponseDto addComment(Long id, Long userId, CommentDto commentDto) {
        bookingRepository.findAllByItem_Id(id).stream()
                .filter(booking -> booking.getBooker().getId().equals(userId))
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                .findFirst()
                .orElseThrow();

        Comment comment = commentMapper.toComment(commentDto);
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        User author = userMapper.toUser(userService.getById(userId));

        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        return commentMapper.toResponseDto(commentRepository.save(comment));
    }
}
