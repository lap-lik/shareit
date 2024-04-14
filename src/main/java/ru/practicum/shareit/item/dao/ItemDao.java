package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * The ItemDao interface represents a data access object for managing items.
 * It extends the GenericDao interface with Item as the entity type.
 * The methods provided in this interface are: remove and find all items by ownerId, and find items by text.
 *
 * @see JpaRepository
 */
public interface ItemDao extends JpaRepository<Item, Long> {

    boolean existsById(Long id);

    void deleteByOwnerId(Long ownerId);

    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description);

    boolean existsItemByIdAndOwner_Id(Long itemId, Long ownerId);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE items SET available = :approved  WHERE id = :itemId AND owner_id = :ownerId")
    int updateAvailabilityStatusToFalse(Long itemId, Long ownerId, boolean approved);
}
