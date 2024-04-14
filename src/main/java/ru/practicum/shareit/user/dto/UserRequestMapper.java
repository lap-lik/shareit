package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;

/**
 * The UserMapper interface represents a mapper for converting User entities to UserRequestDTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    /**
     * Converts a DTO object to an entity.
     *
     * @param requestDto The DTO object to be converted.
     * @return The corresponding entity.
     */
    User toEntity(UserRequestDto requestDto);

    /**
     * Converts an entity object to a DTO.
     *
     * @param entity The entity object to be converted.
     * @return The corresponding requestDTO.
     */
    UserRequestDto toDto(User entity);
}
