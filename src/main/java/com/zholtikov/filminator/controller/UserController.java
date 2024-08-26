package com.zholtikov.filminator.controller;

import com.zholtikov.filminator.model.Event;
import com.zholtikov.filminator.model.Film;
import com.zholtikov.filminator.model.User;
import com.zholtikov.filminator.service.FilmService;
import com.zholtikov.filminator.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    FilmService filmService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}") // GET /users/{id}
    public User findUserById(@PathVariable("id") @Positive Long id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}") // PUT /users/{id}/friends/{friendId} — добавление в друзья.
    public User addFriend(@PathVariable("id") @Positive Long userId,
                          @PathVariable("friendId") Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") // DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public User removeFriend(@PathVariable("id") @Positive Long userId,
                             @PathVariable("friendId") Long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("{id}/friends") // GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> findFriends(@PathVariable("id") @Positive Long userId) {
        return userService.findFriends(userId);
    }

    // GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") @Positive Long userId,
                                        @PathVariable("otherId") @Positive Long otherUserId) {
        return userService.findCommonFriends(userId, otherUserId);
    }


    @GetMapping("/{id}/feed")
    //   @Operation(summary = "Get list of user actions")
    public List<Event> getEvents(@PathVariable("id") @Positive Long userId) {
        return userService.getEvents(userId);
    }


    @GetMapping("/{id}/recommendations")
    public List<Film> recommendFilms(@PathVariable(value = "id") Long userId) {
        return filmService.getRecommendFilms(userId);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable("id") @Positive Long userId) {
        return userService.deleteUser(userId);
    }

}
