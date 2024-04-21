package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * The UserResponseMapper interface is used to map between User entities and UserResponseDto objects.
 */
@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    /**
     * Maps the fields from a User entity to a UserResponseDto object.
     *
     * @param entity The User entity to be mapped.
     * @return The mapped UserResponseDto object.
     */
    UserResponseDto toDto(User entity);

    /**
     * Maps a list of User entities to a list of UserResponseDto objects.
     *
     * @param entities The list of User entities to be mapped.
     * @return The list of mapped UserResponseDto objects.
     */
    List<UserResponseDto> toDtos(List<User> entities);
}
