package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingResponseMapper {

    BookingResponseDto toDto(Booking entity);

    List<BookingResponseDto> toDtos(List<Booking> entities);
}
