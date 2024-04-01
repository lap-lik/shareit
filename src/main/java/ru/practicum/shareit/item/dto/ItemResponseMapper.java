package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.generic.GenericMapper;
import ru.practicum.shareit.item.model.Item;

/**
 * The ItemMapper interface represents a mapper for converting Item entities to ItemResponseDTOs and vice versa.
 *
 * @see GenericMapper
 */
@Mapper(componentModel = "spring")
public interface ItemResponseMapper extends GenericMapper<Item, ItemResponseDto> {
}
