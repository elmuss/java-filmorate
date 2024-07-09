package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    Map<Long, Film> films = new HashMap<>();

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("film_id");
        Film film = films.get(id);
        if (film == null) {
            film = Film.builder()
                    .id(id)
                    .name(resultSet.getString("film_name"))
                    .description(resultSet.getString("description"))
                    .releaseDate(resultSet.getDate("release_date"))
                    .duration(resultSet.getInt("duration"))
                    .likes(new HashSet<>())
                    .build();
        }
        return film;
    }
}
