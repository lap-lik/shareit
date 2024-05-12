package ru.practicum.shareit.booking.dto;


import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingInputDTO {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Status status;

    private Long bookerId;

    private Long itemId;
}
