package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void create(Film film) {
        filmStorage.create(film);
    }

    public void update(Film newFilm) {
        filmStorage.update(newFilm);
    }

    public Film get(long id) {
        return filmStorage.get(id);
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public void addLike(Long id, Long userId) {
        filmStorage.get(id);
        userStorage.get(userId);
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        filmStorage.deleteLike(id, userId);
    }

    public Collection<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }

}
