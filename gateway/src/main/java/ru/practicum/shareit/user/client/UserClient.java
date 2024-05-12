package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserInputDTO;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(UserInputDTO inputDTO) {

        String url = "";

        return post(url, inputDTO);
    }

    public ResponseEntity<Object> updateUser(long userId, UserInputDTO inputDTO) {

        String url = String.format("/%d", userId);

        return patch(url, inputDTO);
    }

    public ResponseEntity<Object> getUserById(long userId) {

        String url = String.format("/%d", userId);

        return get(url);
    }

    public ResponseEntity<Object> getAllUsers() {

        String url = "";

        return get(url);
    }

    public void deleteUserById(long userId) {

        String url = String.format("/%d", userId);

        delete(url);
    }
}
