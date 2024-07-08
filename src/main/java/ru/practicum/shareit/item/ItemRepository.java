package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRepository {
    final Map<Long, Item> items = new HashMap<>();
    Long idGenerator = 1L;

    public Item save(Item item) {
        item.setId(idGenerator++);
        item.setName(item.getName());
        item.setDescription(item.getDescription());

        items.put(item.getId(), item);

        return item;
    }


    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundException(String.format("Item with id=%s not found", id));
        }
        return item;
    }

    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }
}
