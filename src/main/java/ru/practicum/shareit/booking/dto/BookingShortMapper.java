package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingShortMapper {

    @Mappings({
            @Mapping(source = "booker.id", target = "bookerId"),
    })
    BookingShortDto toDto(Booking entity);
}
