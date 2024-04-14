package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.constant.UserConstant.REQUEST_HEADER_USER_ID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(@RequestHeader(REQUEST_HEADER_USER_ID) Long bookerId,
                                            @RequestBody final BookingRequestDto requestDto) {

        log.info("START endpoint `method:POST /bookings` (create booking), booking itemId: {}.", requestDto.getItemId());
        requestDto.setBookerId(bookerId);

        return bookingService.create(requestDto);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                             @PathVariable final Long bookingId) {

        log.info("START endpoint `method:GET /bookings/{bookingId}` (get booking by id), booking id: {}.", bookingId);

        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookingsAtBooker(@RequestHeader(REQUEST_HEADER_USER_ID) Long bookerId,
                                                  @RequestParam(defaultValue = "ALL") String state) {

        log.info("START endpoint `method:GET /bookings?state={state}` (get all bookings at booker), booker id: {}.", bookerId);

        return bookingService.getAllBookingsAtBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsAtOwner(@RequestHeader(REQUEST_HEADER_USER_ID) Long ownerId,
                                                  @RequestParam(defaultValue = "ALL") String state) {

        log.info("START endpoint `method:GET /bookings/owner?state={state}` (get all bookings at owner), owner id: {}.", ownerId);

        return bookingService.getAllBookingsAtOwner(ownerId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBooking(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                            @PathVariable final Long bookingId,
                                            @RequestParam boolean approved) {

        log.info("START endpoint `method:PATCH /bookings/{bookingId}` (approved booking), booking id: {}.", bookingId);

        return bookingService.approvedBooking(userId, bookingId, approved);
    }

}
