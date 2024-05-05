package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserInputDTO;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserOutputDTO;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserInputDTO userInputDTO;
    private UserOutputDTO userOutputDTO;
    private Long userId = 1L;

    void setUp() {
        user = User.builder()
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

    @Test
    public void testCreateUser() {
        // Создание тестовых данных
        setUp();

        // Настройка моков
        when(userMapper.inputDTOToEntity(userInputDTO)).thenReturn(user);
        when(userDAO.save(user)).thenReturn(user);
        when(userMapper.toOutputDTO(user)).thenReturn(userOutputDTO);

        // Вызов метода, который мы тестируем
        UserOutputDTO actualUserOutputDTO = userService.create(userInputDTO);

        // Проверка результатов
        assertEquals(userOutputDTO, actualUserOutputDTO);
        verify(userMapper).inputDTOToEntity(userInputDTO);
        verify(userDAO).save(user);
        verify(userMapper).toOutputDTO(user);
    }

}