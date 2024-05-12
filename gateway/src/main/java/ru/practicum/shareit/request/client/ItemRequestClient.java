package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestInputDTO;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/request";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }


    public ResponseEntity<Object> createItemRequest(long requesterId, ItemRequestInputDTO inputDTO) {

        String url = "";

        return post(url, requesterId, inputDTO);
    }

    public ResponseEntity<Object> getAllByRequesterId(long requesterId) {

        String url = "";

        return get(url, requesterId, null);
    }

    public ResponseEntity<Object> getAll(long userId, Integer from, Integer size) {

        String url = String.format("/all?from=%d&size=%d", from, size);

        return get(url, userId, null);
    }

    public ResponseEntity<Object> getByRequestId(long userId, long requestId) {

        String url = "/" + requestId;

        return get(url, userId);
    }
}
