package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotImplementedException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestMapper;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.validation.Marker;
import ru.practicum.shareit.validation.ValidatorUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemResponseMapper itemResponseMapper;

    @Override
    public ItemResponseDto create(ItemRequestDto requestDto) {

        Long ownerId = requestDto.getOwnerId();
        checkExistsOwnerById(ownerId);

        ValidatorUtils.validate(requestDto, Marker.OnCreate.class);

        return itemResponseMapper.toDto(itemDao.save(itemRequestMapper.toEntity(requestDto)));
    }

    @Override
    public ItemResponseDto update(Long ownerId, ItemRequestDto requestDto) {

        Long itemId = requestDto.getId();
        ItemRequestDto itemFromDB = itemRequestMapper.toDto(itemDao.findById(itemId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The item with the ID - `%d` was not found.", itemId))
                        .build()));

        if (!Objects.equals(ownerId, itemFromDB.getOwnerId())) {
            throw NotFoundException.builder()
                    .message(String.format("The item with the ID - `%d` was created by another user.", itemId))
                    .build();
        }

        requestDto.setOwnerId(ownerId);
        String itemName = requestDto.getName();
        String itemDescription = requestDto.getDescription();
        Boolean itemAvailable = requestDto.getAvailable();

        if (Objects.isNull(itemName)) {
            requestDto.setName(itemFromDB.getName());
        }
        if (Objects.isNull(itemDescription)) {
            requestDto.setDescription(itemFromDB.getDescription());
        }
        if (Objects.isNull(itemAvailable)) {
            requestDto.setAvailable(itemFromDB.getAvailable());
        }

        ValidatorUtils.validate(requestDto, Marker.OnUpdate.class);

        return itemResponseMapper.toDto(itemDao.update(itemRequestMapper.toEntity(requestDto)));
    }

    @Override
    public ItemResponseDto getById(Long itemId) {

        return itemResponseMapper.toDto(itemDao.findById(itemId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The item with the ID - `%d` was not found.", itemId))
                        .build()));
    }

    @Override
    public List<ItemResponseDto> getAll() {

        throw NotImplementedException.builder()
                .message("The method of getAllItems is not implemented.")
                .build();
    }

    @Override
    public void deleteById(Long itemId) {

        throw NotImplementedException.builder()
                .message(String.format("The method of deleting an item by ID - `%d` is not implemented.", itemId))
                .build();
    }

    @Override
    public List<ItemResponseDto> getAllByOwnerId(Long ownerId) {

        checkExistsOwnerById(ownerId);

        return itemResponseMapper.toDtos(itemDao.findAllByOwnerId(ownerId));
    }

    @Override
    public List<ItemResponseDto> searchItemsByText(String text) {

        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemResponseMapper.toDtos(itemDao.findAllByText(text.toLowerCase()));
    }

    private void checkExistsOwnerById(Long ownerId) {

        boolean isUserExists = userDao.existsById(ownerId);
        if (!isUserExists) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID - `%d` was not found.", ownerId))
                    .build();
        }
    }
}