package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void getPopularFilms() {
        final Film film1 = new Film(
                1L,
                "DUNE",
                "fantasy",
                LocalDate.of(2020, 3, 1),
                1800, new HashSet<>());

        final Film film2 = new Film(
                2L,
                "DUNE2",
                "fantasy",
                LocalDate.of(2021, 3, 1),
                2800, new HashSet<>());

        final Film film3 = new Film(
                3L,
                "DUNE3",
                "fantasy",
                LocalDate.of(2022, 3, 1),
                3800, new HashSet<>());

        final Film film4 = new Film(
                4L,
                "DUNE4",
                "fantasy",
                LocalDate.of(2023, 3, 1),
                3800, new HashSet<>());

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

        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);

        filmStorage.create(film1);
        filmStorage.create(film2);
        filmStorage.create(film3);
        filmStorage.create(film4);

        filmStorage.addLike(4L, 1L);
        filmStorage.addLike(4L, 2L);
        filmStorage.addLike(4L, 3L);
        filmStorage.addLike(3L, 1L);
        filmStorage.addLike(2L, 2L);
        filmStorage.addLike(3L, 3L);
        filmStorage.addLike(4L, 4L);

        System.out.println(filmStorage.getPopular(3L));

        assertEquals(4L, filmStorage.getPopular(3L).get(0).getId());
    }
}
