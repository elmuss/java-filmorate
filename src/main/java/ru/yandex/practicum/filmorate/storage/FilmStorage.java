package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Film get(long id);

    boolean delete(long id);

    Collection<Film> getAll();

    void addLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userId);

    List<Film> getPopular(Long count);

}
