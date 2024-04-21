package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.item.model.Comment;

/**
 * The CommentRequestMapper interface is used to map between CommentRequestDto and Comment entities.
 */
@Mapper(componentModel = "spring")
public interface CommentRequestMapper {

    /**
     * Maps the fields from a CommentRequestDto to a Comment entity.
     *
     * @param requestDto The CommentRequestDto object to be mapped.
     * @return The mapped Comment entity.
     */
    @Mappings({@Mapping(source = "authorId", target = "author.id"),
            @Mapping(source = "itemId", target = "item.id")})
    Comment toEntity(CommentRequestDto requestDto);
}
