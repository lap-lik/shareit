package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInputDTO;
import ru.practicum.shareit.request.dto.ItemRequestOutputDTO;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestOutputDTO createItemRequest(@RequestHeader(REQUEST_HEADER_USER_ID) Long requesterId,
                                                  @RequestBody final ItemRequestInputDTO inputDTO) {

        log.info("START endpoint `method:POST /requests` (create itemRequest), request: {}.", inputDTO.getDescription());

        return itemRequestService.create(requesterId, inputDTO);
    }

    @GetMapping
    public List<ItemRequestOutputDTO> getAllByRequesterId(@RequestHeader(REQUEST_HEADER_USER_ID) Long requesterId) {

        log.info("START endpoint `method:GET /requests` (get all itemRequests by requester id), requester id: {}.", requesterId);

        return itemRequestService.getAllByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutputDTO> getAll(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                             @RequestParam Integer from,
                                             @RequestParam Integer size) {

        log.info("START endpoint `method:GET /requests` (get all itemRequests), user id: {}.", userId);

        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutputDTO getByRequestId(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                               @PathVariable Long requestId) {

        log.info("START endpoint `method:GET /requests/:requestId` (get itemRequest by id), itemRequest id: {}.", requestId);

        return itemRequestService.getByRequestId(userId, requestId);
    }
}