package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

/**
 * The ItemMapper interface represents a mapper for converting Item entities to ItemRequestDTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(source = "ownerId", target = "owner.id")
    Item toEntity(ItemRequestDto requestDto);

    @Mapping(source = "owner.id", target = "ownerId")
    ItemRequestDto toDto(Item entity);
}
