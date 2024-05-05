package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserInputDTO;
import ru.practicum.shareit.user.dto.UserOutputDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    private User user;
    private UserInputDTO userInputDTO;
    private UserOutputDTO userOutputDTO;
    private final Long userId = 1L;
    private final Long invalidUserId = 999L;
    private final NotFoundException notFoundException = NotFoundException.builder().message("Exception").build();

    void setUp() {
        user = User.builder()
                .id(userId)
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
        userInputDTO = UserInputDTO.builder()
                .id(null)
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
        userOutputDTO = UserOutputDTO.builder()
                .id(userId)
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать пользователя, передается пустое поле name, " +
            "возвращается ответ: HttpStatus.BAD_REQUEST.")
    void testCreateUser_WithEmptyName_ResultException() {

        log.info("Start test: создания пользователя, передается пустое поле name.");
        setUp();
        userInputDTO = userInputDTO.toBuilder()
                .name(null)
                .build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ConstraintViolationException.class));

        verify(userService, never()).create(userInputDTO);

        log.info("End test: создать пользователя, передается пустое поле name, " +
                "возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать пользователя, передается невалидное поле email, " +
            "возвращается ответ: HttpStatus.BAD_REQUEST.")
    void testCreateUser_WithInvalidEmail_ResultException() {

        log.info("Start test: создать пользователя, передается невалидное поле email.");
        setUp();
        userInputDTO = userInputDTO.toBuilder()
                .email("ruru@yandex")
                .build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ConstraintViolationException.class));

        verify(userService, never()).create(userInputDTO);

        log.info("End test: создать пользователя, передается невалидное поле email, " +
                "возвращается ответ: HttpStatus.BAD_REQUEST.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать пользователя, возвращается ответ: HttpStatus.CREATED.")
    void testCreateUser_ResultCreated() {

        log.info("Start test: создать пользователя.");
        setUp();

        when(userService.create(any(UserInputDTO.class))).thenReturn(userOutputDTO);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(userOutputDTO)));

        verify(userService, times(1)).create(any(UserInputDTO.class));

        log.info("End test: создать пользователя, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить пользователя по ID, возвращается ответ: HttpStatus.OK.")
    void testGetUser_ById_ResultOk() {

        log.info("Start test: получить пользователя по ID.");
        setUp();

        when(userService.getById(anyLong())).thenReturn(userOutputDTO);

        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userOutputDTO)));

        verify(userService, times(1)).getById(anyLong());

        log.info("End test: получить пользователя по ID, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    void testGetUser_ByInvalidId_ResultNotFound() {

        log.info("Start test: получить пользователя по неверному ID.");
        setUp();

        when(userService.getById(anyLong())).thenThrow(notFoundException);

        mvc.perform(get("/users/{invalidUserId}", invalidUserId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));


        verify(userService, times(1)).getById(invalidUserId);

        log.info("End test: получить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

}