package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.validation.Marker;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserInputDTO;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient client;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createUser(@Valid @RequestBody final UserInputDTO inputDTO) {

        log.info("START endpoint `method:POST /users` (create user), request: {}.", inputDTO.getName());

        return client.createUser(inputDTO);
    }

    @PatchMapping("/{userId}")
    @Validated(Marker.OnUpdate.class)
    public ResponseEntity<Object> updateUser(@PathVariable long userId,
                                             @Valid @RequestBody final UserInputDTO inputDTO) {

        log.info("START endpoint `method:PATCH /users/:userId` (update user), user id: {}.", userId);

        return client.updateUser(userId, inputDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {

        log.info("START endpoint `method:GET /users/{userId}` (get user by id), user id: {}.", userId);

        return client.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {

        log.info("START endpoint `method:GET /users` (get all users).");

        return client.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable long userId) {

        log.info("START endpoint `method:DELETE /users/{userId}` (delete user by id), user id: {}.", userId);

        client.deleteUserById(userId);
    }
}
