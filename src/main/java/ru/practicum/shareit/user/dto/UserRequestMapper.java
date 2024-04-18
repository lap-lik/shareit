package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;

/**
 * The UserRequestMapper interface is used to map between UserRequestDto objects and User entities.
 */
@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    /**
     * Maps the fields from a UserRequestDto object to a User entity.
     *
     * @param requestDto The UserRequestDto object to be mapped.
     * @return The mapped User entity.
     */
    User toEntity(UserRequestDto requestDto);

    /**
     * Maps the fields from a User entity to a UserRequestDto object.
     *
     * @param entity The User entity to be mapped.
     * @return The mapped UserRequestDto object.
     */
    UserRequestDto toDto(User entity);
}
