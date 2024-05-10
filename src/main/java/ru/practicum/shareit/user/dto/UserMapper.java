package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * The UserRequestMapper interface is used to map between UserRequestDTO objects and User entities.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps the fields from a UserInputDTO object to a User entity.
     *
     * @param inputDto The UserInputDTO object to be mapped.
     * @return The mapped User entity.
     */
    User inputDTOToEntity(UserInputDTO inputDto);

    /**
     * Maps the fields from a User entity to a UserInputDTO object.
     *
     * @param entity The User entity to be mapped.
     * @return The mapped UserInputDTO object.
     */
    UserInputDTO toInputDTO(User entity);

    /**
     * Maps the fields from a User entity to a UserOutputDTO object.
     *
     * @param entity The User entity to be mapped.
     * @return The mapped UserOutputDTO object.
     */
    UserOutputDTO toOutputDTO(User entity);

    /**
     * Maps a list of User entities to a list of UserOutputDTO objects.
     *
     * @param entities The list of User entities to be mapped.
     * @return The list of mapped UserOutputDTO objects.
     */
    List<UserOutputDTO> toOutputDTOs(List<User> entities);
}
