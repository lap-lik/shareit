package ru.practicum.shareit.request.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dao.ItemRequestDAO;
import ru.practicum.shareit.request.dto.ItemRequestInputDTO;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestOutputDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestDAO itemRequestDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private ItemDAO itemDAO;

    @Mock
    private ItemRequestMapper mapper;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private ItemRequest itemRequest;
    private ItemRequestInputDTO itemRequestInputDTO;
    private ItemRequestOutputDTO itemRequestOutputDTO;
    private final Long userId = 1L;
    private final Long itemRequestId = 1L;
    private final Long invalidId = 999L;
    private final int from = 0;
    private final int size = 2;
    LocalDateTime customDateTime = LocalDateTime.of(2022, 10, 31, 12, 30);

    void setUp() {

        itemRequest = ItemRequest.builder()
                .id(itemRequestId)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(customDateTime)
                .requester(User.builder()
                        .id(userId)
                        .name("RuRu")
                        .email("RuRu@yandex.ru")
                        .build())
                .build();

        itemRequestInputDTO = ItemRequestInputDTO.builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requesterId(userId)
                .build();

        itemRequestOutputDTO = ItemRequestOutputDTO.builder()
                .id(itemRequestId)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(customDateTime)
                .requesterId(userId)
                .items(new ArrayList<>())
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName("MockitoTest: создать запрос на предмет, возвращается: ItemRequestOutputDTO.")
    void testCreateItemRequest_ReturnItemRequestOutputDTO() {

        log.info("Start test: создать запрос на предмет.");
        setUp();

        when(userDAO.existsById(userId)).thenReturn(true);
        when(mapper.inputDTOToEntity(itemRequestInputDTO)).thenReturn(itemRequest);
        when(itemRequestDAO.save(itemRequest)).thenReturn(itemRequest);
        when(mapper.toOutputDTO(itemRequest)).thenReturn(itemRequestOutputDTO);

        assertEquals(itemRequestOutputDTO, itemRequestService.create(userId, itemRequestInputDTO));

        log.info("End test: создать запрос на предмет, возвращается: ItemRequestOutputDTO.");
    }

    @Test
    @SneakyThrows
    @DisplayName("MockitoTest: создать запрос на предмет, передается не верный ID пользователя, возвращается: NotFoundException.")
    void testCreateItemRequest_WithInvalidUserId_ReturnNotFoundException() {

        log.info("Start test: создать запрос на предмет, передается не верный ID пользователя.");
        setUp();

        when(userDAO.existsById(invalidId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemRequestService.create(invalidId, itemRequestInputDTO));

        log.info("End test: создать запрос на предмет, передается не верный ID пользователя, возвращается: NotFoundException.");
    }

    @Test
    @SneakyThrows
    @DisplayName("MockitoTest: получить запрос на предмет по ID, возвращается: ItemRequestOutputDTO.")
    void testGetItemRequest_ByRequestId_ReturnItemRequestOutputDTO() {

        log.info("Start test: получить запрос на предмет по ID.");
        setUp();

        when(userDAO.existsById(userId)).thenReturn(true);
        when(itemRequestDAO.findById(itemRequestId)).thenReturn(Optional.of(itemRequest));
        when(mapper.toOutputDTO(itemRequest)).thenReturn(itemRequestOutputDTO);
        when(itemDAO.findAllByRequest_Id(itemRequestId)).thenReturn(List.of());
        when(itemMapper.toShortOutputDTOs(List.of())).thenReturn(List.of());

        assertEquals(itemRequestOutputDTO, itemRequestService.getByRequestId(userId, itemRequestId));

        log.info("End test: получить запрос на предмет по ID, возвращается: ItemRequestOutputDTO.");
    }

    @Test
    @SneakyThrows
    @DisplayName("MockitoTest: получить запрос на предмет по неверному ID, возвращается: NotFoundException.")
    void testGetItemRequest_ByInvalidRequestId_ReturnNotFoundException() {

        log.info("Start test: получить запрос на предмет по неверному ID.");

        when(userDAO.existsById(userId)).thenReturn(true);
        when(itemRequestDAO.findById(invalidId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> itemRequestService.getByRequestId(userId, invalidId));

        log.info("End test: получить запрос на предмет по неверному ID, возвращается: NotFoundException.");
    }

    @Test
    @SneakyThrows
    @DisplayName("MockitoTest: получить все запросы на предметы по ID создателя запросов, возвращается: List<ItemRequestOutputDTO>.")
    void testGetAllItemRequest_ByRequesterId_ReturnListOfItemRequestOutputDTO() {

        log.info("Start test: получить все запросы на предметы по ID создателя запросов.");
        setUp();
        List<ItemRequest> itemRequests = List.of(itemRequest);
        List<ItemRequestOutputDTO> itemRequestOutputDTOS = List.of(itemRequestOutputDTO);

        when(userDAO.existsById(userId)).thenReturn(true);
        when(itemRequestDAO.findAllByRequester_Id(userId)).thenReturn(itemRequests);
        when(mapper.toOutputDTOs(itemRequests)).thenReturn(itemRequestOutputDTOS);

        assertEquals(itemRequestOutputDTOS, itemRequestService.getAllByRequesterId(userId));

        log.info("End test: получить все запросы на предметы по ID создателя запросов, возвращается: List<ItemRequestOutputDTO>.");
    }

    @Test
    @SneakyThrows
    @DisplayName("MockitoTest: получить все запросы на предметы других пользователей, возвращается: List<ItemRequestOutputDTO>.")
    void testGetAllItemRequestFromOtherUsers_ByPageParam_ReturnListOfItemRequestOutputDTO() {

        log.info("Start test: получить все запросы на предметы других пользователей.");
        setUp();
        List<ItemRequest> itemRequests = List.of(itemRequest);
        List<ItemRequestOutputDTO> itemRequestOutputDTOS = List.of(itemRequestOutputDTO);

        when(userDAO.existsById(userId)).thenReturn(true);
        when(itemRequestDAO.findAllFromOtherUsers(userId, from, size)).thenReturn(itemRequests);
        when(mapper.toOutputDTOs(itemRequests)).thenReturn(itemRequestOutputDTOS);

        assertEquals(itemRequestOutputDTOS, itemRequestService.getAll(userId, from, size));

        log.info("End test: получить все запросы на предметы других пользователей, возвращается: List<ItemRequestOutputDTO>.");
    }
}