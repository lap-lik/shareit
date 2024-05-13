package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.validation.Marker;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestInputDTO;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItemRequest(@RequestHeader(REQUEST_HEADER_USER_ID) Long requesterId,
                                                    @Valid @RequestBody final ItemRequestInputDTO inputDTO) {

        log.info("START endpoint `method:POST /requests` (create itemRequest), request: {}.", inputDTO.getDescription());

        return client.createItemRequest(requesterId, inputDTO);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByRequestId(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                 @PathVariable Long requestId) {

        log.info("START endpoint `method:GET /requests/:requestId` (get itemRequest by id), itemRequest id: {}.", requestId);

        return client.getByRequestId(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "20") @Positive Integer size) {

        log.info("START endpoint `method:GET /requests` (get all itemRequests), user id: {}.", userId);

        return client.getAll(userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequesterId(@RequestHeader(REQUEST_HEADER_USER_ID) Long requesterId) {

        log.info("START endpoint `method:GET /requests` (get all itemRequests by requester id), requester id: {}.", requesterId);

        return client.getAllByRequesterId(requesterId);
    }
}
