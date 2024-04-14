package ru.practicum.shareit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * The UserDao interface represents a data access object for managing users.
 * It extends the GenericDao interface with User as the entity type.
 * The methods provided in this interface are: getting the user ID by email.
 *
 * @see JpaRepository
 */
public interface UserDao extends JpaRepository<User, Long> {
    boolean existsById(Long id);
}
