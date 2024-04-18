package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
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
     * <p>
     * or whose description contains the specified string (case-insensitive) and are available.
     *
     * @param name        The string to search in the item name.
     * @param description The string to search in the item description.
     * @return A list of items that meet the search criteria and are available.
     */
    List<Item> findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description);

    /**
     * Check if an item exists by its ID and owner ID.
     *
     * @param itemId  The ID of the item.
     * @param ownerId The ID of the owner.
     * @return True if an item with the specified ID exists and is owned by the specified owner, false otherwise.
     */
    boolean existsItemByIdAndOwner_Id(Long itemId, Long ownerId);
}
