package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingDao;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingResponseMapper;
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
import java.util.*;
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
    private final BookingResponseMapper bookingResponseMapper;
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
        ItemRequestDto itemFromDB = getItemRequestDto(ownerId, itemId);
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
        requestDto.setOwnerId(ownerId);

        ValidatorUtils.validate(requestDto, Marker.OnUpdate.class);

        return itemResponseMapper.toDto(itemDao.save(itemRequestMapper.toEntity(requestDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemWithBookingsAndCommentsDto getById(Long userId, Long itemId) {

        ItemWithBookingsAndCommentsDto responseItem = itemWithBookingsMapper.toDto(itemDao.findById(itemId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The item with the ID - `%d` was not found.", itemId))
                        .build()));

        List<CommentResponseDto> comments = commentResponseMapper.toDtos(commentDao.findAllByItem_IdOrderByCreatedDesc(itemId));
        if (!itemDao.existsItemByIdAndOwner_Id(itemId, userId)) {
            responseItem.setComments(comments);
            return responseItem;
        }

        List<BookingResponseDto> bookings = bookingResponseMapper.toDtos(bookingDao.findAllByItem_IdAndStatusIsNot(itemId, REJECTED));
        LocalDateTime now = LocalDateTime.now();

        return getItemWithBookingsAndCommentsDto(responseItem, comments, bookings, now);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemWithBookingsAndCommentsDto> getAllByOwnerId(Long ownerId) {

        List<ItemWithBookingsAndCommentsDto> responseItems = itemWithBookingsMapper.toDtos(itemDao.findAllByOwnerIdOrderById(ownerId));
        List<Long> itemsIds = responseItems.stream()
                .map(ItemWithBookingsAndCommentsDto::getId)
                .collect(Collectors.toList());
        Map<Long, List<BookingResponseDto>> bookings = bookingDao.findAllByItem_IdInAndStatusIsNot(itemsIds, REJECTED).stream()
                .map(bookingResponseMapper::toDto)
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
        Map<Long, List<CommentResponseDto>> comments = commentDao.findAllByItem_IdInOrderByCreatedDesc(itemsIds).stream()
                .map(commentResponseMapper::toDto)
                .collect(Collectors.groupingBy(CommentResponseDto::getItemId));
        LocalDateTime now = LocalDateTime.now();

        return responseItems.stream().map(itemDto -> {
            Long itemId = itemDto.getId();
            return getItemWithBookingsAndCommentsDto(itemDto, comments.get(itemId), bookings.get(itemId), now);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> searchItemsByText(String text) {

        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemResponseMapper.toDtos(itemDao
                .findAllByNameOrDescriptionContains(text));
    }

    @Override
    public CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto requestDto) {

        UserResponseDto userResponseDto = checkExistsUserById(userId);
        checkExistsItemById(itemId);

        LocalDateTime now = LocalDateTime.now();

        boolean isBookingConfirmed  = bookingDao.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(itemId, userId, APPROVED, now);
        if (!isBookingConfirmed ) {
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

    private ItemRequestDto getItemRequestDto(Long ownerId, Long itemId) {

        ItemRequestDto itemFromDB = itemRequestMapper.toDto(itemDao.findById(itemId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The item with the ID - `%d` was not found.", itemId))
                        .build()));

        if (!Objects.equals(ownerId, itemFromDB.getOwnerId())) {
            throw NotFoundException.builder()
                    .message(String.format("The item with the ID - `%d` was created by another user.", itemId))
                    .build();
        }

        return itemFromDB;
    }

    private ItemWithBookingsAndCommentsDto getItemWithBookingsAndCommentsDto(ItemWithBookingsAndCommentsDto item,
                                                                             List<CommentResponseDto> comments,
                                                                             List<BookingResponseDto> bookings,
                                                                             LocalDateTime now) {

        if (Objects.isNull(bookings)) {
            return item.toBuilder()
                    .lastBooking(null)
                    .nextBooking(null)
                    .comments(comments)
                    .build();
        }

        BookingShortDto lastBooking = bookingShortMapper.responseDtoToShortDto(bookings.stream()
                .filter(booking -> booking.getStart().isBefore(now))
                .max(Comparator.comparing(BookingResponseDto::getStart))
                .orElse(null));

        BookingShortDto nextBooking = bookingShortMapper.responseDtoToShortDto(bookings.stream()
                .filter(booking -> booking.getStart().isAfter(now))
                .min(Comparator.comparing(BookingResponseDto::getStart))
                .orElse(null));

        return item.toBuilder()
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }

    private void checkExistsItemById(Long itemId) {

        if (!itemDao.existsById(itemId)) {
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