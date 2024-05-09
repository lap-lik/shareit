package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ItemRequestMapperTest {

    @InjectMocks
    private ItemRequestMapperImpl itemRequestMapper;

    @Test
    public void testInputDTOToEntity_Mapping() {

        ItemRequestInputDTO inputDTO = ItemRequestInputDTO.builder()
                .id(1L)
                .description("Нужен диван.")
                .requesterId(2L)
                .build();

        ItemRequest itemRequest = itemRequestMapper.inputDTOToEntity(inputDTO);

        assertThat(itemRequest).isNotNull();
        assertThat(itemRequest.getId()).isEqualTo(inputDTO.getId());
        assertThat(itemRequest.getDescription()).isEqualTo(inputDTO.getDescription());
        assertThat(itemRequest.getRequester().getId()).isEqualTo(inputDTO.getRequesterId());
    }

    @Test
    public void testInputDTOToEntity_ReturnNull() {

        assertNull(itemRequestMapper.inputDTOToEntity(null));
    }

    @Test
    public void testToOutputDTO_Mapping() {
        User requester = User.builder()
                .id(2L)
                .build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Нужен диван.")
                .requester(requester)
                .build();

        ItemRequestOutputDTO outputDTO = itemRequestMapper.toOutputDTO(itemRequest);

        assertThat(outputDTO).isNotNull();
        assertThat(outputDTO.getId()).isEqualTo(itemRequest.getId());
        assertThat(outputDTO.getDescription()).isEqualTo(itemRequest.getDescription());
        assertThat(outputDTO.getRequesterId()).isEqualTo(itemRequest.getRequester().getId());
    }

    @Test
    public void testToOutputDTOs_Mapping() {
        ItemRequest itemRequest1 = ItemRequest.builder().id(1L).build();
        ItemRequest itemRequest2 = ItemRequest.builder().id(2L).build();
        List<ItemRequest> entities = Arrays.asList(itemRequest1, itemRequest2);

        List<ItemRequestOutputDTO> result = itemRequestMapper.toOutputDTOs(entities);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    public void testToOutputDTOs_ReturnNull() {

        assertNull(itemRequestMapper.toOutputDTOs(null));
    }


    @Test
    public void testItemRequestInputDTOToUser_Mapping() {
        ItemRequestInputDTO itemRequestInputDTO = new ItemRequestInputDTO();
        itemRequestInputDTO.setRequesterId(1L);

        assertEquals(1L, itemRequestMapper.itemRequestInputDTOToUser(itemRequestInputDTO).getId());
    }

    @Test
    public void testItemRequestInputDTOToUser_ReturnNull() {

        assertNull(itemRequestMapper.itemRequestInputDTOToUser(null));
    }

}
