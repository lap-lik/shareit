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
public interface ItemDao extends JpaRepository<Item, Long> {

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
    List<Item> findAllByOwnerIdOrderById(Long ownerId);

    /**
     * Find all items whose name contains the specified string (case-insensitive) and are available,
     * or whose description contains the specified string (case-insensitive) and are available.
     *
     * @param text The text to search for in the name or description of the items.
     * @return A list of items whose name or description contains the specified text.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM items " +
                    "WHERE (name ILIKE CONCAT('%', :text, '%') AND available = TRUE) " +
                    "OR (description ILIKE CONCAT('%', :text, '%') AND available = TRUE);")
    List<Item> findAllByNameOrDescriptionContains(String text);

    /**
     * Check if an item exists by its ID and owner ID.
     *
     * @param itemId  The ID of the item.
     * @param ownerId The ID of the owner.
     * @return True if an item with the specified ID exists and is owned by the specified owner, false otherwise.
     */
    boolean existsItemByIdAndOwner_Id(Long itemId, Long ownerId);
}
