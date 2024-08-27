package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.request.dto.RequestDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestDtoTest {
    private final JacksonTester<RequestDto> json;

    @Test
    void testRequestDto() throws Exception {
        RequestDto requestDto = new RequestDto("desc");

        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
    }
}
