package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.enumeration.State;
import ru.practicum.shareit.exception.UnsupportedException;
import ru.practicum.shareit.exception.ValidException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient client;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestHeader(REQUEST_HEADER_USER_ID) long bookerId,
                                                @Valid @RequestBody final BookingInputDTO inputDTO) {

        log.info("START endpoint `method:POST /bookings` (create booking), booking itemId: {}.", inputDTO.getItemId());
        validateDateTime(inputDTO);

        return client.createBooking(bookerId, inputDTO);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                 @PathVariable long bookingId) {

        log.info("START endpoint `method:GET /bookings/{bookingId}` (get booking by id), booking id: {}.", bookingId);

        return client.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsAtBooker(@RequestHeader(REQUEST_HEADER_USER_ID) Long bookerId,
                                                         @RequestParam(defaultValue = "ALL") String state,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(defaultValue = "20") @Positive Integer size) {

        log.info("START endpoint `method:GET /bookings?state={state}` (get all bookings at booker), booker id: {}.", bookerId);
        State validState = validateState(state);

        return client.getAllBookingsAtBooker(bookerId, validState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsAtOwner(@RequestHeader(REQUEST_HEADER_USER_ID) long ownerId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                        @RequestParam(defaultValue = "20") @Positive Integer size) {

        log.info("START endpoint `method:GET /bookings/owner?state={state}` (get all bookings at owner), owner id: {}.", ownerId);
        State validState = validateState(state);

        return client.getAllBookingsAtOwner(ownerId, validState, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                                @PathVariable long bookingId,
                                                @RequestParam boolean approved) {

        log.info("START endpoint `method:PATCH /bookings/{bookingId}` (approved booking), booking id: {}.", bookingId);

        return client.updateBooking(userId, bookingId, approved);
    }

    private State validateState(String queryState) {

        try {
            return State.valueOf(queryState);
        } catch (IllegalArgumentException e) {
            throw UnsupportedException.builder()
                    .message(String.format("Unknown state: %s", queryState))
                    .build();
        }
    }

    private void validateDateTime(BookingInputDTO inputDTO) {

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
