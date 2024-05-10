package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * The CommentDao interface represents a data access object for managing comments.
 * It extends the JpaRepository interface with Comment as the entity type.
 *
 * @see JpaRepository
 */
public interface CommentDAO extends JpaRepository<Comment, Long> {

    /**
     * Retrieves a list of comments for a specific item ID, ordered by created date in descending order.
     *
     * @param itemId The ID of the item to retrieve comments for.
     * @return A list of comments for the specified item ID.
     */
    List<Comment> findAllByItem_IdOrderByCreatedDesc(Long itemId);

    /**
     * Retrieves a list of comments for multiple item IDs, ordered by created date in descending order.
     *
     * @param itemId A list of item IDs to retrieve comments for.
     * @return A list of comments for the specified item IDs.
     */
    List<Comment> findAllByItem_IdInOrderByCreatedDesc(List<Long> itemId);
}
