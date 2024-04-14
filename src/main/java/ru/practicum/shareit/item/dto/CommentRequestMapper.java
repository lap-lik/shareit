package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.item.model.Comment;

/**
 * The CommentRequestMapper interface represents a mapper for converting Item entities to ItemRequestDTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface CommentRequestMapper {

    @Mappings({@Mapping(source = "authorId", target = "author.id"),
            @Mapping(source = "itemId", target = "item.id")})
    Comment toEntity(CommentRequestDto requestDto);
}
