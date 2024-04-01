package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.generic.GenericMapper;
import ru.practicum.shareit.user.model.User;

/**
 * The UserMapper interface represents a mapper for converting User entities to UserRequestDTOs and vice versa.
 *
 * @see GenericMapper
 */
@Mapper(componentModel = "spring")
public interface UserRequestMapper extends GenericMapper<User, UserRequestDto> {
}
