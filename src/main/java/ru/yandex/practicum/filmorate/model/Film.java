package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Film {
    @NonNull
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private Date releaseDate;
    @Positive
    private Integer duration;
    @EqualsAndHashCode.Exclude
    private Set<Long> likes;
    private List<Genre> genres;
    @NotNull
    private Mpa mpa;

    public Film(@NonNull String name, String description, Date releaseDate, Integer duration, @NotNull Mpa mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

}