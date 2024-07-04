/*package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    InMemoryUserStorage userStorage;

    @BeforeEach
    void setUc() {
        userStorage = new InMemoryUserStorage();
    }

    @Test
    void validateUserOk() {
        final User validUser = new User("999@999", "999");
        validUser.setId(1L);
        validUser.setName("Q");
        validUser.setBirthday(LocalDate.of(2000, 3, 1));
        validUser.setFriends(new HashSet<>());

        userStorage.validate(validUser);
    }

    @Test
    void validateUserEmailFail() {
        final User validUser = new User("999999", "999");
        validUser.setId(1L);
        validUser.setName("Q");
        validUser.setBirthday(LocalDate.of(2000, 3, 1));
        validUser.setFriends(new HashSet<>());

        Exception exception = assertThrows(
                ValidationException.class, () -> userStorage.validate(validUser)
        );

        assertEquals("Электронная почта должна содержать символ @", exception.getMessage());

    }

    @Test
    void validateUserLoginFail() {
        final User validUser = new User("999@999", "9 9 9");
        validUser.setId(1L);
        validUser.setName("Q");
        validUser.setBirthday(LocalDate.of(2000, 3, 1));
        validUser.setFriends(new HashSet<>());

        Exception exception = assertThrows(
                ValidationException.class, () -> userStorage.validate(validUser)
        );

        assertEquals("Логин не должен содержать пробелы", exception.getMessage());

    }

    @Test
    void validateUserBirthDateFail() {
        final User validUser = new User("999@999", "999");
        validUser.setId(1L);
        validUser.setName("Q");
        validUser.setBirthday(LocalDate.of(2100, 3, 1));
        validUser.setFriends(new HashSet<>());

        Exception exception = assertThrows(
                ValidationException.class, () -> userStorage.validate(validUser)
        );

        assertEquals("Введенная дата рождения не может быть позже сегодняшней даты", exception.getMessage());

    }

    @Test
    void validateUserNameIsNull() {
        final User validUser = new User("999@999", "999");
        validUser.setId(1L);
        validUser.setName(null);
        validUser.setBirthday(LocalDate.of(2000, 3, 1));
        validUser.setFriends(new HashSet<>());

        userStorage.validate(validUser);

        assertEquals("999", validUser.getName());

    }

    @Test
    void addFriendAndGetCommonFriends() {
        User user1 = new User("999@999", "999");
        user1.setId(1L);
        user1.setName("A");
        user1.setBirthday(LocalDate.of(2000, 3, 1));
        user1.setFriends(new HashSet<>());

        User user2 = new User("999@000", "000");
        user2.setId(2L);
        user2.setName("B");
        user2.setBirthday(LocalDate.of(2000, 3, 2));
        user2.setFriends(new HashSet<>());

        User user3 = new User("000@000", "111");
        user3.setId(3L);
        user3.setName("C");
        user3.setBirthday(LocalDate.of(2000, 3, 3));
        user3.setFriends(new HashSet<>());

        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);

        userStorage.addFriend(1L, 2L);
        userStorage.addFriend(1L, 3L);
        userStorage.addFriend(2L, 3L);

        assertTrue(userStorage.getAll().contains(user1));
        assertTrue(userStorage.getAllFriends(1L).contains(user3));
        assertTrue(userStorage.getCommonFriends(1L, 2L).contains(user3));

    }
}*/
