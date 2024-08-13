package com.zholtikov.filminator.dao.impl;

import com.zholtikov.filminator.dao.GenreDao;
import com.zholtikov.filminator.exceptions.GenreNotFoundException;
import com.zholtikov.filminator.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String sql = "select * from filminator.genre";
        List<Optional<Genre>> queryResult = jdbcTemplate.query(sql, this::mapRowToGenre);
        List<Genre> genreList = new ArrayList<>();
        for (Optional<Genre> optionalGenre : queryResult) {
            optionalGenre.ifPresent(genreList::add);
        }
        return genreList;
    }

    @Override
    public Genre findGenreById(Long id) {
        final String sql = "select * from filminator.genre where genre_id = ?";
        Optional<Genre> genre = jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        if (genre.isEmpty()) {
            throw new GenreNotFoundException("Genre with id \"" + id + "\" not found.");

        } else return genre.get();
    }

    private Optional<Genre> mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name"))
                .build();
        return Optional.of(genre);
    }

    @Override
    public void checkGenreExistence(Long id) {
        final String sql = "select COUNT(g.genre_id) " +
                "from filminator.genre as g " +
                "where g.genre_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new GenreNotFoundException("Genre with id \"" + id + "\" not found.");
        }
    }

}
