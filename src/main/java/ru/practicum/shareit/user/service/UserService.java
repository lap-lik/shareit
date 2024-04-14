package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

/**
 * The UserService interface represents a service for managing user.
 * It extends the GenericService interface with UserDTO as the entity.
 *
 */
public interface UserService {

    /**
     * Creates a new entity.
     *
     * @param requestDto The requestDTO object representing the entity to be created.
     * @return The created entity (responseDTO).
     */
    UserResponseDto create(UserRequestDto requestDto);

    /**
     * Updates an entity.
     *
     * @param ownerId         The identifier of the entity.
     * @param requestDto The requestDTO object representing the entity to be updated.
     * @return The updated entity (responseDTO).
     */
    UserResponseDto update(Long ownerId, UserRequestDto requestDto);

    /**
     * Retrieves an entity by its identifier.
     *
     * @param itemId The identifier of the entity.
     * @return The entity (responseDTO) with the specified identifier, or null if not found.
     */
    UserResponseDto getById(Long itemId);

    /**
     * Retrieves a list of all entities.
     *
     * @return A list containing all entities.
     */
    List<UserResponseDto> getAll();

    /**
     * Deletes an entity by its identifier.
     *
     * @param itemId The identifier of the entity to be deleted.
     */
    void deleteById(Long itemId);
}
