package ru.yandex.practicum.filmorate.dal;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;


class FilmDbStorageTest {
    JdbcTemplate jdbc = new JdbcTemplate();
    GenreRowMapper genreMapper = new GenreRowMapper();
    GenreDbStorage genreDbStorage = new GenreDbStorage(jdbc, genreMapper);

    @Test
    void getAllGenres() {

        System.out.println(genreDbStorage.getAllGenres());
    }
}