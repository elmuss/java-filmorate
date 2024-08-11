package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class GenreService {
    GenreDbStorage genreDbStorage;

    public Genre getGenre(long id) {
        return genreDbStorage.getGenre(id);
    }

    public Collection<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }


}
