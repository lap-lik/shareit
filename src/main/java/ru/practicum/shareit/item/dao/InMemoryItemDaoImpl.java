package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemDaoImpl implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, Set<Long>> itemIdsByOwnerId = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item save(Item item) {

        Long itemId = generateId();
        Long ownerId = item.getOwner().getId();
        item.setId(itemId);
        items.put(itemId, item);

        Set<Long> itemsIds = itemIdsByOwnerId.getOrDefault(ownerId, new HashSet<>());
        itemsIds.add(itemId);
        itemIdsByOwnerId.put(ownerId, itemsIds);

        return item;
    }

    @Override
    public Item update(Item item) {

        items.put(item.getId(), item);

        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {

        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {

        return new ArrayList<>(items.values());
    }

    @Override
    public void deleteById(Long id) {

        Item removeItem = items.remove(id);
        Long ownerId = removeItem.getOwner().getId();
        Set<Long> itemsIds = itemIdsByOwnerId.get(ownerId);
        itemsIds.remove(id);
    }

    @Override
    public boolean existsById(Long id) {

        return items.containsKey(id);
    }

    @Override
    public void deleteByOwnerId(Long ownerId) {

        Set<Long> removedItemsIds = itemIdsByOwnerId.remove(ownerId);
        if (Objects.isNull(removedItemsIds)) {
            return;
        }
        removedItemsIds.forEach(items::remove);
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {

        Set<Long> itemsIds = itemIdsByOwnerId.get(ownerId);

        return itemsIds.stream().map(items::get).collect(Collectors.toList());
    }

    @Override
    public List<Item> findAllByText(String text) {

        return items.values().stream()
                .filter(item -> item.getAvailable().equals(true) &&
                        (item.getName().toLowerCase().contains(text) ||
                                item.getDescription().toLowerCase().contains(text)))
                .collect(Collectors.toList());
    }

    private Long generateId() {

        return ++id;
    }
}
