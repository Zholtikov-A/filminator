package com.zholtikov.filminator.dao.impl;

import com.zholtikov.filminator.dao.DirectorStorage;
import com.zholtikov.filminator.exceptions.DirectorNotFoundException;
import com.zholtikov.filminator.model.Director;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DirectorDao implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> findAll() {
        String sql = "select * from filminator.directors";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs));
    }

    @Override
    public Director findById(Long id) {
        String sql = "select * from filminator.directors where director_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirector(rs), id)
                .stream().findAny().orElseThrow(() -> new DirectorNotFoundException("Director with id " + id + " not found"));
    }

    @Override
    public Director addDirector(Director director) {

        String sql = "insert into filminator.directors (name)" +
                "values (?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        int key = keyHolder.getKey().intValue();

        return jdbcTemplate.query("select * from filminator.directors where director_id = ?", (rs, rowNum) -> makeDirector(rs), key)
                .stream().findAny().orElse(null);

    }

    @Override
    public Director updateDirector(Director director) {

        String sql = "update filminator.directors set " +
                "name = ? " +
                "where director_id = ?";

        int checkNum = jdbcTemplate.update(sql,
                director.getName(),
                director.getId());

        if (checkNum == 0) {
            throw new DirectorNotFoundException("Director with id" + director.getId() + " not found");
        }

        return jdbcTemplate.query("select * from filminator.directors where director_id = ?", (rs, rowNum) -> makeDirector(rs), director.getId())
                .stream().findAny().orElse(null);
    }

    @Override
    public void deleteDirector(Long id) {
        String sqlDeleteLink = "delete from filminator.film_directors where director_id = ?";
        jdbcTemplate.update(sqlDeleteLink, id);
        String sqlDeleteDirector = "delete from filminator.directors where director_id = ? ";
        jdbcTemplate.update(sqlDeleteDirector, id);
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        return Director.builder()
                .id(rs.getLong("director_id"))
                .name(rs.getString("name"))
                .build();
    }
}
