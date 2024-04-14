package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto create(BookingRequestDto requestDto);

    BookingResponseDto getById(Long userId, Long bookingId);

    BookingResponseDto approvedBooking(Long userId, Long bookingId, boolean approved);

    List<BookingResponseDto> getAllBookingsAtBooker(Long bookerId, String state);

    List<BookingResponseDto> getAllBookingsAtOwner(Long ownerId, String state);
}
