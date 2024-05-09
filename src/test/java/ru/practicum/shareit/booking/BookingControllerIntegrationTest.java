package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dao.BookingDAO;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemShortOutputDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserOutputDTO;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private BookingDAO bookingDAO;

    @Autowired
    private ItemDAO itemDAO;
    private final ObjectMapper mapper = new ObjectMapper();

    private User user1;
    private User user2;
    private UserOutputDTO userOutputDTO1;
    private UserOutputDTO userOutputDTO2;

    private Item item1;
    private Item item2;
    private Item item3;
    private ItemShortOutputDTO itemShortOutputDTO1;
    private ItemShortOutputDTO itemShortOutputDTO2;


    private Booking booking;
    private BookingInputDTO bookingInputDTO;
    private BookingOutputDTO bookingOutputDTO;


    private final Long userId1 = 1L;
    private final Long userId2 = 2L;
    private final Long itemId1 = 1L;
    private final Long itemId2 = 2L;
    private final Long itemId3 = 3L;
    private final Long bookingId1 = 1L;
    private final Long invalidId = 999L;
    private final int from = 0;
    private final int size = 2;
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime startTime = now.plusHours(1);
    private final LocalDateTime endTime = now.plusHours(2);


    public void init() {
        userDAO.save(user1);
        userDAO.save(user2);
        itemDAO.save(item1);
        itemDAO.save(item2);
        itemDAO.save(item3);
    }

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        user1 = User.builder()
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
        userOutputDTO1 = UserOutputDTO.builder()
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();

        user2 = User.builder()
                .email("comcom@gmail.com")
                .name("ComCom")
                .build();
        userOutputDTO2 = UserOutputDTO.builder()
                .id(userId2)
                .email("comcom@gmail.com")
                .name("ComCom")
                .build();

        item1 = Item.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .owner(user1.toBuilder().id(1L).build())
                .build();
        itemShortOutputDTO1 = ItemShortOutputDTO.builder()
                .id(itemId1)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();

        item2 = Item.builder()
                .name("Отвертка")
                .description("Крестовая отвертка")
                .available(true)
                .owner(user2.toBuilder().id(2L).build())
                .build();
        itemShortOutputDTO2 = ItemShortOutputDTO.builder()
                .id(itemId2)
                .name("Отвертка")
                .description("Крестовая отвертка")
                .available(true)
                .build();

        item3 = Item.builder()
                .name("Стул")
                .description("Пластиковый стул")
                .available(false)
                .owner(user1.toBuilder().id(1L).build())
                .build();

        booking = Booking.builder()
                .start(startTime)
                .end(endTime)
                .status(Status.APPROVED)
                .booker(user1.toBuilder().id(1L).build())
                .item(item2.toBuilder().id(2L).build())
                .build();

        bookingInputDTO = BookingInputDTO.builder()
                .start(startTime)
                .end(endTime)
                .itemId(itemId2)
                .build();

        bookingOutputDTO = BookingOutputDTO.builder()
                .id(bookingId1)
                .start(startTime)
                .end(endTime)
                .status(Status.WAITING)
                .booker(userOutputDTO1)
                .item(itemShortOutputDTO2)
                .build();
    }

    @Test
    @Order(0)
    @SneakyThrows
    @DisplayName("Integration Test: создать резервирование предмета, возвращается ответ: HttpStatus.CREATED.")
    public void testCreateBooking_ResulStatusCreated() {
        log.info("Start test: создать резервирование предмета.");
        init();

        mvc.perform(post("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(bookingOutputDTO.getId()), Long.class))
                .andReturn();


        log.info("End test: создать резервирование предмета, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @Order(1)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предмета по ID, возвращается ответ: HttpStatus.OK.")
    public void testGetBookingById_ResulStatusOk() {
        log.info("Start test: получить пользователя по ID.");

        mvc.perform(get("/bookings/{bookingId}", bookingId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(bookingOutputDTO.getId()), Long.class))
                .andReturn();

        log.info("End test: получить пользователя по ID, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(2)
    @SneakyThrows
    @DisplayName("Integration Test: обновить резервирование предмета, передается ID несуществующего пользователя, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testUpdateBooking_WithInvalidUserId_ResulStatusNotFound() {
        log.info("Start test: обновить резервирование предмета, передается ID несуществующего пользователя.");

        mvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(REQUEST_HEADER_USER_ID, invalidId)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: обновить резервирование предмета, передается ID несуществующего пользователя, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(3)
    @SneakyThrows
    @DisplayName("Integration Test: обновить резервирование предмета, передается ID несуществующего бронирования, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testUpdateBooking_WithInvalidBookingId_ResulStatusNotFound() {
        log.info("Start test: обновить резервирование предмета, передается ID несуществующего бронирования.");

        mvc.perform(patch("/bookings/{bookingId}", invalidId)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: обновить резервирование предмета, передается ID несуществующего бронирования, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(4)
    @SneakyThrows
    @DisplayName("Integration Test: обновить резервирование предмета, передается ID пользователя не являющегося создателем предмета, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testUpdateBooking_WithUserNotItemOwner_ResulStatusNotFound() {
        log.info("Start test: обновить резервирование предмета, передается ID пользователя не являющегося создателем предмета.");

        mvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: обновить резервирование предмета, передается ID пользователя не являющегося создателем предмета, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(5)
    @SneakyThrows
    @DisplayName("Integration Test: обновить резервирование предмета, отклонение бронирования, возвращается ответ: HttpStatus.OK.")
    public void testUpdateBooking_WithRejected_ResulStatusOk() {
        log.info("Start test: обновить резервирование предмета, отклонение бронирования.");

        bookingOutputDTO = bookingOutputDTO.toBuilder()
                .status(Status.REJECTED)
                .build();

        mvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("approved", "false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(bookingOutputDTO.getId()), Long.class))
                .andExpect(jsonPath("status", is(bookingOutputDTO.getStatus().toString()), String.class))
                .andReturn();

        log.info("End test: обновить резервирование предмета, отклонение бронирования, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(6)
    @SneakyThrows
    @DisplayName("Integration Test: обновить резервирование предмета, подтверждение бронирования, возвращается ответ: HttpStatus.OK.")
    public void testUpdateBooking_WithApproved_ResulStatusOk() {
        log.info("Start test: обновить резервирование предмета, подтверждение бронирования.");

        bookingOutputDTO = bookingOutputDTO.toBuilder()
                .status(Status.APPROVED)
                .build();

        mvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(bookingOutputDTO.getId()), Long.class))
                .andExpect(jsonPath("status", is(bookingOutputDTO.getStatus().toString()), String.class))
                .andReturn();

        log.info("End test: обновить резервирование предмета, подтверждение бронирования, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(7)
    @SneakyThrows
    @DisplayName("Integration Test: обновить резервирование предмета, отклонение бронирования после подтверждения, возвращается ответ: HttpStatus.BAD_REQUEST.")
    public void testUpdateBooking_WithRejectedAfterApproved_ResulStatusBadRequest() {
        log.info("Start test: обновить резервирование предмета, отклонение бронирования после подтверждения.");

        mvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("approved", "false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ValidException.class));

        log.info("End test: обновить резервирование предмета, отклонение бронирования после подтверждения, возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @Order(8)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, все от создателя бронирований, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserBookerAndStateAll_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, все от создателя бронирований.");

        bookingOutputDTO = BookingOutputDTO.builder()
                .id(bookingId1)
                .build();
        List<BookingOutputDTO> bookings = Collections.singletonList(bookingOutputDTO);

        mvc.perform(get("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));

        log.info("End test: получить резервирование предметов, все от создателя бронирований, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(9)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, действующие от создателя бронирований, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserBookerAndStateCurrent_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, действующие от создателя бронирований.");

        mvc.perform(get("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .param("state", "CURRENT")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));

        log.info("End test: получить резервирование предметов, действующие от создателя бронирований, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(10)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, прошедшие от создателя бронирований, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserBookerAndStatePast_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, прошедшие от создателя бронирований.");

        mvc.perform(get("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .param("state", "PAST")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));

        log.info("End test: получить резервирование предметов, прошедшие от создателя бронирований, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(11)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, будущие от создателя бронирований, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserBookerAndStateFuture_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, будущие от создателя бронирований.");

        bookingOutputDTO = BookingOutputDTO.builder()
                .id(bookingId1)
                .build();
        List<BookingOutputDTO> bookings = Collections.singletonList(bookingOutputDTO);

        mvc.perform(get("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .param("state", "FUTURE")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));

        log.info("End test: получить резервирование предметов, будущие от создателя бронирований, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(12)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, не подтвержденные от создателя бронирований, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserBookerAndStateWaiting_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, не подтвержденные от создателя бронирований.");

        mvc.perform(get("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .param("state", "WAITING")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));

        log.info("End test: получить резервирование предметов, не подтвержденные от создателя бронирований, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(13)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, отклоненные от создателя бронирований, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserBookerAndStateRejected_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, отклоненные от создателя бронирований.");

        mvc.perform(get("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .param("state", "REJECTED")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));

        log.info("End test: получить резервирование предметов, отклоненные от создателя бронирований, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(14)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, с несуществующим статусом от создателя бронирований, возвращается ответ: HttpStatus.BAD_REQUEST.")
    public void testGetAllBookings_WithUserBookerAndStateUnknown_ResulStatusBadRequest() {
        log.info("Start test: получить резервирование предметов, с несуществующим статусом от создателя бронирований.");

        mvc.perform(get("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .param("state", "Unknown")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        UnsupportedException.class));

        log.info("End test: получить резервирование предметов, с несуществующим статусом от создателя бронирований, возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @Order(15)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предмета по несуществующему ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testGetBookingById_WithInvalidBookingId_ResulStatusNotFound() {
        log.info("Start test: получить пользователя по несуществующему ID.");

        mvc.perform(get("/bookings/{bookingId}", invalidId)
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: получить пользователя по несуществующему ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }


    @Test
    @Order(16)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, все от создателя предметов, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserItemOwner_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, все от создателя предметов.");

        bookingOutputDTO = BookingOutputDTO.builder()
                .id(bookingId1)
                .build();
        List<BookingOutputDTO> bookings = Collections.singletonList(bookingOutputDTO);

        mvc.perform(get("/bookings/owner")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("state", "ALL")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));

        log.info("End test: получить резервирование предметов, все от создателя предметов, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(17)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, действующие от создателя предметов, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserItemOwnerAndStateCurrent_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, действующие от создателя предметов.");

        mvc.perform(get("/bookings/owner")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("state", "CURRENT")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));

        log.info("End test: получить резервирование предметов, действующие от создателя предметов, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(18)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, прошедшие от создателя предметов, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserItemOwnerAndStatePast_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, прошедшие от создателя предметов.");

        mvc.perform(get("/bookings/owner")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("state", "PAST")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));

        log.info("End test: получить резервирование предметов, прошедшие от создателя предметов, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(19)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, будущие от создателя предметов, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserItemOwnerAndStateFuture_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, будущие от создателя предметов.");

        bookingOutputDTO = BookingOutputDTO.builder()
                .id(bookingId1)
                .build();
        List<BookingOutputDTO> bookings = Collections.singletonList(bookingOutputDTO);

        mvc.perform(get("/bookings/owner")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("state", "FUTURE")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));

        log.info("End test: получить резервирование предметов, будущие от создателя предметов, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(20)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, не подтвержденные от создателя предметов, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserItemOwnerAndStateWaiting_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, не подтвержденные от создателя предметов.");

        mvc.perform(get("/bookings/owner")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("state", "WAITING")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));

        log.info("End test: получить резервирование предметов, не подтвержденные от создателя предметов, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(21)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, отклоненные от создателя предметов, возвращается ответ: HttpStatus.OK.")
    public void testGetAllBookings_WithUserItemOwnerAndStateRejected_ResulStatusOk() {
        log.info("Start test: получить резервирование предметов, отклоненные от создателя предметов.");

        mvc.perform(get("/bookings/owner")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("state", "REJECTED")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));

        log.info("End test: получить резервирование предметов, отклоненные от создателя предметов, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(22)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, с несуществующим статусом от создателя предметов, возвращается ответ: HttpStatus.BAD_REQUEST.")
    public void testGetAllBookings_WithUserItemOwnerAndStateUnknown_ResulStatusBadRequest() {
        log.info("Start test: получить резервирование предметов, с несуществующим статусом от создателя предметов.");

        mvc.perform(get("/bookings/owner")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .param("state", "Unknown")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        UnsupportedException.class));

        log.info("End test: получить резервирование предметов, с несуществующим статусом от создателя предметов, возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @Order(23)
    @SneakyThrows
    @DisplayName("Integration Test: получить резервирование предметов, все от создателя предметов несуществующего ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testGetAllBookings_WithUserItemOwnerInvalidId_ResulStatusNotFound() {
        log.info("Start test: получить резервирование предметов, все от создателя предметов несуществующего ID.");

        mvc.perform(get("/bookings/owner")
                        .header(REQUEST_HEADER_USER_ID, invalidId)
                        .param("state", "ALL")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: получить резервирование предметов, все от создателя предметов несуществующего ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(24)
    @SneakyThrows
    @DisplayName("Integration Test: создать резервирование предмета, от создателя предмета, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testCreateBooking_WithUserItemBooker_ResulStatusNotFound() {
        log.info("Start test: создать резервирование предмета, от создателя предмета.");

        bookingInputDTO = BookingInputDTO.builder()
                .start(startTime)
                .end(endTime)
                .itemId(itemId1)
                .build();

        mvc.perform(post("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: создать резервирование предмета, от создателя предмета, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(25)
    @SneakyThrows
    @DisplayName("Integration Test: создать резервирование предмета, по неправильному ID редмета, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testCreateBooking_WithItemIdInvalid_ResulStatusNotFound() {
        log.info("Start test: создать резервирование предмета, по неправильному ID редмета.");

        bookingInputDTO = BookingInputDTO.builder()
                .start(startTime)
                .end(endTime)
                .itemId(invalidId)
                .build();

        mvc.perform(post("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: создать резервирование предмета, по неправильному ID редмета, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(26)
    @SneakyThrows
    @DisplayName("Integration Test: создать резервирование предмета, начала аренды позже завершения, возвращается ответ: HttpStatus.BAD_REQUEST.")
    public void testCreateBooking_WithStartAfterEndTime_ResulStatusBadRequest() {
        log.info("Start test: создать резервирование предмета, начала аренды позже завершения.");

        bookingInputDTO = BookingInputDTO.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(itemId1)
                .build();

        mvc.perform(post("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ValidException.class));

        log.info("End test: создать резервирование предмета, начала аренды позже завершения, возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @Order(27)
    @SneakyThrows
    @DisplayName("Integration Test: создать резервирование предмета, начала аренды совпадает с завершением, возвращается ответ: HttpStatus.BAD_REQUEST.")
    public void testCreateBooking_WithStartEqualsEndTime_ResulStatusBadRequest() {
        log.info("Start test: создать резервирование предмета, начала аренды совпадает с завершением.");

        LocalDateTime startAndEndTime = LocalDateTime.now().plusDays(1);
        bookingInputDTO = BookingInputDTO.builder()
                .start(startAndEndTime)
                .end(startAndEndTime)
                .itemId(itemId1)
                .build();

        mvc.perform(post("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ValidException.class));

        log.info("End test: создать резервирование предмета, начала аренды совпадает с завершением, возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @Order(28)
    @SneakyThrows
    @DisplayName("Integration Test: создать резервирование предмета, с недоступным к бронированию предметом, возвращается ответ: HttpStatus.BAD_REQUEST.")
    public void testCreateBooking_WithItemStatusNotAvailable_ResulStatusBadRequest() {
        log.info("Start test: создать резервирование предмета, с недоступным к бронированию предметом.");

        bookingInputDTO = BookingInputDTO.builder()
                .start(startTime)
                .end(endTime)
                .itemId(itemId3)
                .build();

        mvc.perform(post("/bookings")
                        .header(REQUEST_HEADER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ValidException.class));

        log.info("End test: создать резервирование предмета, с недоступным к бронированию предметом, возвращается ответ: HttpStatus.BAD_REQUEST.");
    }
}
