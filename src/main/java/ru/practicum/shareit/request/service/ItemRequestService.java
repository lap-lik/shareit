package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestInputDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutputDTO;

import java.util.List;

/**
 * The ItemRequestService interface represents a service for managing item request.
 */
public interface ItemRequestService {

    ItemRequestOutputDTO create(Long requesterId, ItemRequestInputDTO inputDTO);

    List<ItemRequestOutputDTO> getAllByRequesterId(Long requesterId);

    List<ItemRequestOutputDTO> getAll(Long userId, Integer from, Integer size);

    ItemRequestOutputDTO getByRequestId(Long userId, Long requestId);
}
