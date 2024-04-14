package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

/**
 * The ItemService interface represents a service for managing item.
 * It extends the GenericService interface with ItemDTO as the entity.
 * The methods provided in this interface are: get all items by ownerId, search items by text.
 */
public interface ItemService {

    ItemResponseDto create(ItemRequestDto requestDto);

    ItemResponseDto update(Long ownerId, ItemRequestDto requestDto);

    ItemWithBookingsDto getById(Long userId, Long itemId);

    List<ItemWithBookingsDto> getAllByOwnerId(Long ownerId);

    List<ItemResponseDto> searchItemsByText(String text);

    CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto requestDto);
}

