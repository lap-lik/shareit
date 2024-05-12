package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemShortOutputDTO;
import ru.practicum.shareit.user.dto.UserOutputDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingOutputDTO {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Status status;

    private UserOutputDTO booker;

    private ItemShortOutputDTO item;
}
