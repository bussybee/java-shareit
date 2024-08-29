package ru.practicum.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.ResponseDto;
import ru.practicum.request.dto.ResponseDtoWithItems;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;

    private RequestDto requestDto;
    private ResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new RequestDto("red shoes");
        responseDto = new ResponseDto(1L, "red shoes", new User(), LocalDateTime.now());
    }

    @Test
    void createRequest() throws Exception {
        when(requestService.create(any(RequestDto.class), anyLong()))
                .thenReturn(responseDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("red shoes")));
    }

    @Test
    void getAll() throws Exception {
        when(requestService.getAll())
                .thenReturn(List.of(responseDto));

        mvc.perform(get("/requests/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is("red shoes")));
    }

    @Test
    void getRequestById() throws Exception {
        ResponseDtoWithItems response = new ResponseDtoWithItems(1L, "red shoes", new User(), LocalDateTime.now(), List.of());
        when(requestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(response);

        mvc.perform(get("/requests/{id}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("red shoes")));
    }
}
