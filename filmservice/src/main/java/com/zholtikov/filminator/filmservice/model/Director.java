package com.zholtikov.filminator.filmservice.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
//@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Director {
    @Positive
    Long id;
    @NotBlank(message = "Field \"name\" can't consist of space symbols.")
    @NotEmpty(message = "Field \"name\" can't be empty.")
    @NotNull(message = "Field \"name\" can't be null.")
    String name;


    //по аналогии с жанрами добавлю конструктор
    public Director() {
    }

    public Director(Long id, String name) {
        this.id = id;
        this.name = name;
    }


}
