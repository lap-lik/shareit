package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDTO;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingOutputDTO createBooking(@RequestHeader(REQUEST_HEADER_USER_ID) Long bookerId,
                                          @Valid @RequestBody final BookingInputDTO inputDTO) {

        log.info("START endpoint `method:POST /bookings` (create booking), booking itemId: {}.", inputDTO.getItemId());
        inputDTO.setBookerId(bookerId);

        return bookingService.create(inputDTO);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDTO getBookingById(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                           @PathVariable final Long bookingId) {

        log.info("START endpoint `method:GET /bookings/{bookingId}` (get booking by id), booking id: {}.", bookingId);

        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingOutputDTO> getAllBookingsAtBooker(@RequestHeader(REQUEST_HEADER_USER_ID) Long bookerId,
                                                         @RequestParam(defaultValue = "ALL") String state,
                                                         @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                         @RequestParam(defaultValue = "20") @Positive Integer size) {

        log.info("START endpoint `method:GET /bookings?state={state}` (get all bookings at booker), booker id: {}.", bookerId);

        return bookingService.getAllBookingsAtBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingOutputDTO> getAllBookingsAtOwner(@RequestHeader(REQUEST_HEADER_USER_ID) Long ownerId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                        @RequestParam(defaultValue = "20") @Positive Integer size) {

        log.info("START endpoint `method:GET /bookings/owner?state={state}` (get all bookings at owner), owner id: {}.", ownerId);

        return bookingService.getAllBookingsAtOwner(ownerId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDTO updateBooking(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                          @PathVariable final Long bookingId,
                                          @RequestParam boolean approved) {

        log.info("START endpoint `method:PATCH /bookings/{bookingId}` (approved booking), booking id: {}.", bookingId);

        return bookingService.approveBooking(userId, bookingId, approved);
    }
}
