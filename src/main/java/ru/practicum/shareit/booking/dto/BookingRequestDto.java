package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.validation.Marker;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingRequestDto {

    @Null(groups = Marker.OnCreate.class)
    @NotNull(groups = Marker.OnUpdate.class, message = "The ID must not be null.")
    private Long id;

    @NotNull(message = "The start of the booking must not be null.")
    @FutureOrPresent(message = "The start of the booking cannot be earlier than the current time.")
    private LocalDateTime start;

    @NotNull(message = "The end of the booking must not be null.")
    @Future(message = "The end of the booking cannot be earlier than the current time.")
    private LocalDateTime end;

    @NotNull(message = "The status must not be null.")
    private Status status;

    @NotNull(message = "The bookerId must not be null.")
    private Long bookerId;

    @NotNull(message = "The itemId must not be null.")
    private Long itemId;
}
