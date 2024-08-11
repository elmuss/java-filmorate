package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class MpaService {
    MpaDbStorage mpaDbStorage;

    public Mpa getMpaById(long id) {
        return mpaDbStorage.get(id);
    }

    public Collection<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa();
    }
}
