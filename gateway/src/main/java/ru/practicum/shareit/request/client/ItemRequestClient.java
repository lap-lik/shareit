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

    private static final String API_PREFIX = "/requests";
    private static final String CREATE_PATCH = "";
    private static final String GET_PATCH = "/%d";
    private static final String GET_ALL_PATCH = "/all?from=%d&size=%d";
    private static final String GET_ALL_BY_REQUESTER_ID_PATCH = "";

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

        return post(CREATE_PATCH, requesterId, inputDTO);
    }

    public ResponseEntity<Object> getByRequestId(long userId, long requestId) {

        String url = String.format(GET_PATCH, requestId);

        return get(url, userId);
    }

    public ResponseEntity<Object> getAll(long userId, Integer from, Integer size) {

        String url = String.format(GET_ALL_PATCH, from, size);

        return get(url, userId);
    }

    public ResponseEntity<Object> getAllByRequesterId(long requesterId) {

        return get(GET_ALL_BY_REQUESTER_ID_PATCH, requesterId);
    }
}
