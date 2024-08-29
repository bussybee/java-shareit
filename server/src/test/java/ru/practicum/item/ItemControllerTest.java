package ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.item.comment.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemGetAllResponseDto;
import ru.practicum.item.dto.ItemUpdateRequestDto;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto;
    private ItemGetAllResponseDto getAllResponseDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item 1")
                .description("item description")
                .available(true)
                .build();

        getAllResponseDto = ItemGetAllResponseDto.builder()
                .id(1L)
                .name("item 1")
                .description("item description")
                .available(true)
                .build();
    }

    @Test
    void createNewItem() throws Exception {
        when(itemService.create(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void updateItem() throws Exception {
        Long itemId = 1L;
        ItemUpdateRequestDto updateRequest = ItemUpdateRequestDto.builder()
                .name("new name")
                .description("new description")
                .build();
        ItemDto updateItem = ItemDto.builder()
                .id(itemId)
                .name("new name")
                .description("new description")
                .build();

        when(itemService.update(any(ItemUpdateRequestDto.class), eq(itemId), eq(1L)))
                .thenReturn(updateItem);

        mvc.perform(patch("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId.intValue())))
                .andExpect(jsonPath("$.name", is(updateItem.getName())))
                .andExpect(jsonPath("$.description", is(updateItem.getDescription())));
    }

    @Test
    void getById() throws Exception {
        when(itemService.getById(1L, 1L))
                .thenReturn(itemDto);

        mvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void getAll() throws Exception {
        when(itemService.getAll(anyLong()))
                .thenReturn(List.of(getAllResponseDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(getAllResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(getAllResponseDto.getName())))
                .andExpect(jsonPath("$[0].description", is(getAllResponseDto.getDescription())));
    }

    @Test
    void searchByText() throws Exception {
        when(itemService.searchByText(anyString(), anyLong()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "item")
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void addComment() throws Exception {
        Long itemId = 1L;
        Long userId = 1L;
        CommentDto comment = new CommentDto("comment text");

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }
}
