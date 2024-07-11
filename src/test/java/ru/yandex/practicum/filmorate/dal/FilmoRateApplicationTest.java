package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class, UserRowMapper.class, FilmDbStorage.class, FilmRowMapper.class,
        GenreDbStorage.class, GenreRowMapper.class, MpaDbStorage.class, MpaRowMapper.class})
class FilmoRateApplicationTest {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    void testFindUserById() {

        userStorage.create(new User("email@email", "login", "name", new Date(1999 - 11 - 11)));
        User userToGet = userStorage.get(1);

        assertEquals(1L, userToGet.getId());
    }

    @Test
    void testFilmUpdate() {
        List<Genre> genresToUpdate = new ArrayList<>();
        genresToUpdate.add(new Genre(1L, "Комедия"));
        genresToUpdate.add(new Genre(6L, "Боевик"));


        List<Genre> updatedGenres = new ArrayList<>();
        updatedGenres.add(new Genre(1L, "Комедия"));


        Film filmToUpdate = new Film("name", "description", new Date(1999 - 10 - 11),
                100, new Mpa(1L, "Комедия"));
        filmToUpdate.setGenres(genresToUpdate);

        filmDbStorage.create(filmToUpdate);

        filmToUpdate.setGenres(updatedGenres);

        filmDbStorage.update(filmToUpdate);
        Film getUpdatedFilm = filmDbStorage.get(filmToUpdate.getId());

        assertEquals(1, getUpdatedFilm.getGenres().size());
    }

    @Test
    void testFilmUpdateUnknown() {
        List<Genre> genresToUpdate = new ArrayList<>();
        genresToUpdate.add(new Genre(1L, "Комедия"));
        genresToUpdate.add(new Genre(6L, "Боевик"));

        Film filmToUpdate = new Film("name", "description", new Date(1999 - 10 - 11),
                100, new Mpa(1L, "Комедия"));
        filmToUpdate.setGenres(genresToUpdate);

        Exception exception = assertThrows(
                NotFoundException.class, () -> filmDbStorage.update(filmToUpdate)
        );

        assertEquals("Такого фильма нет.", exception.getMessage());
    }

    @Test
    void testFilmGetUnknown() {
        Exception exception = assertThrows(
                NotFoundException.class, () -> filmDbStorage.get(1000000L)
        );

        assertEquals("Такого фильма нет.", exception.getMessage());
    }
}
