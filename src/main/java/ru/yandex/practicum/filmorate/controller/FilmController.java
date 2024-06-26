package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectArgumentException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        return filmService.get(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmService.create(film);
        log.info("Добавлен новый фильм, id={}", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        filmService.update(newFilm);
        log.info("Данные фильма обновлены");
        return newFilm;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        filmService.delete(id);
        log.info("Удален фильм с id {}", id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
        return filmService.get(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
        return filmService.get(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Long count) {
        if (count < 0) {
            throw new IncorrectArgumentException("Передано отрицательное значение count");
        } else if (count == 0) {
            throw new IncorrectArgumentException("Передано значение count, равное 0");
        }
        return filmService.getPopular(count);
    }
}