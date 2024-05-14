package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserInputDTO;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserOutputDTO;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final ItemDAO itemDAO;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserOutputDTO create(UserInputDTO inputDTO) {

        return userMapper.toOutputDTO(userDAO.save(userMapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    @Transactional
    public UserOutputDTO update(Long userId, UserInputDTO inputDTO) {

        UserInputDTO userFromDB = userMapper.toInputDTO(userDAO.findById(userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user with the ID - `%d` was not found.", userId))
                        .build()));

        inputDTO.setId(userId);
        String userName = inputDTO.getName();
        String userEmail = inputDTO.getEmail();

        if (Objects.isNull(userName)) {
            inputDTO.setName(userFromDB.getName());
        }
        if (Objects.isNull(userEmail)) {
            inputDTO.setEmail(userFromDB.getEmail());
        }

        return userMapper.toOutputDTO(userDAO.save(userMapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    public UserOutputDTO getById(Long userId) {

        return userMapper.toOutputDTO(userDAO.findById(userId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The user with the ID - `%d` was not found.", userId))
                        .build()));
    }

    @Override
    public List<UserOutputDTO> getAll() {

        return userMapper.toOutputDTOs(userDAO.findAll());
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {

        if (!userDAO.existsById(userId)) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID - `%d` was not found.", userId))
                    .build();
        }

        userDAO.deleteById(userId);
        itemDAO.deleteByOwnerId(userId);
    }
}