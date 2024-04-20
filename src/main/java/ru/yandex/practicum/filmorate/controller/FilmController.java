package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final static int MAX_DESCRIPTION_SIZE = 200;
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public void create(@RequestBody Film film) {
        validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public void update(@Validated(Film.class) @RequestBody Film newFilm) {
        validate(newFilm);
        films.put(newFilm.getId(), newFilm);
        log.info("Данные фильма обновлены");
    }

    public void validate(Film film) {

        if (film.getDescription().length() > MAX_DESCRIPTION_SIZE) {
            throw new ValidationException("Длина описания фильма не может превышать 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не должна быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration().getSeconds() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть больше нуля");
        }

    }
}