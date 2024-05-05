package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * The ItemResponseMapper interface is used to map between Item entities and ItemResponseDto objects.
 */
@Mapper(componentModel = "spring")
public interface ItemResponseMapper {
    /**
     * Maps the fields from an Item entity to an ItemResponseDto object.
     *
     * @param entity The Item entity to be mapped.
     * @return The mapped ItemResponseDto object.
     */
    ItemResponseDto toDto(Item entity);

    /**
     * Maps a list of Item entities to a list of ItemResponseDto objects.
     *
     * @param entities The list of Item entities to be mapped.
     * @return The list of mapped ItemResponseDto objects.
     */
    List<ItemResponseDto> toDtos(List<Item> entities);
}
