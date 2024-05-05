package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserInputDTO;
import ru.practicum.shareit.user.dto.UserOutputDTO;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ItemRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private UserInputDTO userInputDTO;
    private UserOutputDTO userOutputDTO;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Long userId = 1L;
    private final Long invalidUserId = 999L;

    public void setUpCreate() {

        userInputDTO = UserInputDTO.builder()
                .email("RuRu@yandex.ru")
                .name("RuRu")
                .build();
        userOutputDTO = UserOutputDTO.builder()
                .id(userId)
                .email("RuRu@yandex.ru")
                .name("RuRu")
                .build();
    }

    public void setUpUpdate() {

        userInputDTO = UserInputDTO.builder()
                .email("updateRuRu@yandex.ru")
                .build();
        userOutputDTO = UserOutputDTO.builder()
                .id(userId)
                .email("updateRuRu@yandex.ru")
                .name("RuRu")
                .build();
    }


}