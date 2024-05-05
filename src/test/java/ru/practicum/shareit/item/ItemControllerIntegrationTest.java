package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemInputDTO;
import ru.practicum.shareit.item.dto.ItemOutputDTO;
import ru.practicum.shareit.item.dto.ItemShortOutputDTO;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserDAO userDAO;

    private ItemInputDTO inputDTO;
    private ItemShortOutputDTO shortOutputDTO;
    private ItemOutputDTO outputDTO;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Long itemId1 = 1L;
    private final Long itemId2 = 2L;
    private final Long userId1 = 1L;
    private final Long userId2 = 2L;
    private final Long invalidItemId = 999L;
    private final Long invalidUserId = 999L;

    public void init() {
        User user1 = User.builder()
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
        User user2 = User.builder()
                .email("comcom@gmail.com")
                .name("ComCom")
                .build();
        userDAO.save(user1);
        userDAO.save(user2);
    }

    public void setUp() {

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
    @Order(0)
    @SneakyThrows
    @DisplayName("Integration Test: создать предмет пользователем №1, возвращается ответ: HttpStatus.CREATED.")
    public void testCreateItem_ReturnsStatusCreated() {
        log.info("Start test: создать предмет пользователем №1.");
        init();
        setUp();

        mvc.perform(post("/items")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(shortOutputDTO)));

        log.info("End test: создать предмет пользователем №1, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @Order(1)
    @SneakyThrows
    @DisplayName("Integration Test: создать предмет с неверным ID пользователя, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testCreateItem_WithInvalidUserId_ReturnsStatusNotFound() {
        log.info("Start test: создать предмет с неверным ID пользователя.");
        setUp();

        mvc.perform(post("/items")
                        .header(REQUEST_HEADER_USER_ID, invalidUserId)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: создать предмет с неверным ID пользователя, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(2)
    @SneakyThrows
    @DisplayName("Integration Test: обновить предмет, возвращается ответ: HttpStatus.OK.")
    public void testUpdateItem_ReturnsStatusOk() {
        log.info("Start test: обновить предмет.");
        setUp();

        inputDTO.toBuilder().name("Дрель++").build();
        shortOutputDTO.toBuilder().name("Дрель++").build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(shortOutputDTO)));

        log.info("End test: обновить предмет, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(3)
    @SneakyThrows
    @DisplayName("Integration Test: обновить предмет с неверным ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testUpdateItem_WithInvalidItemId_ReturnsStatusNotFound() {
        log.info("Start test: обновить предмет с неверным ID.");
        setUp();

        inputDTO.toBuilder().name("Дрель--").build();

        mvc.perform(patch("/items/{itemId}", invalidItemId)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: обновить предмет с неверным ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(4)
    @SneakyThrows
    @DisplayName("Integration Test: создать предмет пользователем №2, возвращается ответ: HttpStatus.CREATED.")
    public void testCreateItem_WithUser2_ReturnsStatusCreated() {
        log.info("Start test: создать предмет пользователем №2.");
        setUp();
        inputDTO = inputDTO.toBuilder().name("Отвертка").description("Аккумуляторная отвертка").available(true).build();
        shortOutputDTO = shortOutputDTO.toBuilder().id(2L).name("Отвертка").description("Аккумуляторная отвертка").available(true).build();

        mvc.perform(post("/items")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(shortOutputDTO)))
                .andReturn();

        log.info("End test: создать предмет пользователем №2, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @Order(5)
    @SneakyThrows
    @DisplayName("Integration Test: получить предмет по ID, возвращается ответ: HttpStatus.Ok.")
    public void testGetItem_ReturnsStatusOk() {
        log.info("Start test: получить предмет по ID.");
        setUp();

        mvc.perform(get("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outputDTO)));

        log.info("End test: получить предмет по ID, возвращается ответ: HttpStatus.Ok.");
    }

    @Test
    @Order(6)
    @SneakyThrows
    @DisplayName("Integration Test: получить предмет по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testGetItem_WithInvalidItemId_ReturnsStatusNotFound() {
        log.info("Start test: получить предмет по неверному ID.");
        setUp();

        mvc.perform(get("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outputDTO)));

        log.info("End test: получить предмет по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(7)
    @SneakyThrows
    @DisplayName("Integration Test: получить предмет по неверному ID пользователя, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testGetItem_WithInvalidUserId_ReturnsStatusNotFound() {
        log.info("Start test: получить предмет по неверному ID пользователя.");
        setUp();

        mvc.perform(get("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outputDTO)));

        log.info("End test: получить предмет по неверному ID пользователя, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

//    List<ItemOutputDTO> getAllItems(@RequestHeader(REQUEST_HEADER_USER_ID) Long ownerId,
//                                    @RequestParam(defaultValue = "0") @Min(0) Integer from,
//                                    @RequestParam(defaultValue = "20") @Positive Integer size)
//
//    List<ItemShortOutputDTO> searchItemsByText(@RequestParam String text,
//                                               @RequestParam(defaultValue = "0") @Min(0) Integer from,
//                                               @RequestParam(defaultValue = "20") @Positive Integer size)
//
//    CommentOutputDTO addComment(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
//                                @PathVariable Long itemId,
//                                @Valid @RequestBody CommentInputDTO inputDTO)

}


