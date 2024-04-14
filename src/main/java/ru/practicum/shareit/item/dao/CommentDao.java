package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * The CommentDao interface represents a data access object for managing items.
 * It extends the GenericDao interface with Item as the entity type.
 * The methods provided in this interface are: remove and find all items by ownerId, and find items by text.
 *
 * @see JpaRepository
 */

public interface CommentDao extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItem_IdOrderByCreatedDesc(Long itemId);
}
