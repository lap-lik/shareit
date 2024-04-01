package ru.practicum.shareit.item.service;

import ru.practicum.shareit.generic.GenericService;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

/**
 * The ItemService interface represents a service for managing item.
 * It extends the GenericService interface with ItemDTO as the entity.
 * The methods provided in this interface are: get all items by ownerId, search items by text.
 *
 * @see GenericService
 */
public interface ItemService extends GenericService<ItemRequestDto, ItemResponseDto> {

    List<ItemResponseDto> getAllByOwnerId(Long ownerId);

    List<ItemResponseDto> searchItemsByText(String text);
}

