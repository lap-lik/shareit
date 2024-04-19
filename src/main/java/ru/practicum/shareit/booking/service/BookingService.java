package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

/**
 * The BookingService interface represents a service for managing booking.
 */
public interface BookingService {

    /**
     * Creates a new booking based on the information provided in the booking request DTO.
     *
     * @param requestDto The BookingRequestDto object containing the booking details.
     * @return The BookingResponseDto object representing the created booking.
     */
    BookingResponseDto create(BookingRequestDto requestDto);

    /**
     * Retrieves a specific booking by the user ID and booking ID.
     *
     * @param userId    The ID of the user.
     * @param bookingId The ID of the booking.
     * @return The BookingResponseDto object representing the retrieved booking.
     */
    BookingResponseDto getById(Long userId, Long bookingId);

    /**
     * Approves or rejects a booking based on the provided approval status.
     *
     * @param userId    The ID of the user.
     * @param bookingId The ID of the booking.
     * @param approved  A boolean flag indicating approval status.
     * @return The BookingResponseDto object representing the updated booking.
     */
    BookingResponseDto approveBooking(Long userId, Long bookingId, boolean approved);

    /**
     * Retrieves all bookings associated with a booker user based on the booker's ID and state.
     *
     * @param bookerId The ID of the booker user.
     * @param state    The state of the bookings to filter by.
     * @return A list of BookingResponseDto objects representing the bookings.
     */
    List<BookingResponseDto> getAllBookingsAtBooker(Long bookerId, String state);

    /**
     * Retrieves all bookings associated with an owner user based on the owner's ID and state.
     *
     * @param ownerId The ID of the owner user.
     * @param state   The state of the bookings to filter by.
     * @return A list of BookingResponseDto objects representing the bookings.
     */
    List<BookingResponseDto> getAllBookingsAtOwner(Long ownerId, String state);
}
