package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

/**
 * The BookingMapper interface is used to map between BookingDTO objects and Booking entities.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper {
    /**
     * Maps the fields from a BookingInputDto object to a Booking entity.
     *
     * @param inputDTO The BookingInputDto object to be mapped.
     * @return The mapped Booking entity.
     */
    @Mappings({
            @Mapping(source = "bookerId", target = "booker.id"),
            @Mapping(source = "itemId", target = "item.id")
    })
    Booking inputDTOToEntity(BookingInputDTO inputDTO);

    /**
     * Maps the fields from a Booking entity to a BookingOutputDto object.
     *
     * @param entity The Booking entity to be mapped.
     * @return The mapped BookingOutputDto object.
     */
    BookingOutputDTO toOutputDTO(Booking entity);

    /**
     * Maps a list of Booking entities to a list of BookingOutputDto objects.
     *
     * @param entities The list of Booking entities to be mapped.
     * @return The list of mapped BookingOutputDto objects.
     */
    List<BookingOutputDTO> toOutputDTOs(List<Booking> entities);

    /**
     * Maps the fields from a BookingOutputDTO to a BookingShortOutputDTO object.
     *
     * @param outputDTO The BookingOutputDTO object to be mapped.
     * @return The mapped BookingShortOutputDTO object.
     */
    @Mapping(source = "booker.id", target = "bookerId")
    BookingShortOutputDTO outputDTOToShortOutputDTO(BookingOutputDTO outputDTO);
}
