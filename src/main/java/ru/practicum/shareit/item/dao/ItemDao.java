package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.generic.GenericDao;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * The ItemDao interface represents a data access object for managing items.
 * It extends the GenericDao interface with Item as the entity type.
 * The methods provided in this interface are: remove and find all items by ownerId, and find items by text.
 *
 * @see GenericDao
 */
public interface ItemDao extends GenericDao<Item> {

    void deleteByOwnerId(Long ownerId);

    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> findAllByText(String text);
}
