package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingDao;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.dto.BookingShortMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.validation.Marker;
import ru.practicum.shareit.exception.validation.ValidatorUtils;
import ru.practicum.shareit.item.dao.CommentDao;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserResponseMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.REJECTED;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;
    private final BookingDao bookingDao;
    private final CommentDao commentDao;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemResponseMapper itemResponseMapper;
    private final ItemWithBookingsMapper itemWithBookingsMapper;
    private final BookingShortMapper bookingShortMapper;
    private final CommentRequestMapper commentRequestMapper;
    private final CommentResponseMapper commentResponseMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    @Transactional
    public ItemResponseDto create(ItemRequestDto requestDto) {

        ValidatorUtils.validate(requestDto, Marker.OnCreate.class);

        Long ownerId = requestDto.getOwnerId();
        checkExistsUserById(ownerId);

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

        boolean itemCreatedByUser = itemDao.existsItemByIdAndOwner_Id(itemId, userId);
        List<CommentResponseDto> comments = commentResponseMapper.toDtos(commentDao.findAllByItem_IdOrderByCreatedDesc(itemId));

        if (!itemCreatedByUser) {
            responseItem.setComments(comments);
            return responseItem;
        }


        BookingShortDto lastBooking = bookingShortMapper.toDto(
                bookingDao.findFirstByItem_IdAndStartIsBeforeAndStatusIsNotOrderByStartDesc(itemId, now, REJECTED).orElse(null));
        BookingShortDto nextBooking = bookingShortMapper.toDto(
                bookingDao.findFirstByItem_IdAndStartIsAfterAndStatusIsNotOrderByStartAsc(itemId, now, REJECTED).orElse(null));

        return responseItem.toBuilder()
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemWithBookingsDto> getAllByOwnerId(Long ownerId) {

        LocalDateTime now = LocalDateTime.now();
        List<ItemWithBookingsDto> responseDto = itemWithBookingsMapper.toDtos(itemDao.findAllByOwnerIdOrderById(ownerId));

        return responseDto.stream().map(itemDto -> {
            Long itemId = itemDto.getId();
            List<CommentResponseDto> comments = commentResponseMapper.toDtos(commentDao.findAllByItem_IdOrderByCreatedDesc(itemId));

            BookingShortDto lastBooking = bookingShortMapper.toDto(
                    bookingDao.findFirstByItem_IdAndStartIsBeforeAndStatusIsNotOrderByStartDesc(itemId, now, REJECTED).orElse(null));
            BookingShortDto nextBooking = bookingShortMapper.toDto(
                    bookingDao.findFirstByItem_IdAndStartIsAfterAndStatusIsNotOrderByStartAsc(itemId, now, REJECTED).orElse(null));

            return itemDto.toBuilder()
                    .lastBooking(lastBooking)
                    .nextBooking(nextBooking)
                    .comments(comments)
                    .build();
        }).collect(Collectors.toList());
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

    @Override
    public CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto requestDto) {
        LocalDateTime now = LocalDateTime.now();
        UserResponseDto userResponseDto = checkExistsUserById(userId);
        checkExistsItemById(itemId);

        boolean booking = bookingDao.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(itemId, userId, APPROVED, now);
        if (!booking) {
            throw ValidException.builder()
                    .message(String.format("The user with with the ID - `%d` did not rent item with the ID - `%d`.", userId, itemId))
                    .build();
        }

        requestDto.setCreated(now);
        requestDto.setAuthorId(userId);
        requestDto.setItemId(itemId);
        ValidatorUtils.validate(requestDto);

        CommentResponseDto responseDto = commentResponseMapper.toDto(commentDao.save(commentRequestMapper.toEntity(requestDto)));
        responseDto.setAuthorName(userResponseDto.getName());

        return responseDto;
    }

    private void checkExistsItemById(Long itemId) {

        boolean isItemExists = itemDao.existsById(itemId);
        if (!isItemExists) {
            throw NotFoundException.builder()
                    .message(String.format("The item with the ID - `%d` was not found.", itemId))
                    .build();
        }
    }


    private UserResponseDto checkExistsUserById(Long userId) {

        return userResponseMapper.toDto(userDao.findById(userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user with the ID - `%d` was not found.", userId))
                        .build()));
    }
}