package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemService {
    ItemRepository itemRepository;
    ItemMapper itemMapper;
    UserService userService;
    UserMapper userMapper;

    public ItemDto create(ItemDto itemDto, Long userId) {
        User owner = userMapper.toUser(userService.getById(userId));
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        return itemMapper.toDTO(itemRepository.save(item));
    }

    public ItemDto update(ItemUpdateRequestDto itemDto, Long id, long userId) {
        User owner = userMapper.toUser(userService.getById(userId));
        Item itemRep = itemRepository.findById(id);
        Item item = itemMapper.toItem(itemDto);

        item.setId(id);
        item.setOwner(owner);
        item.setName(itemDto.getName() == null ? itemRep.getName() : itemDto.getName());
        item.setDescription(itemDto.getDescription() == null ? itemRep.getDescription() : itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable() == null ? itemRep.getAvailable() : itemDto.getAvailable());

        if (!itemRep.getOwner().equals(owner)) {
            throw new NotFoundException("Введен некорректный пользователь");
        }

        return itemMapper.toDTO(itemRepository.update(item));
    }

    public ItemDto getById(Long id, Long userId) {
        userService.getById(userId);
        return itemMapper.toDTO(itemRepository.findById(id));
    }

    public List<ItemDto> getAll(Long userId) {
        userService.getById(userId);
        return itemRepository.getAll().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchByText(String text, Long userId) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        } else {
            userService.getById(userId);
            return itemRepository.getAll().stream()
                    //TODO: придумать более изящный способ
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(item -> item.getAvailable().equals(true))
                    .map(itemMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }
}
