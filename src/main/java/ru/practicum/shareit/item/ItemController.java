package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.constant.UserConstant.REQUEST_HEADER_USER_ID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto createItem(@RequestHeader(REQUEST_HEADER_USER_ID) Long ownerId,
                                      @RequestBody final ItemRequestDto requestDto) {

        log.info("START endpoint `method:POST /items` (create item), request: {}.", requestDto.getName());
        requestDto.setOwnerId(ownerId);

        return itemService.create(requestDto);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@RequestHeader(REQUEST_HEADER_USER_ID) Long ownerId,
                                  @PathVariable Long itemId,
                                  @RequestBody ItemRequestDto requestDto) {

        log.info("START endpoint `method:PATCH /items/:itemId` (update item), item id: {}.", itemId);
        requestDto.setId(itemId);

        return itemService.update(ownerId, requestDto);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingsDto getItemById(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                           @PathVariable final Long itemId) {

        log.info("START endpoint `method:GET /items/{itemId}` (get item by id), item id: {}.", itemId);

        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemWithBookingsDto> getAllItems(@RequestHeader(REQUEST_HEADER_USER_ID) Long ownerId) {

        log.info("START endpoint `method:GET /items` (get all items by owner id).");

        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItemsByText(@RequestParam String text) {

        log.info("START endpoint `method:GET /items/search` (search items by text), text: {}.", text);

        return itemService.searchItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody CommentRequestDto requestDto) {

        log.info("START endpoint `method:POST /items/{itemId}/comment` (create comment to item by id), item id: {}.", itemId);

        return itemService.addComment(userId, itemId, requestDto);
    }
}
