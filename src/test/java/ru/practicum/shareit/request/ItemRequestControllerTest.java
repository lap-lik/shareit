package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestInputDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutputDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService service;

    private ItemRequestInputDTO itemRequestInputDTO;
    private ItemRequestOutputDTO itemRequestOutputDTO;
    private final Long userId = 1L;
    private final Long requestId = 1L;
    private final Long invalidId = 999L;
    private final int from = 0;
    private final int size = 2;
    private final LocalDateTime now = LocalDateTime.now();
    private final NotFoundException notFoundException = NotFoundException.builder().message("Exception").build();

    void setUp() {

        itemRequestInputDTO = ItemRequestInputDTO.builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .build();

        itemRequestOutputDTO = ItemRequestOutputDTO.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(now)
                .items(new ArrayList<>())
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать запрос на предмет, возвращается ответ: HttpStatus.CREATED.")
    void testCreateItem_ResulStatusCreated() {

        log.info("Start test: создать запрос на предмет.");
        setUp();

        when(service.create(anyLong(), any(ItemRequestInputDTO.class))).thenReturn(itemRequestOutputDTO);

        mvc.perform(post("/requests")
                        .header(REQUEST_HEADER_USER_ID, userId)
                        .content(mapper.writeValueAsString(itemRequestInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestOutputDTO)));

        verify(service, times(1)).create(anyLong(), any(ItemRequestInputDTO.class));

        log.info("End test: создать запрос на предмет, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать запрос на предмет, передается пустое поле description, " +
            "возвращается ответ: HttpStatus.BAD_REQUEST.")
    void testCreateItem_WithEmptyDescription_ResulStatusBadRequest() {

        log.info("Start test: создать запрос на предмет, передается пустое поле description.");
        setUp();
        itemRequestInputDTO = itemRequestInputDTO.toBuilder()
                .description(null)
                .build();

        mvc.perform(post("/requests")
                        .header(REQUEST_HEADER_USER_ID, userId)
                        .content(mapper.writeValueAsString(itemRequestInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ConstraintViolationException.class));

        verify(service, never()).create(anyLong(), any(ItemRequestInputDTO.class));

        log.info("End test: создать запрос на предмет, передается пустое поле description, " +
                "возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить запрос на предмет по ID, возвращается ответ: HttpStatus.OK.")
    void testGetItemRequest_ById_ResultStatusOk() {

        log.info("Start test: получить запрос на предмет по ID.");
        setUp();

        when(service.getByRequestId(anyLong(), anyLong())).thenReturn(itemRequestOutputDTO);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header(REQUEST_HEADER_USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestOutputDTO)));

        verify(service, times(1)).getByRequestId(anyLong(), anyLong());

        log.info("End test: получить запрос на предмет по ID, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить запрос на предмет по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    void testGetItemRequest_ByInvalidId_ResultStatusNotFound() {

        log.info("Start test: получить запрос на предмет по неверному ID.");
        setUp();

        when(service.getByRequestId(anyLong(), anyLong())).thenThrow(notFoundException);

        mvc.perform(get("/requests/{requestId}", invalidId)
                        .header(REQUEST_HEADER_USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        verify(service, times(1)).getByRequestId(anyLong(), anyLong());

        log.info("End test: получить запрос на предмет по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить запрос на предмет по неверному ID пользователя, возвращается ответ: HttpStatus.NOT_FOUND.")
    void testGetItemRequest_ByInvalidUserId_ResultStatusNotFound() {

        log.info("Start test: получить запрос на предмет по неверному ID пользователя.");
        setUp();

        when(service.getByRequestId(anyLong(), anyLong())).thenThrow(notFoundException);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header(REQUEST_HEADER_USER_ID, invalidId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        verify(service, times(1)).getByRequestId(anyLong(), anyLong());

        log.info("End test: получить запрос на предмет по неверному ID пользователя, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить все запросы на предметы, возвращается ответ: HttpStatus.OK.")
    void testGetAllItemRequest_ResultStatusOk() {

        log.info("Start test: получить всех пользователей.");


        ItemRequestOutputDTO itemRequestOutputDTO1 = ItemRequestOutputDTO.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(now.minusHours(1))
                .items(new ArrayList<>())
                .build();

        ItemRequestOutputDTO itemRequestOutputDTO2 = ItemRequestOutputDTO.builder()
                .id(2L)
                .description("Нужна отвертка")
                .created(now)
                .items(new ArrayList<>())
                .build();

        List<ItemRequestOutputDTO> itemRequests = Arrays.asList(itemRequestOutputDTO1, itemRequestOutputDTO2);

        when(service.getAll(anyLong(), anyInt(), anyInt())).thenReturn(itemRequests);

        mvc.perform(get("/requests/all")
                        .header(REQUEST_HEADER_USER_ID, invalidId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequests)));

        verify(service, times(1)).getAll(anyLong(), anyInt(), anyInt());

        log.info("End test: получить все запросы на предметы, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить все запросы на предметы, передается отрицательное количество объектов для " +
            "вывода на странице, возвращается ответ: HttpStatus.BAD_REQUEST.")
    void testGetAllItemRequest_InvalidPageSizeParam_ResultStatusBadRequest() {

        log.info("Start test: получить все запросы на предметы, передается отрицательное количество объектов для вывода на странице.");

        mvc.perform(get("/requests/all")
                        .header(REQUEST_HEADER_USER_ID, invalidId)
                        .param("from", String.valueOf(from))
                        .param("size", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ConstraintViolationException.class));

        verify(service, never()).getAll(anyLong(), anyInt(), anyInt());

        log.info("End test: получить все запросы на предметы, передается отрицательное количество объектов для " +
                "вывода на странице, возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить все запросы на предметы по ID создателя запросов, возвращается ответ: HttpStatus.OK.")
    void testGetAllByRequesterId_ResultStatusOk() {

        log.info("Start test: получить все запросы на предметы по ID создателя запросов.");


        ItemRequestOutputDTO itemRequestOutputDTO1 = ItemRequestOutputDTO.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(now.minusHours(1))
                .items(new ArrayList<>())
                .build();

        ItemRequestOutputDTO itemRequestOutputDTO2 = ItemRequestOutputDTO.builder()
                .id(2L)
                .description("Нужна отвертка")
                .created(now)
                .items(new ArrayList<>())
                .build();

        List<ItemRequestOutputDTO> itemRequests = Arrays.asList(itemRequestOutputDTO1, itemRequestOutputDTO2);

        when(service.getAllByRequesterId(anyLong())).thenReturn(itemRequests);

        mvc.perform(get("/requests")
                        .header(REQUEST_HEADER_USER_ID, invalidId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequests)));

        verify(service, times(1)).getAllByRequesterId(anyLong());

        log.info("End test: получить все запросы на предметы по ID создателя запросов, возвращается ответ: HttpStatus.OK.");
    }
}