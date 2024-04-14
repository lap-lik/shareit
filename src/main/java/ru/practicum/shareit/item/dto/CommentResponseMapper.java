package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * The ItemMapper interface represents a mapper for converting Item entities to ItemResponseDTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface CommentResponseMapper {

    @Mapping(source = "author.name", target = "authorName")
    CommentResponseDto toDto(Comment entity);

    List<CommentResponseDto> toDtos(List<Comment> entity);
}
