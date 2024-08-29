package ru.practicum.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.comment.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemUpdateRequestDto;

import java.util.ArrayList;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto item,
                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Creating new item {}", item);
        return itemClient.create(item, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody @Valid ItemUpdateRequestDto item, @PathVariable Long id,
                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Updating item {}", item);
        return itemClient.update(item, userId, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Getting item {}", id);
        return itemClient.getById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting items");
        return itemClient.getAll(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByText(@RequestParam String text,
                                               @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        if (text == null || text.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        } else {
            log.info("Searching for items with text {}", text);
            return itemClient.searchByText(userId, text);
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long id, @RequestBody CommentDto commentDto,
                                             @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Adding comment {}", commentDto);
        return itemClient.addComment(id, commentDto, userId);
    }
}
