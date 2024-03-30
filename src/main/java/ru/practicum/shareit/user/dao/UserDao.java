package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.generic.GenericDao;
import ru.practicum.shareit.user.model.User;

/**
 * The UserDao interface represents a data access object for managing users.
 * It extends the GenericDao interface with User as the entity type.
 * The methods provided in this interface are: getting the user ID by email.
 *
 * @see GenericDao
 */
public interface UserDao extends GenericDao<User> {

    Long findUserIdByEmail(String email);
}
