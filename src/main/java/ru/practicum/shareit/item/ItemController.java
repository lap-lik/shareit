package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.validation.Marker;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ItemShortOutputDTO createItem(@RequestHeader(REQUEST_HEADER_USER_ID) Long ownerId,
                                         @Valid @RequestBody final ItemInputDTO inputDTO) {

        log.info("START endpoint `method:POST /items` (create item), request: {}.", inputDTO.getName());

        return itemService.create(ownerId, inputDTO);
    }

    @PatchMapping("/{itemId}")
    @Validated(Marker.OnUpdate.class)
    public ItemShortOutputDTO update(@RequestHeader(REQUEST_HEADER_USER_ID) Long ownerId,
                                     @PathVariable Long itemId,
                                     @Valid @RequestBody ItemInputDTO inputDTO) {

        log.info("START endpoint `method:PATCH /items/{itemId}` (update item), item id: {}.", itemId);
        inputDTO.setId(itemId);

        return itemService.update(ownerId, inputDTO);
    }

    @GetMapping("/{itemId}")
    public ItemOutputDTO getItemById(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                     @PathVariable final Long itemId) {

        log.info("START endpoint `method:GET /items/{itemId}` (get item by id), item id: {}.", itemId);

        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemOutputDTO> getAllItems(@RequestHeader(REQUEST_HEADER_USER_ID) Long ownerId,
                                           @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(defaultValue = "20") @Positive Integer size) {

        log.info("START endpoint `method:GET /items` (get all items by owner id), owner id: {}.", ownerId);

        return itemService.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemShortOutputDTO> searchItemsByText(@RequestParam String text,
                                                      @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                      @RequestParam(defaultValue = "20") @Positive Integer size) {

        log.info("START endpoint `method:GET /items/search` (search items by text), text: {}.", text);

        return itemService.searchItemsByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutputDTO addComment(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                       @PathVariable Long itemId,
                                       @Valid @RequestBody CommentInputDTO inputDTO) {

        log.info("START endpoint `method:POST /items/{itemId}/comment` (create comment to item by id), item id: {}.", itemId);

        return itemService.addComment(userId, itemId, inputDTO);
    }
}
