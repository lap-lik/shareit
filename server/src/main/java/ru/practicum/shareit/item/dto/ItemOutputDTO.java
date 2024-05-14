package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingShortOutputDTO;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemOutputDTO {

    private Long id;

    private String name;

    private String description;

    private boolean available;

    private BookingShortOutputDTO lastBooking;

    private BookingShortOutputDTO nextBooking;

    private List<CommentOutputDTO> comments;

    private Long requestId;
}