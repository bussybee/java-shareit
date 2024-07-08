package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ItemController {
    ItemService itemService;

    @PostMapping
    ResponseEntity<ItemDto> create(@RequestBody @Valid ItemDto item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Creating new item: {}", item);
        return new ResponseEntity<>(itemService.create(item, userId), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    ResponseEntity<ItemDto> update(@RequestBody @Valid ItemUpdateRequestDto item, @PathVariable Long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Updating existing item: {}", item);
        return new ResponseEntity<>(itemService.update(item, id, userId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ItemDto> getById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting item by id: {}", id);
        return new ResponseEntity<>(itemService.getById(id, userId), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<ItemDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting all items");
        return new ResponseEntity<>(itemService.getAll(userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<List<ItemDto>> searchByText(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Searching for items by text: {}", text);
        return new ResponseEntity<>(itemService.searchByText(text, userId), HttpStatus.OK);
    }
}
