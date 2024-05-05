package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShortOutputDTO;
import ru.practicum.shareit.request.dao.ItemRequestDAO;
import ru.practicum.shareit.request.dto.ItemRequestInputDTO;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestOutputDTO;
import ru.practicum.shareit.user.dao.UserDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestDAO itemRequestDAO;
    private final UserDAO userDAO;
    private final ItemDAO itemDAO;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestOutputDTO create(Long requesterId, ItemRequestInputDTO inputDTO) {

        checkExistsUserById(requesterId);
        inputDTO.setRequesterId(requesterId);

        return itemRequestMapper.toOutputDTO(itemRequestDAO.save(itemRequestMapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    public List<ItemRequestOutputDTO> getAllByRequesterId(Long requesterId) {

        checkExistsUserById(requesterId);
        List<ItemRequestOutputDTO> outputDTOs = itemRequestMapper.toOutputDTOs(itemRequestDAO.findAllByRequester_Id(requesterId));

        return setItemsToRequests(outputDTOs);
    }

    @Override
    public List<ItemRequestOutputDTO> getAll(Long userId, Integer from, Integer size) {

        checkExistsUserById(userId);

        List<ItemRequestOutputDTO> outputDTOs = itemRequestMapper.toOutputDTOs(itemRequestDAO.findAllFromOtherUsers(userId, from, size));

        return setItemsToRequests(outputDTOs);
    }


    private List<ItemRequestOutputDTO> setItemsToRequests(List<ItemRequestOutputDTO> outputDTOs) {
        List<Long> requestIds = outputDTOs.stream()
                .map(ItemRequestOutputDTO::getId)
                .collect(Collectors.toList());

        Map<Long, List<ItemShortOutputDTO>> items = itemDAO.findAllByRequest_IdIn(requestIds).stream()
                .map(itemMapper::toShortOutputDTO)
                .collect(Collectors.groupingBy(ItemShortOutputDTO::getRequestId));

        return outputDTOs.stream()
                .peek(r -> r.setItems(items.getOrDefault(r.getId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestOutputDTO getByRequestId(Long userId, Long requestId) {

        checkExistsUserById(userId);

        ItemRequestOutputDTO outputDTO = itemRequestMapper.toOutputDTO(itemRequestDAO.findById(requestId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The itemRequest with the ID - `%d` was not found.", requestId))
                        .build()));

        outputDTO.setItems(itemMapper.toShortOutputDTOs(itemDAO.findAllByRequest_Id(requestId)));

        return outputDTO;
    }

    private void checkExistsUserById(Long userId) {

        if (!userDAO.existsById(userId)) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID - `%d` was not found.", userId))
                    .build();
        }
    }
}
