package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * The ItemMapper interface represents a mapper for converting Item entities to ItemResponseDTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface ItemResponseMapper {

    ItemResponseDto toDto(Item entity);

    List<ItemResponseDto> toDtos(List<Item> entity);
}
