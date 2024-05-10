package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ItemMapperTest {
    @InjectMocks
    private ItemMapperImpl itemMapper;

    @Test
    public void testToShortOutputDTO_ReturnNull() {

        assertNull(itemMapper.toShortOutputDTO(null));
    }

    @Test
    public void testToShortOutputDTOs_ReturnNull() {

        assertNull(itemMapper.toShortOutputDTOs(null));
    }

    @Test
    public void testToItemOutputDTOs_ReturnNull() {

        assertNull(itemMapper.toItemOutputDTOs(null));
    }

    @Test
    public void testToInputDTO_ReturnNull() {

        assertNull(itemMapper.toInputDTO(null));
    }

    @Test
    public void testToItemOutputDTO_ReturnNull() {

        assertNull(itemMapper.toItemOutputDTO(null));
    }

    @Test
    public void testUpdateInputDTOToEntity_ReturnNull() {

        assertNull(itemMapper.updateInputDTOToEntity(null));
    }
}
