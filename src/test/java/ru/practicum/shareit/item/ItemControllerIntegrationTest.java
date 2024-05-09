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
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.dao.ItemRequestDAO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
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

    @Autowired
    private ItemRequestDAO itemRequestDAO;

    @Autowired
    private BookingService bookingService;

    private ItemInputDTO inputDTO;
    private ItemShortOutputDTO shortOutputDTO;
    private ItemOutputDTO outputDTO;
    private User user1;
    private User user2;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Long itemId1 = 1L;
    private final Long userId1 = 1L;
    private final Long userId2 = 2L;
    private final Long invalidId = 999L;
    private final int from = 0;
    private final int size = 2;

    public void init() {
        user1 = User.builder()
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
        user2 = User.builder()
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
    @DisplayName("Integration Test: создать предмет №1 пользователем №1, возвращается ответ: HttpStatus.CREATED.")
    public void testCreateItem_ReturnsStatusCreated() {
        log.info("Start test: создать предмет №1 пользователем №1.");
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

        log.info("End test: создать предмет №1 пользователем №1, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @Order(1)
    @SneakyThrows
    @DisplayName("Integration Test: создать предмет с неверным ID пользователя, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testCreateItem_WithInvalidUserId_ReturnsStatusNotFound() {
        log.info("Start test: создать предмет с неверным ID пользователя.");
        setUp();

        mvc.perform(post("/items")
                        .header(REQUEST_HEADER_USER_ID, invalidId)
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
    @DisplayName("Integration Test: обновить предмет с неверным ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testUpdateItem1_WithInvalidItemId_ReturnsStatusNotFound() {
        log.info("Start test: обновить предмет с неверным ID.");
        setUp();

        inputDTO.toBuilder().name("Дрель--").build();

        mvc.perform(patch("/items/{itemId}", invalidId)
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
    @Order(3)
    @SneakyThrows
    @DisplayName("Integration Test: создать №2 предмет пользователем №2, возвращается ответ: HttpStatus.CREATED.")
    public void testCreateItem2_WithUser2_ReturnsStatusCreated() {
        log.info("Start test: создать №2 предмет пользователем №2.");
        setUp();

        inputDTO = inputDTO.toBuilder()
                .name("Отвертка")
                .description("Аккумуляторная отвертка")
                .available(true)
                .build();
        shortOutputDTO = shortOutputDTO.toBuilder()
                .id(2L)
                .name("Отвертка")
                .description("Аккумуляторная отвертка")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(shortOutputDTO)))
                .andReturn();

        log.info("End test: создать №2 предмет пользователем №2, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @Order(4)
    @SneakyThrows
    @DisplayName("Integration Test: создать предмет №3 пользователем №2, возвращается ответ: HttpStatus.CREATED.")
    public void testCreateItem3_WithUser2_ReturnsStatusCreated() {
        log.info("Start test: создать предмет №3 пользователем №2.");
        setUp();

        inputDTO = inputDTO.toBuilder()
                .name("Клей Момент")
                .description("Тюбик суперклея марки Момент")
                .available(true)
                .build();
        shortOutputDTO = shortOutputDTO.toBuilder()
                .id(3L)
                .name("Клей Момент")
                .description("Тюбик суперклея марки Момент")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(shortOutputDTO)))
                .andReturn();

        log.info("End test: создать предмет №3 пользователем №2, возвращается ответ: HttpStatus.CREATED.");
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
    @DisplayName("Integration Test: получить предмет по ID и не создателем предмета, возвращается ответ: HttpStatus.Ok.")
    public void testGetItem_WithUserIdNotOwner_ReturnsStatusOk() {
        log.info("Start test: получить предмет по ID и не создателем предмета.");
        setUp();

        mvc.perform(get("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outputDTO)));

        log.info("End test: получить предмет по ID и не создателем предмета, возвращается ответ: HttpStatus.Ok.");
    }

    @Test
    @Order(7)
    @SneakyThrows
    @DisplayName("Integration Test: получить предмет по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testGetItem_WithInvalidItemId_ReturnsStatusNotFound() {
        log.info("Start test: получить предмет по неверному ID.");
        setUp();

        mvc.perform(get("/items/{itemId}", invalidId)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: получить предмет по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(8)
    @SneakyThrows
    @DisplayName("Integration Test: получить предмет по неверному ID пользователя, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testGetItem_WithInvalidUserId_ReturnsStatusNotFound() {
        log.info("Start test: получить предмет по неверному ID пользователя.");
        setUp();

        mvc.perform(get("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: получить предмет по неверному ID пользователя, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(9)
    @SneakyThrows
    @DisplayName("Integration Test: получить все предметы по ID владельца предметов, возвращается ответ: HttpStatus.OK.")
    public void testGetAllItems_WithOwnerId_ReturnsStatusOk() {
        log.info("Start test: получить все предметы по ID владельца предметов.");

        ItemOutputDTO itemOutputDTO1 = ItemOutputDTO.builder()
                .id(2L)
                .name("Отвертка")
                .description("Аккумуляторная отвертка")
                .available(true)
                .build();

        ItemOutputDTO itemOutputDTO2 = ItemOutputDTO.builder()
                .id(3L)
                .name("Клей Момент")
                .description("Тюбик суперклея марки Момент")
                .available(true)
                .build();
        List<ItemOutputDTO> items = Arrays.asList(itemOutputDTO1, itemOutputDTO2);

        mvc.perform(get("/items")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(items)));

        log.info("End test: получить все предметы по ID владельца предметов, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(10)
    @SneakyThrows
    @DisplayName("Integration Test: найти все предметы по содержанию текста, возвращается ответ: HttpStatus.OK.")
    public void testSearchAllItems_WithText_ReturnsStatusOk() {
        log.info("Start test: найти все предметы по содержанию текста.");

        ItemShortOutputDTO itemOutputDTO = ItemShortOutputDTO.builder()
                .id(2L)
                .name("Отвертка")
                .description("Аккумуляторная отвертка")
                .available(true)
                .build();
        String text = "отвер";

        List<ItemShortOutputDTO> items = Collections.singletonList(itemOutputDTO);

        mvc.perform(get("/items/search")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(items)));

        log.info("End test: найти все предметы по содержанию текста, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(11)
    @SneakyThrows
    @DisplayName("Integration Test: создать комментарий, возвращается ответ: HttpStatus.OK.")
    public void testAddComment_ReturnsStatusOk() {
        log.info("Start test: создать комментарий.");
        setUp();
        LocalDateTime now = LocalDateTime.now();

        BookingInputDTO bookingInputDTO = BookingInputDTO.builder()
                .start(now.minusHours(2))
                .end(now.minusHours(1))
                .status(APPROVED)
                .bookerId(userId2)
                .itemId(itemId1)
                .build();
        bookingService.create(bookingInputDTO);
        bookingService.approveBooking(userId1, itemId1, true);

        CommentInputDTO commentInputDTO = CommentInputDTO.builder()
                .text("Add comment from user1")
                .build();

        CommentOutputDTO commentOutputDTO = CommentOutputDTO.builder()
                .id(1L)
                .text("Add comment from user1")
                .authorName("ComCom")
                .created(now)
                .build();

        mvc.perform(post("/items/{itemId}/comment", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(commentInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentOutputDTO.getText())))
                .andExpect(jsonPath("$.authorName", is(commentOutputDTO.getAuthorName())));

        log.info("End test: создать комментарий, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(12)
    @SneakyThrows
    @DisplayName("Integration Test: обновить предмет, обновляется только название, возвращается ответ: HttpStatus.OK.")
    public void testUpdateItem_OnlyName_ReturnsStatusOk() {
        log.info("Start test: обновить предмет, обновляется только название.");
        setUp();

        ItemInputDTO itemInputDTO = ItemInputDTO.builder()
                .id(itemId1)
                .name("Дрель++")
                .build();
        ItemShortOutputDTO itemShortOutputDTO = ItemShortOutputDTO.builder()
                .id(itemId1)
                .name("Дрель++")
                .available(true)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemShortOutputDTO)));

        log.info("End test: обновить предмет, обновляется только название, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(13)
    @SneakyThrows
    @DisplayName("Integration Test: обновить предмет, обновляется только описание, возвращается ответ: HttpStatus.OK.")
    public void testUpdateItem_OnlyDescription_ReturnsStatusOk() {
        log.info("Start test: обновить предмет, обновляется только описание.");
        setUp();
        ItemInputDTO itemInputDTO = ItemInputDTO.builder()
                .id(itemId1)
                .description("Простая дрель++")
                .build();
        ItemShortOutputDTO itemShortOutputDTO = ItemShortOutputDTO.builder()
                .id(itemId1)
                .description("Простая дрель++")
                .available(true)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemShortOutputDTO)));

        log.info("End test: обновить предмет, обновляется только описание, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(14)
    @SneakyThrows
    @DisplayName("Integration Test: обновить предмет, обновляется только связь с запросом на предмет, передается не правильный ID запроса на предмет, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testUpdateItem_InvalidRequestId_ReturnsStatusNotFound() {
        log.info("Start test: обновить предмет, обновляется только связь с запросом на предмет, передается не правильный ID запроса на предмет.");
        setUp();
        ItemInputDTO itemInputDTO = ItemInputDTO.builder()
                .id(itemId1)
                .requestId(invalidId)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: обновить предмет, обновляется только связь с запросом на предмет, передается не правильный ID запроса на предмет, обновляется только описание, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(15)
    @SneakyThrows
    @DisplayName("Integration Test: создать комментарий, по несуществующему бронированию предмета, возвращается ответ: HttpStatus.BAD_REQUEST.")
    public void testAddComment_WithNotConfirmBooking_ReturnsStatusBadRequest() {
        log.info("Start test: создать комментарий, по несуществующему бронированию предмета.");
        setUp();

        CommentInputDTO commentInputDTO = CommentInputDTO.builder()
                .text("Add comment from user1")
                .build();

        mvc.perform(post("/items/{itemId}/comment", 2L)
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(commentInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ValidException.class));

        log.info("End test: создать комментарий, по несуществующему бронированию предмета, возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @Order(16)
    @SneakyThrows
    @DisplayName("Integration Test: создать предмет, по ID запроса на предмет, возвращается ответ: HttpStatus.OK.")
    public void testCreate_WithItemRequestId_ReturnsStatusOk() {
        log.info("Start test: создать предмет, по ID запроса на предмет.");
        setUp();

        ItemRequest itemRequest = ItemRequest.builder().description("Нужен диван").requester(user2).build();
        itemRequestDAO.save(itemRequest);
        ItemInputDTO itemInputDTO = ItemInputDTO.builder()
                .name("Диван")
                .description("Мягкий диван.")
                .available(true)
                .requestId(1L)
                .build();
        ItemOutputDTO itemOutputDTO = ItemOutputDTO.builder()
                .id(4L)
                .name("Диван")
                .description("Мягкий диван.")
                .available(true)
                .requestId(1L)
                .build();

        mvc.perform(post("/items")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(itemOutputDTO.getId()), Long.class))
                .andExpect(jsonPath("name", is(itemOutputDTO.getName())))
                .andExpect(jsonPath("description", is(itemOutputDTO.getDescription())))
                .andExpect(jsonPath("available", is(itemOutputDTO.isAvailable())));

        log.info("End test: создать предмет, по ID запроса на предмет, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(17)
    @SneakyThrows
    @DisplayName("Integration Test: обновить предмет, обновляется только ID звпроса на предмет, возвращается ответ: HttpStatus.OK.")
    public void testUpdateItem_OnlyItemRequestId_ReturnsStatusOk() {
        log.info("Start test: обновить предмет, обновляется только ID звпроса на предмет.");
        setUp();
        ItemInputDTO itemInputDTO = ItemInputDTO.builder()
                .id(itemId1)
                .requestId(1L)
                .build();
        ItemShortOutputDTO itemShortOutputDTO = ItemShortOutputDTO.builder()
                .id(itemId1)
                .requestId(1L)
                .available(true)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemShortOutputDTO)));

        log.info("End test: обновить предмет, обновляется только ID звпроса на предмет, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(18)
    @SneakyThrows
    @DisplayName("Integration Test: обновить предмет, обновляется только название предмета созданного по запросу, возвращается ответ: HttpStatus.OK.")
    public void testUpdateItem_OnlyNameAndHaveItemRequestId_ReturnsStatusOk() {
        log.info("Start test: обновить предмет, обновляется только название предмета созданного по запросу.");
        setUp();
        ItemInputDTO itemInputDTO = ItemInputDTO.builder()
                .id(itemId1)
                .name("Дрель!!!")
                .build();
        ItemShortOutputDTO itemShortOutputDTO = ItemShortOutputDTO.builder()
                .id(itemId1)
                .name("Дрель!!!")
                .requestId(1L)
                .available(true)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemShortOutputDTO)));

        log.info("End test: обновить предмет, обновляется только название предмета созданного по запросу, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(19)
    @SneakyThrows
    @DisplayName("Integration Test: обновить предмет, обновляет не создатель предмета, передается не правильный ID запроса на предмет, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testUpdateItem_WithNotOwner_ReturnsStatusNotFound() {
        log.info("Start test: обновить предмет, обновляется только связь с запросом на предмет, передается не правильный ID запроса на предмет.");
        setUp();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: обновить предмет, обновляется только связь с запросом на предмет, передается не правильный ID запроса на предмет, обновляется только описание, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(20)
    @SneakyThrows
    @DisplayName("Integration Test: создать комментарий, по несуществующему бронированию предмета, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testAddComment_WithInvalidId_ReturnsStatusNotFound() {
        log.info("Start test: создать комментарий, по несуществующему бронированию предмета.");
        setUp();

        CommentInputDTO commentInputDTO = CommentInputDTO.builder()
                .text("Add comment from user1")
                .build();

        mvc.perform(post("/items/{itemId}/comment", invalidId)
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(commentInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: создать комментарий, по несуществующему бронированию предмета, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(21)
    @SneakyThrows
    @DisplayName("Integration Test: найти все предметы по пустому тексту, возвращается ответ: HttpStatus.OK.")
    public void testSearchAllItems_WithEmptyText_ReturnsStatusOk() {
        log.info("Start test: найти все предметы по пустому тексту.");
        String text = "";

        mvc.perform(get("/items/search")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));

        log.info("End test: найти все предметы по пустому тексту, возвращается ответ: HttpStatus.OK.");
    }
}


