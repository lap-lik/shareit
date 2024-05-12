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

        String url = "";

        return post(url, ownerId, inputDTO);
    }

    public ResponseEntity<Object> updateItem(long ownerId, long itemId, ItemInputDTO inputDTO) {

        String url = "/" + itemId;

        return patch(url, ownerId, inputDTO);
    }

    public ResponseEntity<Object> getItemById(long userId, long itemId) {

        String url = "/" + itemId;

        return get(url, userId);
    }

    public ResponseEntity<Object> getAllItems(long ownerId, Integer from, Integer size) {

        String url = String.format("?from=%d&size=%d", from, size);

        return get(url, ownerId);
    }

    public ResponseEntity<Object> searchItemsByText(String text, Integer from, Integer size) {

        String url = String.format("/search?text=%s&from=%d&size=%d", text, from, size);

        return get(url);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentInputDTO inputDTO) {

        String url = String.format("/%d/comment", itemId);

        return post(url, userId, inputDTO);
    }
}
