package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.booking.model.Booking;

/**
 * The BookingRequestMapper interface is used to map between BookingRequestDto objects and Booking entities.
 */
@Mapper(componentModel = "spring")
public interface BookingRequestMapper {

    /**
     * Maps the fields from a BookingRequestDto object to a Booking entity.
     *
     * @param requestDto The BookingRequestDto object to be mapped.
     * @return The mapped Booking entity.
     */
    @Mappings({
            @Mapping(source = "bookerId", target = "booker.id"),
            @Mapping(source = "itemId", target = "item.id")
    })
    Booking toEntity(BookingRequestDto requestDto);

    /**
     * Maps the fields from a Booking entity to a BookingRequestDto object.
     *
     * @param entity The Booking entity to be mapped.
     * @return The mapped BookingRequestDto object.
     */
    BookingRequestDto toDto(Booking entity);
}
