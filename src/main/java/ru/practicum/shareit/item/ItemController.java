package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetAllResponseDto;
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
    public ResponseEntity<ItemDto> create(@RequestBody @Valid ItemDto item,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Creating new item: {}", item);
        return new ResponseEntity<>(itemService.create(item, userId), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@RequestBody @Valid ItemUpdateRequestDto item, @PathVariable Long id,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Updating existing item: {}", item);
        return new ResponseEntity<>(itemService.update(item, id, userId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable Long id,
                                           @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Getting item by id: {}", id);
        return new ResponseEntity<>(itemService.getById(id, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemGetAllResponseDto>> getAll(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Getting all items");
        return new ResponseEntity<>(itemService.getAll(userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchByText(@RequestParam String text,
                                                      @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Searching for items by text: {}", text);
        return new ResponseEntity<>(itemService.searchByText(text, userId), HttpStatus.OK);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long id, @RequestBody CommentDto commentDto,
                                                 @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(itemService.addComment(id, userId, commentDto), HttpStatus.OK);
    }
}
