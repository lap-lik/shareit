package ru.practicum.shareit.booking.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingInputDTO {

    private Long id;

    @NotNull(message = "The start of the booking must not be null.")
    @FutureOrPresent(message = "The start of the booking cannot be earlier than the current time.")
    private LocalDateTime start;

    @NotNull(message = "The end of the booking must not be null.")
    @Future(message = "The end of the booking cannot be earlier than the current time.")
    private LocalDateTime end;

    private Status status;

    private Long bookerId;

    private Long itemId;
}
