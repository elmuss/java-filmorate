package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    static FilmController fc = new FilmController();

    @Test
    void validateFilmOk() {
        final Film validFilm = new Film(
                1L,
                "DUNE",
                "fantasy",
                LocalDate.of(2024, 3, 1),
                1800);

        fc.validate(validFilm);
    }

    @Test
    void validateFilmDurationFail() {
        final Film validFilm = new Film(
                1L,
                "DUNE",
                "fantasy",
                LocalDate.of(2024, 3, 1),
                0);

        Exception exception = assertThrows(
                ValidationException.class, () -> fc.validate(validFilm)
        );

        assertEquals("Продолжительность фильма должна быть больше нуля", exception.getMessage());

    }

    @Test
    void validateFilmDescriptionLengthFail() {
        final Film validFilm = new Film(
                1L,
                "DUNE",
                "*********************************************************************************************************************************************************************************************************",
                LocalDate.of(2024, 3, 1),
                1800);

        Exception exception = assertThrows(
                ValidationException.class, () -> fc.validate(validFilm)
        );

        assertEquals("Длина описания фильма не может превышать 200 символов", exception.getMessage());

    }

    @Test
    void validateFilmReleaseDateFail() {
        final Film validFilm = new Film(
                1L,
                "DUNE",
                "fantasy",
                LocalDate.of(1795, 3, 1),
                1800);

        Exception exception = assertThrows(
                ValidationException.class, () -> fc.validate(validFilm)
        );

        assertEquals("Дата релиза фильма не должна быть раньше 28 декабря 1895 года", exception.getMessage());

    }
}
