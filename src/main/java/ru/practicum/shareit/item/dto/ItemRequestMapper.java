package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

/**
 * The ItemRequestMapper interface is used to map between ItemRequestDto objects and Item entities.
 */
@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    /**
     * Maps the fields from an ItemRequestDto object to an Item entity.
     *
     * @param requestDto The ItemRequestDto object to be mapped.
     * @return The mapped Item entity.
     */
    @Mapping(source = "ownerId", target = "owner.id")
    Item toEntity(ItemRequestDto requestDto);

    /**
     * Maps the fields from an Item entity to an ItemRequestDto object.
     *
     * @param entity The Item entity to be mapped.
     * @return The mapped ItemRequestDto object.
     */
    @Mapping(source = "owner.id", target = "ownerId")
    ItemRequestDto toDto(Item entity);
}
