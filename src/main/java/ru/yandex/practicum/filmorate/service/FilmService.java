package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.MpaDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    FilmDbStorage filmDbStorage;
    UserDbStorage userDbStorage;
    MpaDbStorage mpaDbStorage;
    GenreDbStorage genreDbStorage;

    public Collection<Film> getAll() {
        return filmDbStorage.getAll();
    }

    public Film create(Film film) {
        return filmDbStorage.create(film);
    }

    public Film update(Film newFilm) {
        return filmDbStorage.update(newFilm);
    }

    public Film get(long id) {
        return filmDbStorage.get(id);
    }

    public void delete(long id) {
        filmDbStorage.delete(id);
    }

    public void addLike(Long id, Long userId) {
        if (userDbStorage.get(userId) != null) {
            filmDbStorage.addLike(id, userId);
        } else {
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    public void deleteLike(Long id, Long userId) {
        if (userDbStorage.get(userId) != null) {
            filmDbStorage.deleteLike(id, userId);
        } else {
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    public List<Film> getPopular(Long count) {
        return filmDbStorage.getPopular(count);
    }

    public Collection<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public Genre getGenre(long id) {
        return genreDbStorage.getGenre(id);
    }

    public Collection<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }

    public Mpa getMpaById(long id) {
        if (mpaDbStorage.get(id) != null) {
            return mpaDbStorage.get(id);
        } else {
            throw new NotFoundException("Такого пользователя нет.");
        }
    }
}
