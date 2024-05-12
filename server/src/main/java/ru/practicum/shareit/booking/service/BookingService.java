package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.dto.BookingOutputDTO;

import java.util.List;

/**
 * The BookingService interface represents a service for managing booking.
 */
public interface BookingService {

    /**
     * Creates a new booking based on the information provided in the booking request DTO.
     *
     * @param bookerId The ID of the booker user.
     * @param inputDTO The BookingRequestDto object containing the booking details.
     * @return The BookingResponseDto object representing the created booking.
     */
    BookingOutputDTO create(Long bookerId, BookingInputDTO inputDTO);

    /**
     * Retrieves a specific booking by the user ID and booking ID.
     *
     * @param userId    The ID of the user.
     * @param bookingId The ID of the booking.
     * @return The BookingResponseDto object representing the retrieved booking.
     */
    BookingOutputDTO getById(Long userId, Long bookingId);

    /**
     * Approves or rejects a booking based on the provided approval status.
     *
     * @param userId    The ID of the user.
     * @param bookingId The ID of the booking.
     * @param approved  A boolean flag indicating approval status.
     * @return The BookingResponseDto object representing the updated booking.
     */
    BookingOutputDTO approveBooking(Long userId, Long bookingId, boolean approved);

    /**
     * Retrieves all bookings associated with a booker user based on the booker's ID and state.
     *
     * @param bookerId The ID of the booker user.
     * @param state    The state of the bookings to filter by.
     * @param from     The index of the first booking to retrieve (defaultValue = "0").
     * @param size     The maximum number of bookings to retrieve (defaultValue = "20").
     * @return A list of BookingResponseDto objects representing the bookings.
     */
    List<BookingOutputDTO> getAllBookingsAtBooker(Long bookerId, String state, Integer from, Integer size);

    /**
     * Retrieves all bookings associated with an owner user based on the owner's ID and state.
     *
     * @param ownerId The ID of the owner user.
     * @param state   The state of the bookings to filter by.
     * @param from    The index of the first booking to retrieve (defaultValue = "0").
     * @param size    The maximum number of bookings to retrieve (defaultValue = "20").
     * @return A list of BookingResponseDto objects representing the bookings.
     */
    List<BookingOutputDTO> getAllBookingsAtOwner(Long ownerId, String state, Integer from, Integer size);
}
