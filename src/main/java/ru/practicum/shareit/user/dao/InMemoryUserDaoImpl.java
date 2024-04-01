package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserDaoImpl implements UserDao {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> userIdsByEmails = new HashMap<>();
    private Long id = 0L;

    @Override
    public User save(User user) {

        Long userId = generateId();
        user.setId(userId);
        users.put(userId, user);
        userIdsByEmails.put(user.getEmail(), userId);

        return user;
    }

    @Override
    public User update(User user) {

        Long userId = user.getId();
        User userFromDB = users.put(userId, user);

        userIdsByEmails.remove(userFromDB.getEmail());
        userIdsByEmails.put(user.getEmail(), userId);

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {

        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {

        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long id) {

        User removeUser = users.remove(id);
        userIdsByEmails.remove(removeUser.getEmail());
    }

    @Override
    public boolean existsById(Long id) {

        return users.containsKey(id);
    }

    @Override
    public Long findUserIdByEmail(String email) {

        return userIdsByEmails.get(email);
    }

    private Long generateId() {

        return ++id;
    }
}
