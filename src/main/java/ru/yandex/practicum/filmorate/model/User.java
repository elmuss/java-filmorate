package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;

}