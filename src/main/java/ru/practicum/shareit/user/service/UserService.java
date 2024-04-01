package ru.practicum.shareit.user.service;

import ru.practicum.shareit.generic.GenericService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

/**
 * The UserService interface represents a service for managing user.
 * It extends the GenericService interface with UserDTO as the entity.
 *
 * @see GenericService
 */
public interface UserService extends GenericService<UserRequestDto, UserResponseDto> {
}
