package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

/**
 * The ItemService interface represents a service for managing item.
 */
public interface ItemService {
    /**
     * Creates a new item based on the provided item request data.
     *
     * @param inputDTO The data required to create the new item.
     * @return The response containing the details of the created item.
     */
    ItemShortOutputDTO create(Long ownerId, ItemInputDTO inputDTO);

    /**
     * Updates an existing item owned by the specified owner ID with the provided item request data.
     *
     * @param ownerId  The ID of the owner of the item to be updated.
     * @param inputDTO The data required to update the item.
     * @return The response containing the updated item details.
     */
    ItemShortOutputDTO update(Long ownerId, ItemInputDTO inputDTO);

    /**
     * Retrieves an item by ID along with its associated bookings and comments.
     *
     * @param userId The ID of the user requesting the item details.
     * @param itemId The ID of the item to retrieve.
     * @return The item details, bookings, and comments associated with the item.
     */
    ItemOutputDTO getById(Long userId, Long itemId);

    /**
     * Retrieves all items by the specified owner ID along with their associated bookings and comments.
     *
     * @param ownerId The ID of the owner whose items are to be retrieved.
     * @return A list of items with their bookings and comments owned by the specified owner.
     */
    List<ItemOutputDTO> getAllByOwnerId(Long ownerId, Integer from, Integer size);

    /**
     * Searches for items based on the provided text keyword.
     *
     * @param text The keyword to search for in item descriptions or names.
     * @return A list of items matching the search text along with their details.
     */
    List<ItemShortOutputDTO> searchItemsByText(String text, Integer from, Integer size);

    /**
     * Adds a comment to the specified item by the provided user.
     *
     * @param userId   The ID of the user adding the comment.
     * @param itemId   The ID of the item to add the comment to.
     * @param inputDTO The data required to add the comment.
     * @return The response containing the details of the added comment.
     */
    CommentOutputDTO addComment(Long userId, Long itemId, CommentInputDTO inputDTO);
}

