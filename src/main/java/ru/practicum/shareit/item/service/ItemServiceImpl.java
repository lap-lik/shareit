package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingDAO;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingOutputDTO;
import ru.practicum.shareit.booking.dto.BookingShortOutputDTO;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidException;
import ru.practicum.shareit.item.dao.CommentDAO;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.dao.ItemRequestDAO;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserOutputDTO;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.REJECTED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemDAO itemDao;
    private final UserDAO userDao;
    private final BookingDAO bookingDao;
    private final CommentDAO commentDao;
    private final ItemRequestDAO itemRequestDao;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public ItemShortOutputDTO create(Long ownerId, ItemInputDTO inputDTO) {

        checkExistsUserById(ownerId);
        Long requestId = inputDTO.getRequestId();
        if (Objects.nonNull(requestId)) {
            checkExistsRequestById(requestId);
        }

        inputDTO.setOwnerId(ownerId);

        return itemMapper.toShortOutputDTO(itemDao.save(itemMapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    @Transactional
    public ItemShortOutputDTO update(Long ownerId, ItemInputDTO inputDTO) {

        checkExistsUserById(ownerId);
        Long itemId = inputDTO.getId();
        ItemInputDTO itemFromDB = getItemRequestDto(ownerId, itemId);
        String itemName = inputDTO.getName();
        String itemDescription = inputDTO.getDescription();
        Boolean itemAvailable = inputDTO.getAvailable();
        Long requestId = inputDTO.getRequestId();

        if (Objects.isNull(itemName)) {
            inputDTO.setName(itemFromDB.getName());
        }
        if (Objects.isNull(itemDescription)) {
            inputDTO.setDescription(itemFromDB.getDescription());
        }
        if (Objects.isNull(itemAvailable)) {
            inputDTO.setAvailable(itemFromDB.getAvailable());
        }
        if (Objects.isNull(requestId)) {
            if (Objects.nonNull(itemFromDB.getRequestId())) {
                inputDTO.setRequestId(itemFromDB.getRequestId());
            }
        } else {
            checkExistsRequestById(requestId);
        }
        inputDTO.setOwnerId(ownerId);

        return itemMapper.toShortOutputDTO(itemDao.save(itemMapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    public ItemOutputDTO getById(Long userId, Long itemId) {

        checkExistsUserById(userId);
        ItemOutputDTO outputDto = itemMapper.toItemOutputDTO(itemDao.findById(itemId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The item with the ID - `%d` was not found.", itemId))
                        .build()));

        List<CommentOutputDTO> comments = commentMapper.toOutputDTOs(commentDao.findAllByItem_IdOrderByCreatedDesc(itemId));
        if (!itemDao.existsItemByIdAndOwner_Id(itemId, userId)) {
            outputDto.setComments(comments);
            return outputDto;
        }

        List<BookingOutputDTO> bookings = bookingMapper.toOutputDTOs(bookingDao.findAllByItem_IdAndStatusIsNot(itemId, REJECTED));
        LocalDateTime now = LocalDateTime.now();

        return getItemWithBookingsAndComments(outputDto, comments, bookings, now);
    }

    @Override
    public List<ItemOutputDTO> getAllByOwnerId(Long ownerId, Integer from, Integer size) {

        checkExistsUserById(ownerId);
        List<ItemOutputDTO> responseItems = itemMapper.toItemOutputDTOs(itemDao.findAllByOwnerIdOrderById(ownerId, from, size));
        if (Objects.isNull(responseItems)) {
            return Collections.emptyList();
        }
        List<Long> itemsIds = responseItems.stream()
                .map(ItemOutputDTO::getId)
                .collect(Collectors.toList());
        Map<Long, List<BookingOutputDTO>> bookings = bookingDao.findAllByItem_IdInAndStatusIsNot(itemsIds, REJECTED).stream()
                .map(bookingMapper::toOutputDTO)
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
        Map<Long, List<CommentOutputDTO>> comments = commentDao.findAllByItem_IdInOrderByCreatedDesc(itemsIds).stream()
                .map(commentMapper::toOutputDTO)
                .collect(Collectors.groupingBy(CommentOutputDTO::getItemId));
        LocalDateTime now = LocalDateTime.now();

        return responseItems.stream().map(itemOutputDTO -> {
            Long itemId = itemOutputDTO.getId();
            return getItemWithBookingsAndComments(itemOutputDTO, comments.get(itemId), bookings.get(itemId), now);
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemShortOutputDTO> searchItemsByText(String text, Integer from, Integer size) {

        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemMapper.toShortOutputDTOs(itemDao.findAllByNameOrDescriptionContains(text, from, size));
    }

    @Override
    @Transactional
    public CommentOutputDTO addComment(Long userId, Long itemId, CommentInputDTO inputDTO) {

        UserOutputDTO userResponseDto = checkExistsUserById(userId);
        checkExistsItemById(itemId);

        LocalDateTime now = LocalDateTime.now();

        boolean isBookingConfirmed = bookingDao.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(itemId, userId, APPROVED, now);
        if (!isBookingConfirmed) {
            throw ValidException.builder()
                    .message(String.format("The user with with the ID - `%d` did not rent item with the ID - `%d`.", userId, itemId))
                    .build();
        }

        inputDTO.setCreated(now);
        inputDTO.setAuthorId(userId);
        inputDTO.setItemId(itemId);

        CommentOutputDTO responseDto = commentMapper.toOutputDTO(commentDao.save(commentMapper.inputDTOToEntity(inputDTO)));
        responseDto.setAuthorName(userResponseDto.getName());

        return responseDto;
    }

    private ItemInputDTO getItemRequestDto(Long ownerId, Long itemId) {

        ItemInputDTO itemFromDB = itemMapper.toInputDTO(itemDao.findById(itemId)
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

    private ItemOutputDTO getItemWithBookingsAndComments(ItemOutputDTO item,
                                                         List<CommentOutputDTO> comments,
                                                         List<BookingOutputDTO> bookings,
                                                         LocalDateTime now) {

        if (Objects.isNull(bookings)) {
            return item.toBuilder()
                    .lastBooking(null)
                    .nextBooking(null)
                    .comments(comments)
                    .build();
        }

        BookingShortOutputDTO lastBooking = bookingMapper.outputDTOToShortOutputDTO(bookings.stream()
                .filter(booking -> booking.getStart().isBefore(now))
                .max(Comparator.comparing(BookingOutputDTO::getStart))
                .orElse(null));

        BookingShortOutputDTO nextBooking = bookingMapper.outputDTOToShortOutputDTO(bookings.stream()
                .filter(booking -> booking.getStart().isAfter(now))
                .min(Comparator.comparing(BookingOutputDTO::getStart))
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

    private UserOutputDTO checkExistsUserById(Long userId) {

        return userMapper.toOutputDTO(userDao.findById(userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user with the ID - `%d` was not found.", userId))
                        .build()));
    }

    private void checkExistsRequestById(Long requestId) {

        if (!itemRequestDao.existsById(requestId)) {
            throw NotFoundException.builder()
                    .message(String.format("The itemRequest with the ID - `%d` was not found.", requestId))
                    .build();
        }
    }
}