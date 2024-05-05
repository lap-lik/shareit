package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortOutDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemWithBookingsAndCommentsDto {

    private Long id;

    private String name;

    private String description;

    private boolean available;

    private BookingShortOutDTO lastBooking;

    private BookingShortOutDTO nextBooking;

    private List<CommentResponseDto> comments;
}