package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * The UserMapper interface represents a mapper for converting User entities to UserResponseDTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    /**
     * Converts an entity object to a DTO.
     *
     * @param entity The entity object to be converted.
     * @return The corresponding requestDTO.
     */
    UserResponseDto toDto(User entity);

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param entities The list of entities to be converted.
     * @return The corresponding list of requestDTOs.
     */
    List<UserResponseDto> toDtos(List<User> entities);
}
