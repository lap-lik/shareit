package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDTOTest {
    @Autowired
    private JacksonTester<ItemRequestInputDTO> json;

    private final String content = "{\"description\":\"Хотел бы воспользоваться щёткой для обуви\",\"requesterId\":1}";
    private final ItemRequestInputDTO itemRequestInputDTO = ItemRequestInputDTO.builder()
            .description("Хотел бы воспользоваться щёткой для обуви")
            .requesterId(1L)
            .build();

    @Test
    public void testJsonDeserialization() throws Exception {

        ItemRequestInputDTO result = json.parse(content).getObject();

        assertThat(result.getDescription()).isEqualTo(itemRequestInputDTO.getDescription());
        assertThat(result.getRequesterId()).isEqualTo(itemRequestInputDTO.getRequesterId());
    }
}
