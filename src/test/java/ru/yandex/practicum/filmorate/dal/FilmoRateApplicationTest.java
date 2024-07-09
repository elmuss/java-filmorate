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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Film filmToUpdate = new Film("name", "description", new Date(1999 - 10 - 11), 100,
                new Mpa(1L, "Комедия"));

        filmDbStorage.create(filmToUpdate);

        Film updatedFilm = new Film(1L, "name", "description", new Date(1999 - 10 - 11), 300, new HashSet<>(), null,
                new Mpa(1L, "Комедия"));

        filmDbStorage.update(updatedFilm);
        Film getUpdatedFilm = filmDbStorage.get(updatedFilm.getId());

        assertEquals(300, getUpdatedFilm.getDuration());
    }
}
