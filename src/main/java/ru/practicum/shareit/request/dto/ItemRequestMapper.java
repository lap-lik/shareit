package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * The ItemRequestMapper interface is used to map between ItemRequest entities and ItemRequestDTO objects.
 */
@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(source = "requesterId", target = "requester.id")
    ItemRequest inputDTOToEntity(ItemRequestInputDTO inputDTO);

    @Mapping(source = "requester.id", target = "requesterId")
    ItemRequestOutputDTO toOutputDTO(ItemRequest entity);

    List<ItemRequestOutputDTO> toOutputDTOs(List<ItemRequest> entities);
}
