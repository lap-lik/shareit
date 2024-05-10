package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * The ItemMapper interface is used to map between Item entities and ItemDTO objects.
 */

@Mapper(componentModel = "spring")
public interface ItemMapper {
    /**
     * Maps the fields from an Item entity to an ItemShortOutputDTO object.
     *
     * @param entity The Item entity to be mapped.
     * @return The mapped ItemShortOutputDTO object.
     */
    @Mapping(source = "request.id", target = "requestId")
    ItemShortOutputDTO toShortOutputDTO(Item entity);

    /**
     * Maps a list of Item entities to a list of ItemShortOutputDTO objects.
     *
     * @param entities The list of Item entities to be mapped.
     * @return The list of mapped ItemShortOutputDTO objects.
     */
    List<ItemShortOutputDTO> toShortOutputDTOs(List<Item> entities);

    /**
     * Maps the fields from an ItemInputDTO object to an Item entity.
     * Sets the value of the field "owner.id" from the property "ownerId".
     *
     * @param inputDTO the object of type ItemInputDTO from which the mapping is performed
     * @return an object of type Item containing the mapped data
     */
    @Mapping(source = "ownerId", target = "owner.id")
    Item updateInputDTOToEntity(ItemInputDTO inputDTO);

    default Item inputDTOToEntity(ItemInputDTO inputDTO) {
        Item item = updateInputDTOToEntity(inputDTO);

        if (inputDTO.getRequestId() != null) {
            item = item.toBuilder()
                    .request(ItemRequest.builder()
                            .id(inputDTO.getRequestId())
                            .build())
                    .build();
        }

        return item;
    }

    /**
     * Maps the fields from an Item entity to an ItemInputDTO object.
     *
     * @param entity The Item entity to be mapped.
     * @return The mapped ItemInputDTO object.
     */
    @Mappings({@Mapping(source = "owner.id", target = "ownerId"),
            @Mapping(source = "request.id", target = "requestId")})
    ItemInputDTO toInputDTO(Item entity);

    /**
     * Maps the fields from an Item entity to an ItemOutputDTO object.
     *
     * @param entity The Item entity to be mapped.
     * @return The mapped ItemOutputDTO object.
     */
    @Mapping(source = "request.id", target = "requestId")
    ItemOutputDTO toItemOutputDTO(Item entity);

    /**
     * Maps a list of Item entities to a list of ItemOutputDTO objects.
     *
     * @param entities The list of Item entities to be mapped.
     * @return The list of mapped ItemOutputDTO objects.
     */
    List<ItemOutputDTO> toItemOutputDTOs(List<Item> entities);
}
