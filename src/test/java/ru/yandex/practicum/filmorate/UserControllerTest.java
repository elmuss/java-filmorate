package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController uc;
    UserService us;
    InMemoryUserStorage userStorage;

    @BeforeEach
    void setUc() {
        uc = new UserController(us = new UserService(userStorage = new InMemoryUserStorage()));
    }

    @Test
    void validateUserOk() {
        final User validUser = new User(
                1L,
                "999@999",
                "999",
                "Q",
                LocalDate.of(2000, 3, 1), null);

        userStorage.validate(validUser);
    }

    @Test
    void validateUserEmailFail() {
        final User validUser = new User(
                1L,
                "999999",
                "999",
                "Q",
                LocalDate.of(2000, 3, 1), null);

        Exception exception = assertThrows(
                ValidationException.class, () -> userStorage.validate(validUser)
        );

        assertEquals("Электронная почта должна содержать символ @", exception.getMessage());

    }

    @Test
    void validateUserLoginFail() {
        final User validUser = new User(
                1L,
                "999@999",
                "9 9 9",
                "Q",
                LocalDate.of(2000, 3, 1), null);

        Exception exception = assertThrows(
                ValidationException.class, () -> userStorage.validate(validUser)
        );

        assertEquals("Логин не должен содержать пробелы", exception.getMessage());

    }

    @Test
    void validateUserBirthDateFail() {
        final User validUser = new User(
                1L,
                "999@999",
                "999",
                "Q",
                LocalDate.of(2100, 3, 1), null);

        Exception exception = assertThrows(
                ValidationException.class, () -> userStorage.validate(validUser)
        );

        assertEquals("Введенная дата рождения не может быть позже сегодняшней даты", exception.getMessage());

    }

    @Test
    void validateUserNameIsNull() {
        final User validUser = new User(
                1L,
                "999@999",
                "999",
                null,
                LocalDate.of(2000, 3, 1), null);

        userStorage.validate(validUser);

        assertEquals("999", validUser.getName());

    }

    @Test
    void addFriendAndGetCommonFriends() {
        User user1 = new User(
                1L,
                "999@999",
                "999",
                "A",
                LocalDate.of(2000, 3, 1), null);

        User user2 = new User(
                2L,
                "999@000",
                "000",
                "B",
                LocalDate.of(2000, 3, 2), null);

        User user3 = new User(
                3L,
                "000@000",
                "111",
                "C",
                LocalDate.of(2000, 3, 3), null);

        us.create(user1);
        us.create(user2);
        us.create(user3);

        us.addFriend(1L, 2L);
        us.addFriend(1L, 3L);
        us.addFriend(2L, 3L);

        assertTrue(us.getAll().contains(user1));
        assertTrue(us.getAllFriends(1L).contains(user3));
        assertTrue(us.getCommonFriends(1L, 2L).contains(user3));

    }
}
