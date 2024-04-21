package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The BookingDao interface represents a data access object for managing bookings.
 * It extends the GenericDao interface with Booking as the entity type.
 *
 * @see JpaRepository
 */
public interface BookingDao extends JpaRepository<Booking, Long> {
    /**
     * Finds a booking by the specified IDs and returns an Optional of Booking.
     *
     * @param id        The booking ID to search for.
     * @param bookerId  The booker ID to search for.
     * @param bookingId The booking ID to search for.
     * @param ownerId   The owner ID to search for.
     * @return An Optional of Booking if found, empty otherwise.
     */
    Optional<Booking> findBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(Long id, Long bookerId, Long bookingId, Long ownerId);

    /**
     * Find all bookings made by a specific booker ordered by start time in descending order.
     *
     * @param bookerId The ID of the booker.
     * @return A list of bookings made by the specified booker sorted by start time in descending order.
     */
    List<Booking> findAllByBooker_IdOrderByStartDesc(Long bookerId);

    /**
     * Find all bookings for a specific item where the status is not equal to the specified status.
     *
     * @param itemId The ID of the item.
     * @param status The status to exclude from results.
     * @return A list of bookings for the specified item with status not equal to the given status.
     */
    List<Booking> findAllByItem_IdAndStatusIsNot(Long itemId, Status status);

    /**
     * Find all bookings for a list of items where the status is not equal to the specified status.
     *
     * @param itemsIds The list of item IDs.
     * @param status   The status to exclude from results.
     * @return A list of bookings for the specified items with status not equal to the given status.
     */
    List<Booking> findAllByItem_IdInAndStatusIsNot(List<Long> itemsIds, Status status);

    /**
     * Find all bookings made by a specific booker where the start time is before the current time
     * <p>
     * and the end time is after the current time, ordered by start time in descending order.
     *
     * @param bookerId The ID of the booker.
     * @param now      The current local date time.
     * @param timeNow  The current local date time.
     * @return A list of bookings made by the specified booker where the start time is before the current time
     * <p>
     * and the end time is after the current time, sorted by start time in descending order.
     */
    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long bookerId, LocalDateTime now, LocalDateTime timeNow);

    /**
     * Find all bookings made by a specific booker where the end time is before the current time, ordered by start time in descending order.
     *
     * @param bookerId The ID of the booker.
     * @param now      The current local date time.
     * @return A list of bookings made by the specified booker where the end time is before the current time, sorted by start time in descending order.
     */
    List<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime now);

    /**
     * Find all bookings made by a specific booker where the start time is after the specified time,
     * <p>
     * ordered by start time in descending order.
     *
     * @param bookerId The ID of the booker.
     * @param now      The current local date time.
     * @return A list of bookings made by the specified booker where the start time is after the specified time,
     * <p>
     * sorted by start time in descending order.
     */
    List<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime now);

    /**
     * Find all bookings made by a specific booker with the specified status, ordered by start time in descending order.
     *
     * @param bookerId The ID of the booker.
     * @param status   The status of the bookings to retrieve.
     * @return A list of bookings made by the specified booker with the specified status, sorted by start time in descending order.
     */
    List<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(Long bookerId, Status status);

    /**
     * Find all bookings for items owned by a specific user, ordered by start time in descending order.
     *
     * @param ownerId The ID of the owner of the items.
     * @return A list of bookings for items owned by the specified user, sorted by start time in descending order.
     */
    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(Long ownerId);

    /**
     * Find all bookings for items owned by a specific user where the start time is before the specified time
     * <p>
     * and the end time is after the specified time, ordered by start time in descending order.
     *
     * @param ownerId The ID of the owner of the items.
     * @param now     The current local date time.
     * @param timeNow The current local date time.
     * @return A list of bookings for items owned by the specified user where the start time is before the specified time
     * <p>
     * and the end time is after the specified time, sorted by start time in descending order.
     */
    List<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long ownerId, LocalDateTime now, LocalDateTime timeNow);

    /**
     * Find all bookings for items owned by a specific user where the end time is before the specified time,
     * <p>
     * ordered by start time in descending order.
     *
     * @param ownerId The ID of the owner of the items.
     * @param now     The current local date time.
     * @return A list of bookings for items owned by the specified user where the end time is before the specified time,
     * <p>
     * sorted by start time in descending order.
     */
    List<Booking> findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime now);

    /**
     * Find all bookings for items owned by a specific user where the start time is after the specified time,
     * <p>
     * ordered by start time in descending order.
     *
     * @param ownerId The ID of the owner of the items.
     * @param now     The current local date time.
     * @return A list of bookings for items owned by the specified user where the start time is after the specified time,
     * <p>
     * sorted by start time in descending order.
     */
    List<Booking> findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(Long ownerId, LocalDateTime now);

    /**
     * Find all bookings for items owned by a specific user with the specified status,
     * <p>
     * ordered by start time in descending order.
     *
     * @param ownerId The ID of the owner of the items.
     * @param status  The status of the bookings to retrieve.
     * @return A list of bookings for items owned by the specified user with the specified status,
     * <p>
     * sorted by start time in descending order.
     */
    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(Long ownerId, Status status);

    /**
     * Check if a booking exists by item ID, booker ID, status, and end time before the specified time.
     *
     * @param itemId   The ID of the item.
     * @param bookerId The ID of the booker.
     * @param status   The status of the booking.
     * @param now      The current local date time.
     * @return True if a booking exists with the specified criteria, false otherwise.
     */
    boolean existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(Long itemId, Long bookerId, Status status, LocalDateTime now);

    /**
     * Update the status of a booking by booking ID.
     *
     * @param bookingId The ID of the booking.
     * @param status    The new status to set for the booking.
     */
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE bookings SET status = :status WHERE id = :bookingId")
    void approvedBooking(Long bookingId, String status);
}
