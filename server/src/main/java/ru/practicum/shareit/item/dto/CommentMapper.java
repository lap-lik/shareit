package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * The CommentMapper interface is used to map between CommentDTO and Comment entities.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Maps the fields from a CommentInputDTO to a Comment entity.
     *
     * @param inputDTO The CommentInputDTO object to be mapped.
     * @return The mapped Comment entity.
     */
    @Mappings({@Mapping(source = "authorId", target = "author.id"),
            @Mapping(source = "itemId", target = "item.id")})
    Comment inputDTOToEntity(CommentInputDTO inputDTO);

    /**
     * Maps the fields from a Comment entity to a CommentOutputDTO object.
     *
     * @param entity The Comment entity to be mapped.
     * @return The mapped CommentOutputDTO object.
     */
    @Mapping(source = "author.name", target = "authorName")
    CommentOutputDTO toOutputDTO(Comment entity);

    /**
     * Maps a list of Comment entities to a list of CommentOutputDTO objects.
     *
     * @param entities The list of Comment entities to be mapped.
     * @return The list of mapped CommentOutputDTO objects.
     */
    List<CommentOutputDTO> toOutputDTOs(List<Comment> entities);
}
