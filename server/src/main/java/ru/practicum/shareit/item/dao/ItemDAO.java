package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * The ItemDao interface represents a data access object for managing items.
 * It extends the JpaRepository interface with Item as the entity type.
 *
 * @see JpaRepository
 */
public interface ItemDAO extends JpaRepository<Item, Long> {

    /**
     * Check if an item exists by its ID.
     *
     * @param itemId The ID of the item.
     * @return True if an item with the specified ID exists, false otherwise.
     */
    boolean existsById(Long itemId);

    /**
     * Delete items by owner ID.
     *
     * @param ownerId The ID of the owner.
     */
    void deleteByOwnerId(Long ownerId);

    /**
     * Find all items by owner ID and order them by ID.
     *
     * @param ownerId The ID of the owner.
     * @return A list of items owned by the specified owner, ordered by ID.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM items AS i " +
                    "LEFT JOIN users u on u.id = i.owner_id " +
                    "LEFT JOIN requests r on r.id = i.request_id " +
                    "WHERE i.owner_id = :ownerId " +
                    "ORDER BY i.id LIMIT :size OFFSET :from")
    List<Item> findAllByOwnerIdOrderById(Long ownerId, Integer from, Integer size);

    /**
     * Find all items whose name contains the specified string (case-insensitive) and are available,
     * or whose description contains the specified string (case-insensitive) and are available.
     *
     * @param text The text to search for in the name or description of the items.
     * @return A list of items whose name or description contains the specified text.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM items AS i " +
                    "WHERE (name ILIKE CONCAT('%', :text, '%') AND available = TRUE) " +
                    "OR (description ILIKE CONCAT('%', :text, '%') AND available = TRUE)" +
                    "ORDER BY i.id LIMIT :size OFFSET :from")
    List<Item> findAllByNameOrDescriptionContains(String text, Integer from, Integer size);

    /**
     * Find all items associated with a specific request ID.
     *
     * @param requestId The ID of the request for which items need to be retrieved.
     * @return A list of items associated with the specified request ID.
     */
    List<Item> findAllByRequest_Id(Long requestId);

    /**
     * Retrieves a list of items based on a provided list of item IDs.
     *
     * @param requestIds A list of request IDs for which items need to be retrieved.
     * @return A list of items corresponding to the provided list of item IDs.
     */
    List<Item> findAllByRequest_IdIn(List<Long> requestIds);

    /**
     * Check if an item exists by its ID and owner ID.
     *
     * @param itemId  The ID of the item.
     * @param ownerId The ID of the owner.
     * @return True if an item with the specified ID exists and is owned by the specified owner, false otherwise.
     */
    boolean existsItemByIdAndOwner_Id(Long itemId, Long ownerId);
}
