package ru.practicum.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.dto.ItemDtoForRequest;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.ResponseDto;
import ru.practicum.request.dto.ResponseDtoWithItems;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestService {
    RequestRepository requestRepository;
    UserMapper userMapper;
    UserService userService;
    RequestMapper requestMapper;
    ItemRepository itemRepository;
    ItemMapper itemMapper;

    public ResponseDto create(RequestDto requestDto, Long userId) {
        User requester = userMapper.toUser(userService.getById(userId));
        ItemRequest request = requestMapper.toEntity(requestDto);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());

        return requestMapper.toDto(requestRepository.save(request));
    }

    public List<ResponseDtoWithItems> getAllByRequester(Long userId) {
        userService.getById(userId);

        List<ItemRequest> requests = requestRepository.findByRequester_Id(userId);
        List<ResponseDtoWithItems> requestsWithItems = requests.stream()
                .map(requestMapper::toDtoWithItems)
                .sorted(Comparator.comparing(ResponseDtoWithItems::getCreated).reversed())
                .toList();
        requestsWithItems.forEach(this::addItems);

        return requestsWithItems;
    }

    private void addItems(ResponseDtoWithItems request) {
        List<Item> items = itemRepository.findByRequest_Id(request.getId());
        List<ItemDtoForRequest> itemsForRequest = items.stream()
                .map(itemMapper::toResponse).toList();
        request.setItems(itemsForRequest);
    }


    public List<ResponseDto> getAll() {
        return requestRepository.findAll().stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseDtoWithItems getRequestById(Long requestId, Long userId) {
        userService.getById(userId);

        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id=" + requestId + " не найден"));
        ResponseDtoWithItems requestWithItems = requestMapper.toDtoWithItems(request);
        addItems(requestWithItems);

        return requestWithItems;
    }
}