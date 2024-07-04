package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User get(long id);

    boolean delete(long id);

    Collection<User> getAll();

    User addFriend(Long id, Long friendId);

    boolean deleteFromFriends(Long id, Long friendId);

    Collection<User> getAllFriends(Long id);

    Collection<User> getCommonFriends(Long id, Long friendId);
}
