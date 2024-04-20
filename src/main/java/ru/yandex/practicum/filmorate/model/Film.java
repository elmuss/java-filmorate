package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NonNull
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @NonNull
    private int duration;
}