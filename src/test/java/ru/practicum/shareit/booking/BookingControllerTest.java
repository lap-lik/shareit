package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestInputDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutputDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserInputDTO;
import ru.practicum.shareit.user.dto.UserOutputDTO;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService service;

    private User user;
    private UserInputDTO userInputDTO;
    private UserOutputDTO userOutputDTO;

    private Item item;
    private ItemInputDTO itemInputDTO;
    private ItemOutputDTO itemOutputDTO;
    private ItemShortOutputDTO itemShortOutputDTO;

    private Comment comment;
    private CommentInputDTO commentInputDTO;
    private CommentOutputDTO commentOutputDTO;

    private Booking booking;
    private BookingInputDTO bookingInputDTO;
    private BookingOutputDTO bookingOutputDTO;

    private ItemRequest itemRequest;
    private ItemRequestInputDTO itemRequestInputDTO;
    private ItemRequestOutputDTO itemRequestOutputDTO;

    private final Long userId = 1L;
    private final Long itemId = 1L;
    private final Long commentId = 1L;
    private final Long bookingId = 1L;
    private final Long itemRequestId = 1L;
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime startTime = now.plusHours(1);
    private final LocalDateTime endTime = now.plusHours(2);

    @BeforeEach
    void setUp() {
        setUpUser();
        setUpItem();
        setUpComment();
        setUpBooking();
        setUpItemRequest();
    }

    void setUpUser() {

        user = User.builder()
                .id(userId)
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();

        userInputDTO = UserInputDTO.builder()
                .id(userId)
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();

        userOutputDTO = UserOutputDTO.builder()
                .id(userId)
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
    }

    void setUpItem() {

        item = Item.builder()
                .id(itemId)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .owner(user)
                .request(itemRequest)
                .build();

        itemInputDTO = ItemInputDTO.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();

        itemOutputDTO = ItemOutputDTO.builder()
                .id(itemId)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .comments(List.of())
                .build();

        itemShortOutputDTO = ItemShortOutputDTO.builder()
                .id(itemId)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();
    }

    void setUpComment() {

        comment = Comment.builder()
                .id(commentId)
                .text("Add comment from user1")
                .item(item)
                .author(user)
                .created(startTime)
                .build();

        commentInputDTO = CommentInputDTO.builder()
                .text("Add comment from user1")
                .itemId(itemId)
                .authorId(userId)
                .created(startTime)
                .build();

        commentOutputDTO = CommentOutputDTO.builder()
                .id(commentId)
                .text("Add comment from user1")
                .authorName("RuRu")
                .created(startTime)
                .itemId(itemId)
                .build();
    }

    void setUpItemRequest() {

        itemRequest = ItemRequest.builder()
                .id(itemRequestId)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(startTime)
                .requester(user)
                .build();

        itemRequestInputDTO = ItemRequestInputDTO.builder()
                .id(itemRequestId)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requesterId(userId)
                .build();

        itemRequestOutputDTO = ItemRequestOutputDTO.builder()
                .id(itemRequestId)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requesterId(userId)
                .created(startTime)
                .items(List.of())
                .build();
    }

    void setUpBooking() {

        booking = Booking.builder()
                .id(bookingId)
                .start(startTime)
                .end(endTime)
                .status(Status.WAITING)
                .booker(user)
                .item(item)
                .build();

        bookingInputDTO = BookingInputDTO.builder()
                .id(bookingId)
                .start(startTime)
                .end(endTime)
                .status(Status.WAITING)
                .bookerId(userId)
                .itemId(itemId)
                .build();

        bookingOutputDTO = BookingOutputDTO.builder()
                .id(bookingId)
                .start(startTime)
                .end(endTime)
                .status(Status.WAITING)
                .booker(userOutputDTO)
                .item(itemShortOutputDTO)
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать резервирование предмета, передается пустое поле start, " +
            "возвращается ответ: HttpStatus.BAD_REQUEST.")
    void testCreateBooking_WithStartTimeEmpty_ResultStatusBadRequest() {

        log.info("Start test: создать резервирование предмета, передается пустое поле start.");

        bookingInputDTO = bookingInputDTO.toBuilder()
                .start(null)
                .build();

        mvc.perform(post("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        MethodArgumentNotValidException.class));

        verify(service, never()).create(any(BookingInputDTO.class));

        log.info("End test: создать резервирование предмета, передается пустое поле start, " +
                "возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать резервирование предмета, передается неверное поле start, " +
            "возвращается ответ: HttpStatus.BAD_REQUEST.")
    void testCreateBooking_WithStartTimeInvalid_ResultStatusBadRequest() {

        log.info("Start test: создать резервирование предмета, передается неверное поле start.");

        bookingInputDTO = bookingInputDTO.toBuilder()
                .start(LocalDateTime.now().minusDays(1))
                .build();

        mvc.perform(post("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        MethodArgumentNotValidException.class));

        verify(service, never()).create(any(BookingInputDTO.class));

        log.info("End test: создать резервирование предмета, передается неверное поле start, " +
                "возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать резервирование предмета, возвращается ответ: HttpStatus.CREATED.")
    void testCreateBooking_ResultStatusCreated() {

        log.info("Start test: создать резервирование предмета.");

        when(service.create(any(BookingInputDTO.class))).thenReturn(bookingOutputDTO);

        mvc.perform(post("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(bookingOutputDTO)));

        verify(service, times(1)).create(any(BookingInputDTO.class));

        log.info("End test: создать резервирование предмета, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: обновить резервирование предмета, возвращается ответ: HttpStatus.OK.")
    void testUpdateBooking_ResultStatusOk() {

        log.info("Start test: обновить резервирование предмета.");

        bookingOutputDTO = bookingOutputDTO.toBuilder()
                .status(Status.APPROVED)
                .build();

        when(service.approveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingOutputDTO);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header(REQUEST_HEADER_USER_ID, userId)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingOutputDTO)));

        verify(service, times(1)).approveBooking(userId, bookingId, true);

        log.info("End test: обновить резервирование предмета, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить резервирование предмета, возвращается ответ: HttpStatus.OK.")
    void testGetBooking_ResultStatusOk() {

        log.info("Start test: получить резервирование предмета.");

        bookingOutputDTO = bookingOutputDTO.toBuilder()
                .status(Status.APPROVED)
                .build();

        when(service.getById(anyLong(), anyLong())).thenReturn(bookingOutputDTO);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(REQUEST_HEADER_USER_ID, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingOutputDTO)));

        verify(service, times(1)).getById(userId, bookingId);

        log.info("End test: получить резервирование предмета, возвращается ответ: HttpStatus.OK.");
    }
}