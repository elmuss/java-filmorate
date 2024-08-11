package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class FilmDto {
    private Long id;
    private String name;
    private String description;
    private Date releaseDate;
    private Integer duration;
    private List<String> genres;
    private String mpa;
}
