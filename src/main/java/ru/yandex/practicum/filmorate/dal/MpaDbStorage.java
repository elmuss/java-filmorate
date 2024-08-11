package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class MpaDbStorage {
    protected final JdbcTemplate jdbc;
    protected final MpaRowMapper mpaMapper;

    public Mpa get(long id) {
        try {
            String query = "SELECT * FROM MPA WHERE MPA_ID = ?";
            return jdbc.queryForObject(query, mpaMapper, id);
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Такого рейтинга нет.");
        }
    }

    public Collection<Mpa> getAllMpa() {
        try {
            return jdbc.query("SELECT * FROM MPA", mpaMapper);
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Такого рейтинга нет.");
        }
    }


}
