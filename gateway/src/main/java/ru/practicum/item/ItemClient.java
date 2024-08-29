package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.item.comment.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemUpdateRequestDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemDto itemDto, Long userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> update(ItemUpdateRequestDto itemDto, Long userId, Long id) {
        return patch("" + id, userId, itemDto);
    }

    public ResponseEntity<Object> getById(Long id, Long userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getAll(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchByText(Long userId, String text) {
        return get("/search/?text=" + text, userId);
    }

    public ResponseEntity<Object> addComment(Long itemId, CommentDto commentDto, Long userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
