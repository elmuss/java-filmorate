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
            films.put(id, film);
        }

        /*if (resultSet.getLong("genre_id") != 0) {
            List<Genre> genres = film.getGenres();
            Genre genre = Genre.builder()
                    .id(resultSet.getLong("genre_id"))
                    .name(resultSet.getString("genre_name"))
                    .build();
            if (!genres.contains(genre)) {
                genres.add(genre);
            }
            film.setGenres(genres);
        }*/
        return film;

    }

    /*@Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("film_name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date"));
        film.setDuration(resultSet.getInt("duration"));
        //film.setMpa(new Mpa(resultSet.getLong("mpa_id"), resultSet.getString("mpa_name")));


        //film.setGenres(new ArrayList<>(resultSet.getInt("genre_id")));
        film.setLikes(new HashSet<>());


        //Mpa mpa = resultSet.getObject("mpa", Mpa.class);
        //film.setMpa(mpa);

        return film;
    }*/
}
