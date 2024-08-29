package ru.practicum.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.comment.CommentDto;
import ru.practicum.item.comment.CommentResponseDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemGetAllResponseDto;
import ru.practicum.item.dto.ItemUpdateRequestDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ItemController {
    ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestBody ItemDto item,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        ItemDto createdItem = itemService.create(item, userId);
        log.info("Created item: {}", createdItem);
        return new ResponseEntity<>(createdItem, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@RequestBody ItemUpdateRequestDto item, @PathVariable Long id,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        ItemDto updatedItem = itemService.update(item, id, userId);
        log.info("Updated item: {}", updatedItem);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable Long id,
                                           @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        ItemDto item = itemService.getById(id, userId);
        log.info("Getting item: {}", item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemGetAllResponseDto>> getAll(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        List<ItemGetAllResponseDto> items = itemService.getAll(userId);
        log.info("Getting {} items", items.size());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchByText(@RequestParam String text,
                                                      @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        List<ItemDto> items = itemService.searchByText(text, userId);
        log.info("Searching items by text: {}", items);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long id, @RequestBody CommentDto commentDto,
                                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        CommentResponseDto comment = itemService.addComment(id, userId, commentDto);
        log.info("Added comment: {}", comment);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }
}
