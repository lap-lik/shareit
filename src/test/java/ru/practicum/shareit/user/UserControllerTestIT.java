package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserInputDTO;
import ru.practicum.shareit.user.dto.UserOutputDTO;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserControllerTestIT {

    @Autowired
    private MockMvc mvc;

    private UserInputDTO userInputDTO;
    private UserOutputDTO userOutputDTO;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Long userId1 = 1L;
    private final Long userId2 = 3L;
    private final Long invalidId = 999L;

    public void setUp() {

        userInputDTO = UserInputDTO.builder()
                .email("RuRu@yandex.ru")
                .name("RuRu")
                .build();
        userOutputDTO = UserOutputDTO.builder()
                .id(userId1)
                .email("RuRu@yandex.ru")
                .name("RuRu")
                .build();
    }

    @Test
    @Order(0)
    @SneakyThrows
    @DisplayName("Integration Test: создать пользователя, возвращается ответ: HttpStatus.CREATED.")
    public void testCreateUser_ResulStatusCreated() {
        log.info("Start test: создать пользователя.");
        setUp();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(userOutputDTO)));

        log.info("End test: создать пользователя, возвращается ответ: HttpStatus.CREATED.");
    }


    @Test
    @Order(1)
    @SneakyThrows
    @DisplayName("Integration Test: создать пользователя с существующим email, возвращается ответ: HttpStatus.CONFLICT.")
    public void testCreateUser_WithDuplicateEmail_ResulStatusConflict() {
        log.info("Start test: создать пользователя с существующим email.");
        setUp();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        DataIntegrityViolationException.class));

        log.info("End test: создать пользователя с существующим email, возвращается ответ: HttpStatus.CONFLICT.");
    }

    @Test
    @Order(2)
    @SneakyThrows
    @DisplayName("Integration Test: получить пользователя по ID, возвращается ответ: HttpStatus.OK.")
    public void testGetUserById_ResulStatusOk() {
        log.info("Start test: получить пользователя по ID.");
        setUp();

        mvc.perform(get("/users/{userId}", userId1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userOutputDTO)))
                .andReturn();

        log.info("End test: получить пользователя по ID, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(3)
    @SneakyThrows
    @DisplayName("Integration Test: получить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testGetUserById_WithInvalidId_ResulStatusNotFound() {
        log.info("Start test: получить пользователя по неверному ID.");

        mvc.perform(get("/users/{invalidUserId}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: получить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(4)
    @SneakyThrows
    @DisplayName("Integration Test: обновить пользователя, только поле email, возвращается ответ: HttpStatus.OK.")
    public void testUpdateUser_OnlyEmail_ResulStatusOk() {
        log.info("Start test: обновить пользователя, только поле email.");
        setUp();
        UserInputDTO userInputDTO = UserInputDTO.builder().email("updateRuRu@yandex.ru").build();
        userOutputDTO = userOutputDTO.toBuilder().email("updateRuRu@yandex.ru").build();

        mvc.perform(patch("/users/{userId}", userId1)
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userOutputDTO)));

        log.info("End test: обновить пользователя, только поле email, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(5)
    @SneakyThrows
    @DisplayName("Integration Test: обновить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testUpdateUser_WithInvalidId_ResulStatusNotFound() {
        log.info("Start test: обновить пользователя по неверному ID.");
        setUp();
        userInputDTO = userInputDTO.toBuilder().name("updateRuRu").build();

        mvc.perform(patch("/users/{userId}", invalidId)
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: обновить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @Order(6)
    @SneakyThrows
    @DisplayName("Integration Test: создать пользователя со старым полем email, возвращается ответ: HttpStatus.CREATED.")
    public void testCreateUser_WithOldEmail_ResulStatusCreated() {
        log.info("Start test: создать пользователя со старым полем email.");
        setUp();
        userOutputDTO = userOutputDTO.toBuilder().id(3L).build();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(userOutputDTO)));

        log.info("End test: создать пользователя со старым полем email, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @Order(7)
    @SneakyThrows
    @DisplayName("Integration Test: обновить пользователя, только поле name, возвращается ответ: HttpStatus.OK.")
    public void testUpdateUser_OnlyName_ResulStatusOk() {
        log.info("Start test: обновить пользователя, только поле name.");
        setUp();
        UserInputDTO userInputDTO = UserInputDTO.builder().name("updateRuRu").build();
        userOutputDTO = userOutputDTO.toBuilder().id(userId2).name("updateRuRu").build();

        mvc.perform(patch("/users/{userId}", userId2)
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userOutputDTO)));

        log.info("End test: обновить пользователя, только поле name, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(8)
    @SneakyThrows
    @DisplayName("Integration Test: обновить пользователя с существующим email, возвращается ответ: HttpStatus.CONFLICT.")
    public void testUpdateUser_WithDuplicateEmail_ResulStatusConflict() {
        log.info("Start test: обновить пользователя с существующим email.");
        setUp();
        userInputDTO = userInputDTO.toBuilder().email("updateRuRu@yandex.ru").build();

        mvc.perform(patch("/users/{userId}", userId2)
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        DataIntegrityViolationException.class));

        log.info("End test: обновить пользователя с существующим email, возвращается ответ: HttpStatus.CONFLICT.");
    }

    @Test
    @Order(9)
    @SneakyThrows
    @DisplayName("Integration Test: получить всех пользователей, возвращается ответ: HttpStatus.OK.")
    public void testGetAllUser_ResulStatusOk() {
        log.info("Start test: получить всех пользователей.");

        UserOutputDTO userOutputDTO1 = UserOutputDTO.builder()
                .id(1L)
                .email("updateRuRu@yandex.ru")
                .name("RuRu")
                .build();

        UserOutputDTO userOutputDTO2 = UserOutputDTO.builder()
                .id(3L)
                .email("RuRu@yandex.ru")
                .name("updateRuRu")
                .build();

        List<UserOutputDTO> users = Arrays.asList(userOutputDTO1, userOutputDTO2);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(users)));

        log.info("End test: получить всех пользователей, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(10)
    @SneakyThrows
    @DisplayName("Integration Test: удалить пользователя по ID, возвращается ответ: HttpStatus.NO_CONTENT.")
    public void testDeleteUser_ResulStatusOk() {
        log.info("Start test: удалить пользователя по ID.");

        mvc.perform(delete("/users/{userId}", userId1))
                .andExpect(status().isNoContent());

        log.info("End test: удалить пользователя по ID, возвращается ответ: HttpStatus.NO_CONTENT.");
    }

    @Test
    @Order(11)
    @SneakyThrows
    @DisplayName("Integration Test: получить всех пользователей после удаления пользователя с ID = 1, возвращается ответ: HttpStatus.OK.")
    public void testGetAllUser_AfterDeleteUser_ResulStatusOk() {
        log.info("Start test: получить всех пользователей после удаления пользователя с ID = 1.");

        UserOutputDTO userOutputDTO2 = UserOutputDTO.builder()
                .id(3L)
                .email("RuRu@yandex.ru")
                .name("updateRuRu")
                .build();

        List<UserOutputDTO> users = Collections.singletonList(userOutputDTO2);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(users)));

        log.info("End test: получить всех пользователей после удаления пользователя с ID = 1, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @Order(12)
    @SneakyThrows
    @DisplayName("Integration Test: удалить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    public void testDeleteUser_WithInvalidId_ResulStatusOk() {
        log.info("Start test: удалить пользователя по неверному ID.");

        mvc.perform(delete("/users/{userId}", userId1))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        log.info("End test: удалить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }
}