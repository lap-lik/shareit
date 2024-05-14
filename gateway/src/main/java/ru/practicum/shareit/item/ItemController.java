package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.validation.Marker;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentInputDTO;
import ru.practicum.shareit.item.dto.ItemInputDTO;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient client;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestHeader(REQUEST_HEADER_USER_ID) long ownerId,
                                             @Valid @RequestBody final ItemInputDTO inputDTO) {

        log.info("START endpoint `method:POST /items` (create item), request: {}.", inputDTO.getName());

        return client.createItem(ownerId, inputDTO);
    }

    @PatchMapping("/{itemId}")
    @Validated(Marker.OnUpdate.class)
    public ResponseEntity<Object> updateItem(@RequestHeader(REQUEST_HEADER_USER_ID) long ownerId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody ItemInputDTO inputDTO) {

        log.info("START endpoint `method:PATCH /items/{itemId}` (update item), item id: {}.", itemId);

        return client.updateItem(ownerId, itemId, inputDTO);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                              @PathVariable long itemId) {

        log.info("START endpoint `method:GET /items/{itemId}` (get item by id), item id: {}.", itemId);

        return client.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(REQUEST_HEADER_USER_ID) long ownerId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "20") @Positive Integer size) {

        log.info("START endpoint `method:GET /items` (get all items by owner id), owner id: {}.", ownerId);

        return client.getAllItems(ownerId, from, size);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByText(@RequestParam String text,
                                                    @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(defaultValue = "20") @Positive Integer size) {

        log.info("START endpoint `method:GET /items/search` (search items by text), text: {}.", text);

        return client.searchItemsByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody CommentInputDTO inputDTO) {

        log.info("START endpoint `method:POST /items/{itemId}/comment` (create comment to item by id), item id: {}.", itemId);

        return client.addComment(userId, itemId, inputDTO);
    }
}
