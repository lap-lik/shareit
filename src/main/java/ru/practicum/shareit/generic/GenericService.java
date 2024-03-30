package ru.practicum.shareit.generic;

import java.util.List;

/**
 * The GenericService interface represents a generic service for managing entities.
 *
 * @param <T> The requestDTO type.
 * @param <E> The responseDTO type.
 */
public interface GenericService<T extends BaseDto, E extends BaseDto> {

    /**
     * Creates a new entity.
     *
     * @param requestDto The requestDTO object representing the entity to be created.
     * @return The created entity (responseDTO).
     */
    E create(T requestDto);

    /**
     * Updates an entity.
     *
     * @param id         The identifier of the entity.
     * @param requestDto The requestDTO object representing the entity to be updated.
     * @return The updated entity (responseDTO).
     */
    E update(Long id, T requestDto);

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id The identifier of the entity.
     * @return The entity (responseDTO) with the specified identifier, or null if not found.
     */
    E getById(Long id);

    /**
     * Retrieves a list of all entities.
     *
     * @return A list containing all entities.
     */
    List<E> getAll();

    /**
     * Deletes an entity by its identifier.
     *
     * @param id The identifier of the entity to be deleted.
     */
    void deleteById(Long id);
}
