package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDTOTest {
    @Autowired
    private JacksonTester<ItemRequestInputDTO> json;

    private final LocalDateTime now = LocalDateTime.now();

    private final  ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("Хотел бы воспользоваться щёткой для обуви")
            .created(now)
            .requester(User.builder()
                    .id(1L)
                    .name("RuRu")
                    .email("RuRu@yandex.ru")
                    .build())
            .build();

    private final  ItemRequestInputDTO itemRequestInputDTO = ItemRequestInputDTO.builder()
            .description("Хотел бы воспользоваться щёткой для обуви")
            .requesterId(1L)
            .build();

    private final ItemRequestOutputDTO itemRequestOutputDTO = ItemRequestOutputDTO.builder()
            .id(1L)
            .description("Хотел бы воспользоваться щёткой для обуви")
            .created(now)
            .requesterId(1L)
            .items(new ArrayList<>())
            .build();

    @Test
    public void testJsonSerialization() throws Exception {
        String content = "{\"description\":\"Хотел бы воспользоваться щёткой для обуви\",\"requesterId\":1}";

        String result = json.write(itemRequestInputDTO).getJson();


        assertThat(result).isEqualTo(content);
    }

    @Test
    public void testJsonDeserialization() throws Exception {
        String content = "{\"description\":\"Хотел бы воспользоваться щёткой для обуви\",\"requesterId\":1}";

        ItemRequestInputDTO result = json.parse(content).getObject();

        assertThat(result.getDescription()).isEqualTo(itemRequestInputDTO.getDescription());
        assertThat(result.getRequesterId()).isEqualTo(itemRequestInputDTO.getRequesterId());
    }
}
