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
    InMemoryFilmStorage filmStorage;

    InMemoryUserStorage userStorage;

    @BeforeEach
    void setFc() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
    }

    @Test
    void validateFilmOk() {
        final Film validFilm = new Film();
        validFilm.setId(1L);
        validFilm.setName("DUNE");
        validFilm.setDescription("fantasy");
        validFilm.setReleaseDate(LocalDate.of(2024, 3, 1));
        validFilm.setDuration(1800);
        validFilm.setLikes(new HashSet<>());

        filmStorage.validate(validFilm);
    }

    @Test
    void validateFilmReleaseDateFail() {
        final Film validFilm = new Film();
        validFilm.setId(1L);
        validFilm.setName("DUNE");
        validFilm.setDescription("fantasy");
        validFilm.setReleaseDate(LocalDate.of(1795, 3, 1));
        validFilm.setDuration(1800);
        validFilm.setLikes(new HashSet<>());

        Exception exception = assertThrows(
                ValidationException.class, () -> filmStorage.validate(validFilm)
        );

        assertEquals("Дата релиза фильма не должна быть раньше 28 декабря 1895 года", exception.getMessage());

    }

    @Test
    void getPopularFilms() {
        final Film film1 = new Film();
        film1.setId(1L);
        film1.setName("DUNE");
        film1.setDescription("fantasy");
        film1.setReleaseDate(LocalDate.of(2020, 3, 1));
        film1.setDuration(1800);
        film1.setLikes(new HashSet<>());

        final Film film2 = new Film();
        film2.setId(2L);
        film2.setName("DUNE2");
        film2.setDescription("fantasy");
        film2.setReleaseDate(LocalDate.of(2021, 3, 1));
        film2.setDuration(2800);
        film2.setLikes(new HashSet<>());

        final Film film3 = new Film();
        film3.setId(3L);
        film3.setName("DUNE3");
        film3.setDescription("fantasy");
        film3.setReleaseDate(LocalDate.of(2022, 3, 1));
        film3.setDuration(3800);
        film3.setLikes(new HashSet<>());

        final Film film4 = new Film();
        film4.setId(4L);
        film4.setName("DUNE4");
        film4.setDescription("fantasy");
        film4.setReleaseDate(LocalDate.of(2023, 3, 1));
        film4.setDuration(4800);
        film4.setLikes(new HashSet<>());

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
