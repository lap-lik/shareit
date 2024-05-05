package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.validation.Marker;
import ru.practicum.shareit.user.dto.UserInputDTO;
import ru.practicum.shareit.user.dto.UserOutputDTO;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Validated(Marker.OnCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    public UserOutputDTO createUser(@Valid @RequestBody final UserInputDTO inputDTO) {

        log.info("START endpoint `method:POST /users` (create user), request: {}.", inputDTO.getName());

        return userService.create(inputDTO);
    }

    @PatchMapping("/{userId}")
    @Validated(Marker.OnUpdate.class)
    public UserOutputDTO updateUser(@PathVariable Long userId, @Valid @RequestBody final UserInputDTO inputDTO) {

        log.info("START endpoint `method:PATCH /users/:userId` (update user), user id: {}.", userId);

        return userService.update(userId, inputDTO);
    }

    @GetMapping("/{userId}")
    public UserOutputDTO getUserById(@PathVariable final Long userId) {

        log.info("START endpoint `method:GET /users/{userId}` (get user by id), user id: {}.", userId);

        return userService.getById(userId);
    }

    @GetMapping
    public List<UserOutputDTO> getAllUsers() {

        log.info("START endpoint `method:GET /users` (get all users).");

        return userService.getAll();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {

        log.info("START endpoint `method:DELETE /users/{userId}` (delete user by id), user id: {}.", userId);

        userService.deleteById(userId);
    }
}
