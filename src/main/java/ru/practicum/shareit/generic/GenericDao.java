package ru.practicum.shareit.generic;

import java.util.List;
import java.util.Optional;

/**
 * The GenericDao interface represents a generic data access object (DAO) for managing entities.
 *
 * @param <E> The entity type.
 */
public interface GenericDao<E extends BaseEntity> {

    /**
     * Saves an entity.
     *
     * @param entity The entity to be saved.
     * @return The saved entity.
     */
    E save(E entity);

    /**
     * Updates an existing entity.
     *
     * @param entity The entity to be updated.
     * @return The updated entity if found, or empty if not found.
     */
    E update(E entity);

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id The identifier of the entity.
     * @return An Optional containing the entity if found, or empty if not found.
     */
    Optional<E> findById(Long id);

    /**
     * Retrieves all entities of type T.
     *
     * @return A list of all entities.
     */
    List<E> findAll();

    /**
     * Deletes an entity by its identifier.
     *
     * @param id The identifier of the entity to be deleted.
     */
    void deleteById(Long id);

    /**
     * Checks if an entity exists by its identifier.
     *
     * @param id The identifier of the entity to be checked.
     * @return true if the entity exists, or false if the entity does not exist.
     */
    boolean existsById(Long id);
}
