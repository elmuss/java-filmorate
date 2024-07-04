package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {
    @NonNull
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    private Date birthday;
    private Set<Long> friends;

    public User(@NonNull String email, @NonNull String login, String name, Date birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}