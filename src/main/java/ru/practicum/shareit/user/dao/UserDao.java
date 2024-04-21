package ru.practicum.shareit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * The UserDao interface represents a data access object for managing users.
 * It extends the JpaRepository interface with User as the entity type.
 *
 * @see JpaRepository
 */
public interface UserDao extends JpaRepository<User, Long> {

    /**
     * Checks if an entity with the given ID exists.
     *
     * @param id the ID of the entity to check for existence
     * @return true if an entity with the specified ID exists, false otherwise
     */
    boolean existsById(Long id);
}
