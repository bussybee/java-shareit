package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDtoWithItems;
import ru.practicum.shareit.request.dto.ResponseDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestController {
    RequestService requestService;

    @PostMapping
    public ResponseEntity<ResponseDto> create(@RequestBody RequestDto request,
                                              @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(requestService.create(request, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponseDtoWithItems>> getAllByRequester(@RequestHeader(name = "X-Sharer-User-Id")
                                                                             Long userId) {
        return new ResponseEntity<>(requestService.getAllByRequester(userId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseDto>> getAll() {
        return new ResponseEntity<>(requestService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDtoWithItems> getRequestById(@RequestHeader(name = "X-Sharer-User-Id")
                                                                             Long userId, @PathVariable Long id) {
        return new ResponseEntity<>(requestService.getRequestById(userId, id), HttpStatus.OK);
    }
}