package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.exceptions.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;


@Primary
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    protected final JdbcTemplate jdbc;
    protected final FilmRowMapper mapper;
    protected final GenreRowMapper genreMapper;
    protected final MpaRowMapper mpaMapper;
    protected final MpaDbStorage mpaStorage;
    protected final GenreDbStorage genreStorage;

    private static final String FIND_ALL_QUERY = "SELECT * FROM FILMS";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM FILMS WHERE FILM_ID = ?";
    private static final String FIND_MPA_QUERY = "SELECT FILM_MPA FROM FILMS WHERE FILM_ID = ?";
    private static final String FIND_LIST_OF_GENRES_QUERY = "SELECT GENRE_ID FROM FILMS_GENRES WHERE FILM_ID = ?";
    private static final String FIND_LIST_OF_EMPTY_GENRES_QUERY = "SELECT FG.GENRE_ID FROM FILMS F" +
            "JOIN FILMS_GENRES FG ON F.FILM_ID = FG.FILM_ID WHERE F.FILM_ID = ? LIMIT(1)";
    private static final String INSERT_QUERY = "INSERT INTO films (FILM_NAME, DESCRIPTION, RELEASE_DATE," +
            "DURATION, FILM_MPA) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILMS_GENRES_QUERY = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET FILM_NAME = ?, DESCRIPTION = ?," +
            "RELEASE_DATE = ?, DURATION = ?, FILM_MPA = ? WHERE FILM_ID = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_POPULAR_FILMS = "SELECT  FILM_ID  FROM LIKES GROUP BY FILM_ID ORDER BY COUNT(USER_ID) DESC";

    @Override
    public Film create(Film film) {
        validate(film);
        Long mpaId = film.getMpa().getId();
        List<Genre> genres = film.getGenres();
        try {
            mpaStorage.get(mpaId);
        } catch (NotFoundException e) {
            throw new IncorrectArgumentException("Введен некорректный запрос");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_QUERY, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, film.getReleaseDate());
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        film.setLikes(new HashSet<>());

        film.setGenres(new ArrayList<>());
        for (Genre genre : genres) {
            try {
                if (genreStorage.getGenre(genre.getId()) != null) {
                    film.getGenres().add(genreStorage.getGenre(genre.getId()));
                }
            } catch (NotFoundException e) {
                throw new IncorrectArgumentException("Введен некорректный запрос");
            }
        }

        film.setMpa(mpaStorage.get(mpaId));

        for (Genre genre : genres) {
            jdbc.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(INSERT_FILMS_GENRES_QUERY, new String[]{});
                stmt.setLong(1, film.getId());
                stmt.setLong(2, genre.getId());
                return stmt;
            });
        }
        return film;
    }

    public void validate(Film film) {
        if (film.getReleaseDate().toLocalDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не должна быть раньше 28 декабря 1895 года");
        }
    }

    @Override
    public Film update(Film film) {
        validate(film);
        updateFilm(
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    protected void updateFilm(Object... params) {
        int rowsUpdated = jdbc.update(FilmDbStorage.UPDATE_QUERY, params);
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    @Override
    public Film get(long id) {
        try {
            Film filmToReturn = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
            Long mpaId = jdbc.queryForObject(FIND_MPA_QUERY, Long.class, id);
            filmToReturn.setMpa(mpaStorage.get(mpaId));

            List<Long> listOfGenresId = jdbc.queryForList(FIND_LIST_OF_GENRES_QUERY, Long.class, id);

            if (!listOfGenresId.isEmpty()) {
                List<Genre> listOfGenres = new ArrayList<>();

                for (Long genreId : listOfGenresId) {
                    if (!listOfGenres.contains(genreStorage.getGenre(genreId))) {
                        listOfGenres.add(genreStorage.getGenre(genreId));
                    }
                }
                filmToReturn.setGenres(listOfGenres);

            }
            return filmToReturn;

        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Такого фильма нет.");
        }
    }

    @Override
    public boolean delete(long id) {
        return jdbc.update(DELETE_BY_ID_QUERY, id) > 0;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbc.query(FIND_ALL_QUERY, mapper);
    }

    @Override
    public void addLike(Long id, Long userId) {
        if (get(id) != null) {
            get(id).getLikes().add(userId);
        } else {
            throw new NotFoundException("Такого фильма нет.");
        }

        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_LIKE_QUERY, new String[]{});
            stmt.setLong(1, id);
            stmt.setLong(2, userId);
            return stmt;
        });
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        if (get(id) != null) {
            get(id).getLikes().remove(userId);
        } else {
            throw new NotFoundException("Такого фильма нет.");
        }
        return jdbc.update(DELETE_LIKE_QUERY, id, userId) > 0;
    }

    @Override
    public List<Film> getPopular(Long count) {
        List<Long> listOfFilms = jdbc.queryForList(GET_POPULAR_FILMS, Long.class);
        List<Film> popularFilms = new ArrayList<>();

        for (Long filmId : listOfFilms) {
            popularFilms.add(get(filmId));
        }
        return popularFilms.stream().limit(count).toList();
    }
}
