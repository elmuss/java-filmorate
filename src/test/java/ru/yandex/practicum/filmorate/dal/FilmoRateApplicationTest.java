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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class, UserRowMapper.class, FilmDbStorage.class, FilmRowMapper.class,
        GenreDbStorage.class, GenreRowMapper.class, MpaDbStorage.class, MpaRowMapper.class})
class FilmoRateApplicationTest {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testFindUserById() {

        userStorage.create(new User("email@email", "login", "name", new Date(1999 - 11 - 11)));
        User userToGet = userStorage.get(1);
        Optional<User> userOptional = Optional.of(userToGet);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindFilmByIdWithoutGenre() {

        filmDbStorage.create(new Film("name", "description", new Date(1999 - 10 - 11), 100,
                new Mpa(1L, "Комедия")));
        Film filmToGet = filmDbStorage.get(1);
        Optional<Film> filmOptional = Optional.of(filmToGet);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
}
