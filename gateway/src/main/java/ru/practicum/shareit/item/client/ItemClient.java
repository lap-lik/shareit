package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentInputDTO;
import ru.practicum.shareit.item.dto.ItemInputDTO;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";
    private static final String CREATE_ITEM_PATCH = "";
    private static final String UPDATE_PATCH = "/%d";
    private static final String GET_PATCH = "/%d";
    private static final String GET_ALL_PATCH = "?from=%d&size=%d";
    private static final String SEARCH_BY_TEXT_PATCH = "/search?text=%s&from=%d&size=%d";
    private static final String CREATE_COMMON_PATCH = "/%d/comment";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(long ownerId, ItemInputDTO inputDTO) {

        return post(CREATE_ITEM_PATCH, ownerId, inputDTO);
    }

    public ResponseEntity<Object> updateItem(long ownerId, long itemId, ItemInputDTO inputDTO) {

        String url = String.format(UPDATE_PATCH, itemId);

        return patch(url, ownerId, inputDTO);
    }

    public ResponseEntity<Object> getItemById(long userId, long itemId) {

        String url = String.format(GET_PATCH, itemId);

        return get(url, userId);
    }

    public ResponseEntity<Object> getAllItems(long ownerId, Integer from, Integer size) {

        String url = String.format(GET_ALL_PATCH, from, size);

        return get(url, ownerId);
    }

    public ResponseEntity<Object> searchItemsByText(String text, Integer from, Integer size) {

        String url = String.format(SEARCH_BY_TEXT_PATCH, text, from, size);

        return get(url);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentInputDTO inputDTO) {

        String url = String.format(CREATE_COMMON_PATCH, itemId);

        return post(url, userId, inputDTO);
    }
}
