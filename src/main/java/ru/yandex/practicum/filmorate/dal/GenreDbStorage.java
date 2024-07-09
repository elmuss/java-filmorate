package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class GenreDbStorage {
    protected final JdbcTemplate jdbc;
    protected final GenreRowMapper genreMapper;

    private static final String FIND_ALL_GENRES_QUERY = "SELECT * FROM GENRES";
    private static final String FIND_GENRE_BY_ID_QUERY = "SELECT * FROM GENRES WHERE GENRE_ID = ?";

    public Genre getGenre(long id) {
        try {
            return jdbc.queryForObject(FIND_GENRE_BY_ID_QUERY, genreMapper, id);
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Такого жанра нет.");
        }
    }

    public Collection<Genre> getAllGenres() {
        return jdbc.query(FIND_ALL_GENRES_QUERY, genreMapper);
    }
}
