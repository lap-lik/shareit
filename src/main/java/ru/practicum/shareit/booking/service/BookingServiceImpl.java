package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingDAO;
import ru.practicum.shareit.booking.dto.BookingInputDTO;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingOutputDTO;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShortOutputDTO;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserOutputDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.booking.model.Status.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingDAO bookingDAO;
    private final UserDAO userDAO;
    private final ItemDAO itemDAO;
    private final BookingMapper bookingMapper;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public BookingOutputDTO create(BookingInputDTO inputDTO) {

        UserOutputDTO booker = validateUserById(inputDTO.getBookerId());
        ItemShortOutputDTO item = validateItemById(inputDTO.getItemId());
        Long bookerId = booker.getId();
        Long itemId = item.getId();

        if (itemDAO.existsItemByIdAndOwner_Id(itemId, bookerId)) {
            throw NotFoundException.builder()
                    .message(String.format("The user with an ID - `%d` is creating item with an ID - `%d` and cannot booking it.", itemId, bookerId))
                    .build();
        }

        inputDTO.setStatus(WAITING);
        validateDateTime(inputDTO);

        BookingOutputDTO outputDto = bookingMapper.toOutputDTO(bookingDAO.save(bookingMapper.inputDTOToEntity(inputDTO)));
        outputDto.setBooker(booker);
        outputDto.setItem(item);

        return outputDto;
    }

    @Override
    public BookingOutputDTO getById(Long userId, Long bookingId) {

        return bookingMapper.toOutputDTO(
                bookingDAO.findBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(bookingId, userId, bookingId, userId)
                        .orElseThrow(() -> NotFoundException.builder()
                                .message(String.format("The booking with the ID - `%d` was not found.", bookingId))
                                .build()));
    }

    @Override
    @Transactional
    public BookingOutputDTO approveBooking(Long ownerId, Long bookingId, boolean approved) {

        BookingOutputDTO outputDto = bookingMapper.toOutputDTO(
                bookingDAO.findById(bookingId).orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The booking with the ID - `%d` was not found.", bookingId))
                        .build()));
        Long itemId = outputDto.getItem().getId();
        Status bookingStatus = outputDto.getStatus();

        validateBooking(ownerId, itemId, bookingStatus);

        if (approved) {
            bookingDAO.approvedBooking(bookingId, APPROVED.toString());
            outputDto.setStatus(APPROVED);
        } else {
            bookingDAO.approvedBooking(bookingId, REJECTED.toString());
            outputDto.setStatus(REJECTED);
        }

        return outputDto;
    }

    @Override
    public List<BookingOutputDTO> getAllBookingsAtBooker(Long bookerId, String queryState, Integer from, Integer size) {

        State state = validateState(queryState);
        validateUserById(bookerId);
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByBooker(bookerId, from, size));
            case CURRENT:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(bookerId, now, from, size));
            case PAST:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByBooker_IdAndEndIsBefore(bookerId, now, from, size));
            case FUTURE:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByBooker_IdAndStartIsAfter(bookerId, now, from, size));
            case WAITING:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByBooker_IdAndStatus(bookerId, WAITING.toString(), from, size));
            case REJECTED:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByBooker_IdAndStatus(bookerId, REJECTED.toString(), from, size));
            default:
                throw UnsupportedException.builder()
                        .message(String.format("Unknown state: %s", queryState))
                        .build();
        }
    }

    @Override
    public List<BookingOutputDTO> getAllBookingsAtOwner(Long ownerId, String queryState, Integer from, Integer size) {

        State state = validateState(queryState);
        validateUserById(ownerId);
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByItem_Owner_Id(ownerId, from, size));
            case CURRENT:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(ownerId, now, from, size));
            case PAST:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByItem_Owner_IdAndEndIsBefore(ownerId, now, from, size));
            case FUTURE:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByItem_Owner_IdAndStartIsAfter(ownerId, now, from, size));
            case WAITING:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByItem_Owner_IdAndStatus(ownerId, WAITING.toString(), from, size));
            case REJECTED:
                return bookingMapper.toOutputDTOs(
                        bookingDAO.findAllByItem_Owner_IdAndStatus(ownerId, REJECTED.toString(), from, size));
            default:
                throw UnsupportedException.builder()
                        .message(String.format("Unknown state: %s", queryState))
                        .build();
        }
    }

    private void validateBooking(Long ownerId, Long itemId, Status bookingStatus) {
        if (Objects.equals(bookingStatus, APPROVED)) {
            throw ValidException.builder()
                    .message(String.format("The item with the ID - `%d` has already been booked.", itemId))
                    .build();
        }

        if (!itemDAO.existsItemByIdAndOwner_Id(itemId, ownerId)) {
            throw NotFoundException.builder()
                    .message(String.format("The item with the ID - `%d` does not belong to the user with the ID - `%d`.", itemId, ownerId))
                    .build();
        }
    }

    private State validateState(String queryState) {

        try {
            return State.valueOf(queryState);
        } catch (IllegalArgumentException e) {
            throw UnsupportedException.builder()
                    .message(String.format("Unknown state: %s", queryState))
                    .build();
        }
    }

    private void validateDateTime(BookingInputDTO inputDTO) {

        LocalDateTime start = inputDTO.getStart();
        LocalDateTime end = inputDTO.getEnd();

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

    private UserOutputDTO validateUserById(Long userId) {

        return userMapper.toOutputDTO(userDAO.findById(userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user with the ID - `%d` was not found.", userId))
                        .build()));
    }

    private ItemShortOutputDTO validateItemById(Long itemId) {

        ItemShortOutputDTO shortOutputDto = itemMapper.toShortOutputDTO(itemDAO.findById(itemId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The item with the ID - `%d` was not found.", itemId))
                        .build()));

        if (!shortOutputDto.isAvailable()) {
            throw ValidException.builder()
                    .message(String.format("The item with the ID - `%d` is not available for rent.", itemId))
                    .build();
        }

        return shortOutputDto;
    }
}