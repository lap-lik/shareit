package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * The CommentResponseMapper interface is used to map between Comment entities and CommentResponseDto objects.
 */
@Mapper(componentModel = "spring")
public interface CommentResponseMapper {

    /**
     * Maps the fields from a Comment entity to a CommentResponseDto object.
     *
     * @param entity The Comment entity to be mapped.
     * @return The mapped CommentResponseDto object.
     */
    @Mapping(source = "author.name", target = "authorName")
    CommentResponseDto toDto(Comment entity);

    /**
     * Maps a list of Comment entities to a list of CommentResponseDto objects.
     *
     * @param entities The list of Comment entities to be mapped.
     * @return The list of mapped CommentResponseDto objects.
     */
    List<CommentResponseDto> toDtos(List<Comment> entities);
}
