package com.zholtikov.filminator.filmservice.controller;

import com.zholtikov.filminator.filmservice.model.Event;
import com.zholtikov.filminator.filmservice.model.Film;
import com.zholtikov.filminator.filmservice.model.User;
import com.zholtikov.filminator.filmservice.service.FilmService;
import com.zholtikov.filminator.filmservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
//@RequestMapping("/users")
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Users", description = "Requests for users")
public class UserController {
    UserService userService;
    FilmService filmService;

    @PostMapping
    @Operation(summary = "Add new user to database")
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    @Operation(summary = "Update user in database")
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping
    @Operation(summary = "Get list of all users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}") // GET /users/{id}
    @Operation(summary = "Get user by id")
    public User findUserById(@PathVariable("id") @Positive Long id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @Operation(summary = "Friendship between 2 users")
    public User addFriend(@PathVariable("id") @Positive Long userId,
                          @PathVariable("friendId") Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @Operation(summary = "Delete from friends' list")
    public User removeFriend(@PathVariable("id") @Positive Long userId,
                             @PathVariable("friendId") Long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("{id}/friends")
    @Operation(summary = "Get list of user friends by id")
    public List<User> findFriends(@PathVariable("id") @Positive Long userId) {
        return userService.findFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    @Operation(summary = "Get common friends between 2 users")
    public List<User> findCommonFriends(@PathVariable("id") @Positive Long userId,
                                        @PathVariable("otherId") @Positive Long otherUserId) {
        return userService.findCommonFriends(userId, otherUserId);
    }


    @GetMapping("/{id}/feed")
    @Operation(summary = "Get list of user actions")
    public List<Event> getEvents(@PathVariable("id") @Positive Long userId) {
        return userService.getEvents(userId);
    }


    @GetMapping("/{id}/recommendations")
    @Operation(summary = "Recommend films to user by id")
    public List<Film> recommendFilms(@PathVariable(value = "id") Long userId) {
        return filmService.getRecommendFilms(userId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public User deleteUser(@PathVariable("id") @Positive Long userId) {
        return userService.deleteUser(userId);
    }

}
