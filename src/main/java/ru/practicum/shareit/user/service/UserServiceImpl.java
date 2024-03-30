package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserRequestMapper;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserResponseMapper;
import ru.practicum.shareit.validation.Marker;
import ru.practicum.shareit.validation.ValidatorUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ItemDao itemDao;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;

    @Override
    public UserResponseDto create(UserRequestDto requestDto) {

        checkEmailAvailability(requestDto);
        ValidatorUtils.validate(requestDto, Marker.OnCreate.class);

        return userResponseMapper.toDto(userDao.save(userRequestMapper.toEntity(requestDto)));
    }

    @Override
    public UserResponseDto update(Long userId, UserRequestDto requestDto) {

        UserRequestDto userFromDB = userRequestMapper.toDto(userDao.findById(userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user with the ID - `%d` was not found.", userId))
                        .build()));

        requestDto.setId(userId);
        String userName = requestDto.getName();
        String userEmail = requestDto.getEmail();

        if (Objects.isNull(userName)) {
            requestDto.setName(userFromDB.getName());
        }
        if (Objects.isNull(userEmail)) {
            requestDto.setEmail(userFromDB.getEmail());
        }

        checkEmailAvailability(requestDto);
        ValidatorUtils.validate(requestDto, Marker.OnUpdate.class);

        return userResponseMapper.toDto(userDao.update(userRequestMapper.toEntity(requestDto)));
    }

    @Override
    public UserResponseDto getById(Long userId) {

        return userResponseMapper.toDto(userDao.findById(userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user with the ID - `%d` was not found.", userId))
                        .build()));
    }

    @Override
    public List<UserResponseDto> getAll() {

        return userResponseMapper.toDtos(userDao.findAll());
    }

    @Override
    public void deleteById(Long userId) {

        boolean isUserExists = userDao.existsById(userId);
        if (!isUserExists) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID - `%d` was not found.", userId))
                    .build();
        }

        userDao.deleteById(userId);
        itemDao.deleteByOwnerId(userId);
    }

    private void checkEmailAvailability(UserRequestDto requestDto) {

        Long userIdFromDbByEmail = userDao.findUserIdByEmail(requestDto.getEmail());

        if (Objects.nonNull(userIdFromDbByEmail)) {
            if (!Objects.equals(userIdFromDbByEmail, requestDto.getId())) {
                throw EmailConflictException.builder()
                        .message(String.format("The user with the email - `%s` already exists.", requestDto.getEmail()))
                        .build();
            }
        }
    }
}
