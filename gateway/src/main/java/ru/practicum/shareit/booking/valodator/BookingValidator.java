package ru.practicum.shareit.booking.valodator;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.enumeration.State;
import ru.practicum.shareit.exception.UnsupportedException;
import ru.practicum.shareit.exception.ValidException;

import java.time.LocalDateTime;

@Component
public class BookingValidator {

    public State validateState(String queryState) {

        try {
            return State.valueOf(queryState);
        } catch (IllegalArgumentException e) {
            throw UnsupportedException.builder()
                    .message(String.format("Unknown state: %s", queryState))
                    .build();
        }
    }

    public void validateBookingDateTime(BookingInputDTO inputDTO) {

        LocalDateTime start = inputDTO.getStart();
        LocalDateTime end = inputDTO.getEnd();

        if (start.isAfter(end)) {
            throw ValidException.builder()
                    .message("The start of the booking cannot be later than the end of the booking.")
                    .build();
        }

        if (start.equals(end)) {
            throw ValidException.builder()
                    .message("The beginning of the booking cannot be the end of the booking.")
                    .build();
        }
    }
}
