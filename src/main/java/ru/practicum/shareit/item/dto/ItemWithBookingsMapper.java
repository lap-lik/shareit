package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * The ItemWithBookingsMapper interface is used to map between Item entities
 * and ItemWithBookingsAndCommentsDto objects.
 */
@Mapper(componentModel = "spring")
public interface ItemWithBookingsMapper {

    /**
     * Maps the fields from an Item entity to an ItemWithBookingsAndCommentsDto object.
     *
     * @param entity The Item entity to be mapped.
     * @return The mapped ItemWithBookingsAndCommentsDto object.
     */
    ItemWithBookingsAndCommentsDto toDto(Item entity);

    /**
     * Maps a list of Item entities to a list of ItemWithBookingsAndCommentsDto objects.
     *
     * @param entities The list of Item entities to be mapped.
     * @return The list of mapped ItemWithBookingsAndCommentsDto objects.
     */
    List<ItemWithBookingsAndCommentsDto> toDtos(List<Item> entities);
}
