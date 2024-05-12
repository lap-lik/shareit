package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.constant.Constant.REQUEST_HEADER_USER_ID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemShortOutputDTO createItem(@RequestHeader(REQUEST_HEADER_USER_ID) long ownerId,
                                         @RequestBody final ItemInputDTO inputDTO) {

        log.info("START endpoint `method:POST /items` (create item), request: {}.", inputDTO.getName());

        return itemService.create(ownerId, inputDTO);
    }

    @PatchMapping("/{itemId}")
    public ItemShortOutputDTO update(@RequestHeader(REQUEST_HEADER_USER_ID) long ownerId,
                                     @PathVariable long itemId,
                                     @RequestBody ItemInputDTO inputDTO) {

        log.info("START endpoint `method:PATCH /items/{itemId}` (update item), item id: {}.", itemId);

        return itemService.update(ownerId, itemId, inputDTO);
    }

    @GetMapping("/{itemId}")
    public ItemOutputDTO getItemById(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                     @PathVariable long itemId) {

        log.info("START endpoint `method:GET /items/{itemId}` (get item by id), item id: {}.", itemId);

        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemOutputDTO> getAllItems(@RequestHeader(REQUEST_HEADER_USER_ID) long ownerId,
                                           @RequestParam(name = "from") int from,
                                           @RequestParam(name = "size") int size) {

        log.info("START endpoint `method:GET /items` (get all items by owner id), owner id: {}.", ownerId);

        return itemService.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemShortOutputDTO> searchItemsByText(@RequestParam String text,
                                                      @RequestParam(name = "from") int from,
                                                      @RequestParam(name = "size") int size) {

        log.info("START endpoint `method:GET /items/search` (search items by text), text: {}.", text);

        return itemService.searchItemsByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutputDTO addComment(@RequestHeader(REQUEST_HEADER_USER_ID) long userId,
                                       @PathVariable long itemId,
                                       @RequestBody CommentInputDTO inputDTO) {

        log.info("START endpoint `method:POST /items/{itemId}/comment` (create comment to item by id), item id: {}.", itemId);

        return itemService.addComment(userId, itemId, inputDTO);
    }
}
