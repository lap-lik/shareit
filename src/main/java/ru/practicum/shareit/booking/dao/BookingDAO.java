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
public interface BookingDAO extends JpaRepository<Booking, Long> {

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
     * @param from     Index of object in DB.
     * @param pageable The pagination information for the query.
     * @return A Page of bookings made by the specified booker sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByBooker(Long bookerId, Integer from, Integer size);

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
     * and the end time is after the current time, ordered by start time in descending order.
     *
     * @param bookerId The ID of the booker.
     * @param now      The current local date time.
     * @param from     Index of object in DB.
     * @param pageable The pagination information for the query.
     * @return A Page of bookings made by the specified booker where the start time is before the current time
     * and the end time is after the current time, sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId AND b.start_data < :now AND b.end_data > :now " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime now, Integer from, Integer size);

    /**
     * Find all bookings made by a specific booker where the end time is before the current time, ordered by start time in descending order.
     *
     * @param bookerId The ID of the booker.
     * @param now      The current local date time.
     * @param from     Index of object in DB.
     * @param pageable The pagination information for the query.
     * @return A Page of bookings made by the specified booker where the end time is before the current time, sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId AND b.end_data < :now " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByBooker_IdAndEndIsBefore(Long bookerId, LocalDateTime now, Integer from, Integer size);

    /**
     * Find all bookings made by a specific booker where the start time is after the specified time,
     * ordered by start time in descending order.
     *
     * @param bookerId The ID of the booker.
     * @param now      The current local date time.
     * @param from     Index of object in DB.
     * @param pageable The pagination information for the query.
     * @return A Page of bookings made by the specified booker where the start time is after the specified time,
     * sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId AND b.start_data > :now " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByBooker_IdAndStartIsAfter(Long bookerId, LocalDateTime now, Integer from, Integer size);

    /**
     * Find all bookings made by a specific booker with the specified status, ordered by start time in descending order.
     *
     * @param bookerId The ID of the booker.
     * @param status   The status of the bookings to retrieve.
     * @param from     Index of object in DB.
     * @param pageable The pagination information for the query.
     * @return A Page of bookings made by the specified booker with the specified status, sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE b.booker_id = :bookerId AND b.status = :status " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByBooker_IdAndStatus(Long bookerId, String status, Integer from, Integer size);

    /**
     * Find all bookings for items owned by a specific user, ordered by start time in descending order.
     *
     * @param ownerId  The ID of the owner of the items.
     * @param pageable The pagination information for the query.
     * @param from     Index of object in DB.
     * @return A Page of bookings for items owned by the specified user, sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE i.owner_id = :ownerId " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByItem_Owner_Id(Long ownerId, Integer from, Integer size);

    /**
     * Find all bookings for items owned by a specific user where the start time is before the specified time
     * and the end time is after the specified time, ordered by start time in descending order.
     *
     * @param ownerId  The ID of the owner of the items.
     * @param now      The current local date time.
     * @param from     Index of object in DB.
     * @param pageable The pagination information for the query.
     * @return A Page of bookings for items owned by the specified user where the start time is before the specified time
     * and the end time is after the specified time, sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE i.owner_id = :ownerId AND b.start_data < :now AND b.end_data > :now " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime now, Integer from, Integer size);

    /**
     * Find all bookings for items owned by a specific user where the end time is before the specified time,
     * ordered by start time in descending order.
     *
     * @param ownerId  The ID of the owner of the items.
     * @param now      The current local date time.
     * @param from     Index of object in DB.
     * @param pageable The pagination information for the query.
     * @return A Page of bookings for items owned by the specified user where the end time is before the specified time,
     * sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE i.owner_id = :ownerId AND b.end_data < :now " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByItem_Owner_IdAndEndIsBefore(Long ownerId, LocalDateTime now, Integer from, Integer size);

    /**
     * Find all bookings for items owned by a specific user where the start time is after the specified time,
     * ordered by start time in descending order.
     *
     * @param ownerId  The ID of the owner of the items.
     * @param now      The current local date time.
     * @param from     Index of object in DB.
     * @param pageable The pagination information for the query.
     * @return A Page of bookings for items owned by the specified user where the start time is after the specified time,
     * sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE i.owner_id = :ownerId AND b.start_data > :now " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByItem_Owner_IdAndStartIsAfter(Long ownerId, LocalDateTime now, Integer from, Integer size);

    /**
     * Find all bookings for items owned by a specific user with the specified status,
     * ordered by start time in descending order.
     *
     * @param ownerId  The ID of the owner of the items.
     * @param status   The status of the bookings to retrieve.
     * @param from     Index of object in DB.
     * @param pageable The pagination information for the query.
     * @return A Page of bookings for items owned by the specified user with the specified status,
     * sorted by start time in descending order.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM bookings AS b " +
                    "LEFT JOIN users u on u.id = b.booker_id " +
                    "LEFT JOIN items i on i.id = b.item_id " +
                    "WHERE i.owner_id = :ownerId AND b.status = :status " +
                    "ORDER BY b.start_data DESC OFFSET :from LIMIT :size")
    List<Booking> findAllByItem_Owner_IdAndStatus(Long ownerId, String status, Integer from, Integer size);

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
