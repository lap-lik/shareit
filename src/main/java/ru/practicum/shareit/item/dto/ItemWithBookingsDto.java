package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingShortDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemWithBookingsDto {

    private Long id;

    private String name;

    private String description;

    private boolean available;

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;
}