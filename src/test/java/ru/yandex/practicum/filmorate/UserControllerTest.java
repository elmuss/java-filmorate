package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    static UserController uc = new UserController();

    @Test
    void validateUserOk() {
        final User validUser = new User(
                1L,
                "999@999",
                "999",
                "Q",
                LocalDate.of(2000, 3, 1));

        uc.validate(validUser);
    }

    @Test
    void validateUserEmailFail() {
        final User validUser = new User(
                1L,
                "999999",
                "999",
                "Q",
                LocalDate.of(2000, 3, 1));

        Exception exception = assertThrows(
                ValidationException.class, () -> uc.validate(validUser)
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
                LocalDate.of(2000, 3, 1));

        Exception exception = assertThrows(
                ValidationException.class, () -> uc.validate(validUser)
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
                LocalDate.of(2100, 3, 1));

        Exception exception = assertThrows(
                ValidationException.class, () -> uc.validate(validUser)
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
                LocalDate.of(2000, 3, 1));

        uc.validate(validUser);

        assertEquals("999", validUser.getName());

    }
}
