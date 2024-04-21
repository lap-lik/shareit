package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

/**
 * The BookingResponseMapper interface is used to map between Booking entities and BookingResponseDto objects.
 */
@Mapper(componentModel = "spring")
public interface BookingResponseMapper {

    /**
     * Maps the fields from a Booking entity to a BookingResponseDto object.
     *
     * @param entity The Booking entity to be mapped.
     * @return The mapped BookingResponseDto object.
     */
    BookingResponseDto toDto(Booking entity);

    /**
     * Maps a list of Booking entities to a list of BookingResponseDto objects.
     *
     * @param entities The list of Booking entities to be mapped.
     * @return The list of mapped BookingResponseDto objects.
     */
    List<BookingResponseDto> toDtos(List<Booking> entities);
}
