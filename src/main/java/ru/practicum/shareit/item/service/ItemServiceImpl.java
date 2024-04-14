package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingDao;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.BookingShortMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotImplementedException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.exception.validation.Marker;
import ru.practicum.shareit.exception.validation.ValidatorUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;
    private final BookingDao bookingDao;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemResponseMapper itemResponseMapper;
    private final ItemWithBookingsMapper itemWithBookingsMapper;
    private final BookingShortMapper bookingShortMapper;

    @Override
    @Transactional
    public ItemResponseDto create(ItemRequestDto requestDto) {

        ValidatorUtils.validate(requestDto, Marker.OnCreate.class);

        Long ownerId = requestDto.getOwnerId();
        checkExistsOwnerById(ownerId);

        return itemResponseMapper.toDto(itemDao.save(itemRequestMapper.toEntity(requestDto)));
    }

    @Override
    @Transactional
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

        return itemResponseMapper.toDto(itemDao.save(itemRequestMapper.toEntity(requestDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemWithBookingsDto getById(Long userId, Long itemId) {

        LocalDateTime now = LocalDateTime.now();
        ItemWithBookingsDto responseItem = itemWithBookingsMapper.toDto(itemDao.findById(itemId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The item with the ID - `%d` was not found.", itemId))
                        .build()));

        boolean isItemExists = itemDao.existsItemByIdAndOwner_Id(itemId, userId);
        if (!isItemExists){
            return responseItem;
        }
        
        BookingShortDto lastBooking = bookingShortMapper.toDto(
                bookingDao.findFirstByItem_IdAndEndIsBeforeOrderByEndDesc(itemId, now).orElse(null));
        BookingShortDto nextBooking = bookingShortMapper.toDto(
                bookingDao.findFirstByItem_IdAndStartIsAfterOrderByStartAsc(itemId, now).orElse(null));
        return responseItem.toBuilder()
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemWithBookingsDto> getAll() {

        throw NotImplementedException.builder()
                .message("The method of getAllItems is not implemented.")
                .build();
    }

    @Override
    @Transactional
    public void deleteById(Long itemId) {

        throw NotImplementedException.builder()
                .message(String.format("The method of deleting an item by ID - `%d` is not implemented.", itemId))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllByOwnerId(Long ownerId) {

        checkExistsOwnerById(ownerId);

        return itemResponseMapper.toDtos(itemDao.findAllByOwnerId(ownerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> searchItemsByText(String text) {

        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemResponseMapper.toDtos(itemDao
                .findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text));
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