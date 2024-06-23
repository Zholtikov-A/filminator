package com.zholtikov.filminator.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;

    @Email(message = "Invalid email.")
    String email;

    @Pattern(regexp = "[^ ]*", message = "Field \"login\" can't contains space symbols.")
    @NotEmpty(message = "Field \"login\" can't be empty.")
    @NotNull(message = "Field \"login\" can't be null.")
    String login;

    String name;

    @Past(message = "Field \"birthday\" can't contain future date.")
    LocalDate birthday;

    final Set<Long> friendsIds = new HashSet<>();

    public User() {
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);

        return values;
    }

}
