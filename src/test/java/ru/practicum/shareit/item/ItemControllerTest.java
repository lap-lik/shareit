package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemInputDTO;
import ru.practicum.shareit.item.dto.ItemOutputDTO;
import ru.practicum.shareit.item.dto.ItemShortOutputDTO;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = ItemController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService service;

    private ItemInputDTO inputDTO;
    private ItemOutputDTO outputDTO;
    private ItemShortOutputDTO shortOutputDTO;
    private final Long itemId1 = 1L;
    private final Long userId1 = 1L;
    private final Long invalidItemId = 999L;
    private final NotFoundException notFoundException = NotFoundException.builder().message("Exception").build();

    void setUp() {

        inputDTO = ItemInputDTO.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();

        shortOutputDTO = ItemShortOutputDTO.builder()
                .id(itemId1)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();

        outputDTO = ItemOutputDTO.builder()
                .id(itemId1)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .comments(new ArrayList<>())
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать предмет, передается пустое поле name, " +
            "возвращается ответ: HttpStatus.BAD_REQUEST.")
    void testCreateItem_WithEmptyName_ResultException() {

        log.info("Start test: создания предмет, передается пустое поле name.");
        setUp();
        inputDTO = inputDTO.toBuilder()
                .name(null)
                .build();

        mvc.perform(post("/items")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ConstraintViolationException.class));

        verify(service, never()).create(userId1, inputDTO);

        log.info("End test: создать предмет, передается пустое поле name, " +
                "возвращается ответ: HttpStatus.BAD_REQUEST.");
    }
}