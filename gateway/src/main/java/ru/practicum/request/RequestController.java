package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody RequestDto request,
                                              @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Create request: {}", request);
        return requestClient.create(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequester(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Get requests by user: {}", userId);
        return requestClient.getAllBuRequester(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        log.info("Get all requests");
        return requestClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(name = "X-Sharer-User-Id")
                                                               Long userId, @PathVariable Long id) {
        log.info("Get request by id: {}", id);
        return requestClient.getRequestById(id, userId);
    }
}
