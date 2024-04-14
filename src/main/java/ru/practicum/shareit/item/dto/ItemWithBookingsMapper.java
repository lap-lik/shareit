package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * The ItemMapper interface represents a mapper for converting Item entities to ItemResponseDTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface ItemWithBookingsMapper {

    /**
     * Converts an entity object to a DTO.
     *
     * @param entity The entity object to be converted.
     * @return The corresponding requestDTO.
     */
    ItemWithBookingsDto toDto(Item entity);

    List<ItemWithBookingsDto> toDtos(List<Item> entities);
}
