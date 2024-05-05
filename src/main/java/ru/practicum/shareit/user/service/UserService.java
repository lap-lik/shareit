package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserInputDTO;
import ru.practicum.shareit.user.dto.UserOutputDTO;

import java.util.List;

/**
 * The UserService interface represents a service for managing user.
 */
public interface UserService {

    /**
     * Creates a new user based on the provided user request data.
     *
     * @param inputDTO The data required to create the new user.
     * @return The response containing the details of the created user.
     */
    UserOutputDTO create(UserInputDTO inputDTO);

    /**
     * Updates an existing user owned by the specified owner ID with the provided user request data.
     *
     * @param ownerId  The ID of the owner of the user to be updated.
     * @param inputDTO The data required to update the user.
     * @return The response containing the updated user details.
     */
    UserOutputDTO update(Long ownerId, UserInputDTO inputDTO);

    /**
     * Retrieves a user by ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user details.
     */
    UserOutputDTO getById(Long userId);

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    List<UserOutputDTO> getAll();

    /**
     * Deletes a user by ID.
     *
     * @param userId The ID of the user to delete.
     */
    void deleteById(Long userId);
}
