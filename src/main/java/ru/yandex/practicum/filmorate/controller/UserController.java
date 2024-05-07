package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable long id) {
        return userService.get(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userService.create(user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        userService.update(newUser);
        log.info("Данные пользователя обновлены");
        return newUser;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User create(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователь добавлен в список друзей");
        return userService.get(friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}