package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.generic.GenericMapper;
import ru.practicum.shareit.item.model.Item;

/**
 * The ItemMapper interface represents a mapper for converting Item entities to ItemRequestDTOs and vice versa.
 *
 * @see GenericMapper
 */
@Mapper(componentModel = "spring")
public interface ItemRequestMapper extends GenericMapper<Item, ItemRequestDto> {
    @Override
    @Mapping(source = "ownerId", target = "owner.id")
    Item toEntity(ItemRequestDto requestDto);

    @Override
    @Mapping(source = "owner.id", target = "ownerId")
    ItemRequestDto toDto(Item entity);
}
