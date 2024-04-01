package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody final UserRequestDto requestDto) {

        log.info("START endpoint `method:POST /users` (create user), request: {}.", requestDto.getName());

        return userService.create(requestDto);
    }

    @PatchMapping("/{userId}")
    public UserResponseDto update(@PathVariable Long userId, @RequestBody UserRequestDto requestDto) {

        log.info("START endpoint `method:PATCH /users/:userId` (update user), user id: {}.", userId);

        return userService.update(userId, requestDto);
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable final Long userId) {

        log.info("START endpoint `method:GET /users/{userId}` (get user by id), user id: {}.", userId);

        return userService.getById(userId);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {

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
