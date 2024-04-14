package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingRequestMapper {

    @Mappings({
            @Mapping(source = "bookerId", target = "booker.id"),
            @Mapping(source = "itemId", target = "item.id")
    })
    Booking toEntity(BookingRequestDto requestDto);

    BookingRequestDto toDto(Booking entity);
}
