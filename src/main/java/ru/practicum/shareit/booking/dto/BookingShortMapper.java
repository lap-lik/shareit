package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.booking.model.Booking;

/**
 * The BookingShortMapper interface is used to map between Booking entities and BookingShortDto objects.
 */
@Mapper(componentModel = "spring")
public interface BookingShortMapper {

    /**
     * Maps the fields from a Booking entity to a BookingShortDto object.
     *
     * @param entity The Booking entity to be mapped.
     * @return The mapped BookingShortDto object.
     */
    @Mappings({
            @Mapping(source = "booker.id", target = "bookerId"),
    })
    BookingShortDto toDto(Booking entity);

    /**
     * Maps the fields from a BookingResponseDto to a BookingShortDto object.
     *
     * @param responseDto The BookingResponseDto object to be mapped.
     * @return The mapped BookingShortDto object.
     */
    @Mappings({
            @Mapping(source = "booker.id", target = "bookerId"),
    })
    BookingShortDto responseDtoToShortDto(BookingResponseDto responseDto);
}
