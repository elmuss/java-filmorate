package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.exceptions.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    FilmDbStorage filmDbStorage;
    UserDbStorage userDbStorage;

    public Collection<Film> getAll() {
        return filmDbStorage.getAll();
    }

    public Film create(Film film) {
        try {
            return filmDbStorage.create(film);
        } catch (NotFoundException e) {
            throw new IncorrectArgumentException("Введен некорректный запрос");
        }
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
        try {
            filmDbStorage.addLike(id, userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    public void deleteLike(Long id, Long userId) {
        try {
            filmDbStorage.deleteLike(id, userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    public List<Film> getPopular(Long count) {
        return filmDbStorage.getPopular(count);
    }
}
