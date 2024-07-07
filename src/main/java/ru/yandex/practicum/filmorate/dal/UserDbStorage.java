package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Primary
@Component
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    protected final JdbcTemplate jdbc;
    protected final UserRowMapper mapper;
    private static final String FIND_ALL_QUERY = "SELECT * FROM USERS";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM USERS WHERE USER_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM USERS WHERE USER_ID = ?";
    private static final String DELETE_FROM_FRIENDS_QUERY = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?, ?)";


    @Override
    public User create(User user) {
        validate(user);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_QUERY, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, user.getBirthday());

            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        user.setFriends(new TreeSet<>());
        return user;
    }

    public void validate(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать символ @");
        }

        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().after(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new ValidationException("Введенная дата рождения не может быть позже сегодняшней даты");
        }
    }

    @Override
    public User update(User user) {
        validate(user);
        updateUser(
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    protected void updateUser(Object... params) {
        int rowsUpdated = jdbc.update(UserDbStorage.UPDATE_QUERY, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    @Override
    public User get(long id) {
        try {
            return jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    @Override
    public boolean delete(long id) {
        return jdbc.update(DELETE_BY_ID_QUERY, id) > 0;
    }

    @Override
    public Collection<User> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public User addFriend(Long id, Long friendId) {


        if (get(id) == null) {
            throw new NotFoundException("Пользователя с id" + id + " нет, добавление в друзья невозможно");
        }

        if (get(friendId) == null) {
            throw new NotFoundException("Пользователя с id" + friendId + " нет, добавление в друзья невозможно");
        }

        get(id).setFriends(new HashSet<>());

        jdbc.update(
                INSERT_FRIEND_QUERY,
                id, friendId);

        get(id).getFriends().add(friendId);
        return get(id);
    }

    @Override
    public boolean deleteFromFriends(Long id, Long friendId) {
        if (get(id) == null) {
            throw new NotFoundException("Пользователя с id" + id + " нет, удаление невозможно");
        } else if (get(friendId) == null) {
            throw new NotFoundException("Пользователя с id" + friendId + " нет, удаление невозможно");
        }
        get(id).getFriends().remove(friendId);

        return jdbc.update(DELETE_FROM_FRIENDS_QUERY, id, friendId) > 0;

    }

    @Override
    public Collection<User> getAllFriends(Long id) {
        if (get(id) == null) {
            throw new NotFoundException("Пользователя с id" + id + " нет, добавление в друзья невозможно");
        }

        try {
            List<Long> listOfFriendsId = jdbc.queryForList(FIND_FRIENDS_QUERY, Long.class, id);
            Collection<User> friends = new HashSet<>();
            for (Long friendId : listOfFriendsId) {
                if (!listOfFriendsId.contains(get(friendId))) {
                    friends.add(get(friendId));
                }
            }
            return friends;
        } catch (EmptyResultDataAccessException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long friendId) {
        Collection<User> commonFriendsList = new ArrayList<>(getAllFriends(id));
        commonFriendsList.retainAll(getAllFriends(friendId));
        return commonFriendsList;
    }
}
