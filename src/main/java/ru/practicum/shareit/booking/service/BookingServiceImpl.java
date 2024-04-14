package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingDao;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingRequestMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingResponseMapper;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.exception.validation.Marker;
import ru.practicum.shareit.exception.validation.ValidatorUtils;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserResponseMapper;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.model.Status.*;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingDao bookingDao;
    private final UserDao userDao;
    private final ItemDao itemDao;
    private final BookingRequestMapper bookingRequestMapper;
    private final BookingResponseMapper bookingResponseMapper;
    private final ItemResponseMapper itemResponseMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    @Transactional
    public BookingResponseDto create(BookingRequestDto requestDto) {

        UserResponseDto booker = findUserById(requestDto.getBookerId());
        ItemResponseDto item = findItemById(requestDto.getItemId());
        Long bookerId = booker.getId();
        Long itemId = item.getId();

        boolean isItemExists = itemDao.existsItemByIdAndOwner_Id(itemId, bookerId);

        if (isItemExists) {
            throw NotFoundException.builder()
                    .message(String.format("The user with an ID - `%d` is creating item with an ID - `%d` and cannot booking it.", itemId, bookerId))
                    .build();
        }

        requestDto.setStatus(WAITING);
        ValidatorUtils.validate(requestDto, Marker.OnCreate.class);
        validateDateTime(requestDto);

        BookingResponseDto responseDto = bookingResponseMapper.toDto(bookingDao.save(bookingRequestMapper.toEntity(requestDto)));
        responseDto.setBooker(booker);
        responseDto.setItem(item);

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getById(Long userId, Long bookingId) {

        return bookingResponseMapper.toDto(
                bookingDao.findBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(bookingId, userId, bookingId, userId)
                        .orElseThrow(() -> NotFoundException.builder()
                                .message(String.format("The booking with the ID - `%d` was not found.", bookingId))
                                .build()));
    }

    @Override
    @Transactional
    public BookingResponseDto approvedBooking(Long ownerId, Long bookingId, boolean approved) {

        BookingResponseDto responseDto = bookingResponseMapper.toDto(
                bookingDao.findById(bookingId).orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The booking with the ID - `%d` was not found.", bookingId))
                        .build()));
        Long itemId = responseDto.getItem().getId();

        if (responseDto.getStatus() == APPROVED) {
            throw ValidException.builder()
                    .message(String.format("The item with the ID - `%d` has already been booked.", itemId))
                    .build();
        }

        boolean isItemExists = itemDao.existsItemByIdAndOwner_Id(itemId, ownerId);

        if (!isItemExists) {
            throw NotFoundException.builder()
                    .message(String.format("The item with the ID - `%d` does not belong to the user with the ID - `%d`.", itemId, ownerId))
                    .build();
        }

        if (approved) {
            bookingDao.approvedBooking(bookingId, APPROVED.toString());
            responseDto.setStatus(APPROVED);
        } else {
            bookingDao.approvedBooking(bookingId, REJECTED.toString());
            responseDto.setStatus(REJECTED);
        }

        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookingsAtBooker(Long bookerId, String queryState) {

        LocalDateTime now = LocalDateTime.now();
        State state = checkState(queryState);
        findUserById(bookerId);

        switch (state) {
            case ALL:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByBooker_IdOrderByStartDesc(bookerId));
            case CURRENT:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(bookerId, now, now));
            case PAST:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(bookerId, now));
            case FUTURE:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(bookerId, now));
            case WAITING:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByBooker_IdAndStatusOrderByStartDesc(bookerId, WAITING));
            case REJECTED:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByBooker_IdAndStatusOrderByStartDesc(bookerId, REJECTED));
            default:
                throw UnsupportedException.builder()
                        .message(String.format("Unknown state: %s", queryState))
                        .build();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookingsAtOwner(Long ownerId, String queryState) {

        LocalDateTime now = LocalDateTime.now();
        State state = checkState(queryState);
        findUserById(ownerId);

        switch (state) {
            case ALL:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByItem_Owner_IdOrderByStartDesc(ownerId));
            case CURRENT:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId, now, now));
            case PAST:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(ownerId, now));
            case FUTURE:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(ownerId, now));
            case WAITING:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, WAITING));
            case REJECTED:
                return bookingResponseMapper.toDtos(
                        bookingDao.findAllByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, REJECTED));
            default:
                throw UnsupportedException.builder()
                        .message(String.format("Unknown state: %s", queryState))
                        .build();
        }
    }

    private State checkState(String queryState) {

        try {
            return State.valueOf(queryState);
        } catch (IllegalArgumentException e) {
            throw UnsupportedException.builder()
                    .message(String.format("Unknown state: %s", queryState))
                    .build();
        }
    }

    private void validateDateTime(BookingRequestDto requestDto) {

        LocalDateTime start = requestDto.getStart();
        LocalDateTime end = requestDto.getEnd();

        if (start.isAfter(end)) {
            throw ValidException.builder()
                    .message("The start of the booking cannot be later than the end of the booking.")
                    .build();
        }
        if (start.equals(end)) {
            throw ValidException.builder()
                    .message("The beginning of the booking cannot be the end of the booking.")
                    .build();
        }
    }

    private UserResponseDto findUserById(Long userId) {

        return userResponseMapper.toDto(userDao.findById(userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user with the ID - `%d` was not found.", userId))
                        .build()));
    }

    private ItemResponseDto findItemById(Long itemId) {

        ItemResponseDto item = itemResponseMapper.toDto(itemDao.findById(itemId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The item with the ID - `%d` was not found.", itemId))
                        .build()));

        if (!item.isAvailable()) {
            throw ValidException.builder()
                    .message(String.format("The item with the ID - `%d` is not available for rent.", itemId))
                    .build();
        }

        return item;
    }
}
