package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController fc;
    FilmService fs;
    InMemoryFilmStorage filmStorage;

    InMemoryUserStorage userStorage;

    @BeforeEach
    void setFc() {
        fc = new FilmController(fs = new FilmService(filmStorage = new InMemoryFilmStorage(),
                userStorage = new InMemoryUserStorage()));
    }

    @Test
    void validateFilmOk() {
        final Film validFilm = new Film(
                1L,
                "DUNE",
                "fantasy",
                LocalDate.of(2024, 3, 1),
                1800, null);

        filmStorage.validate(validFilm);
    }

    @Test
    void validateFilmReleaseDateFail() {
        final Film validFilm = new Film(
                1L,
                "DUNE",
                "fantasy",
                LocalDate.of(1795, 3, 1),
                1800, null);

        Exception exception = assertThrows(
                ValidationException.class, () -> filmStorage.validate(validFilm)
        );

        assertEquals("Дата релиза фильма не должна быть раньше 28 декабря 1895 года", exception.getMessage());

    }
}
