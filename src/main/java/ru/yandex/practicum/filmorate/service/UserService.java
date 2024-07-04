package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userDbStorage;

    public User create(User user) {
        if (user.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        userDbStorage.create(user);
        log.info("Добавлен новый пользователь, id={}", user.getId());

        return user;
    }

    public Collection<User> getAll() {
        return userDbStorage.getAll();
    }

    public User update(User newUser) {
        userDbStorage.update(newUser);
        return newUser;
    }

    public User get(long id) {
        if (userDbStorage.get(id) != null) {
            return userDbStorage.get(id);
        } else {
            throw new NotFoundException("Пользователь с ID: " + id + " не найден");
        }
    }

    public void delete(long id) {
        userDbStorage.delete(id);
    }


    public void addFriend(Long id, Long friendId) {
        userDbStorage.addFriend(id, friendId);
        log.info("Добавлен новый друг с id={}", friendId);
    }

    public void deleteFromFriends(Long id, Long friendId) {
        userDbStorage.deleteFromFriends(id, friendId);
        log.info("Удален друг с id={}", friendId);
    }

    public Collection<User> getAllFriends(Long id) {
        return userDbStorage.getAllFriends(id);
    }

    public Collection<User> getCommonFriends(Long id, Long friendId) {
        return userDbStorage.getCommonFriends(id, friendId);
    }
}
