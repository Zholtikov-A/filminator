package com.zholtikov.filminator.filmservice.dao.impl;

import com.zholtikov.filminator.filmservice.dao.MpaDao;
import com.zholtikov.filminator.filmservice.exceptions.MpaRatingNotFoundException;
import com.zholtikov.filminator.filmservice.model.Mpa;
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
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        String sql = "select * from filminator.mpa_rating";
        List<Optional<Mpa>> optionalList = jdbcTemplate.query(sql, this::mapRowToMpa);
        List<Mpa> mpaList = new ArrayList<>();
        for (Optional<Mpa> optionalMpa : optionalList) {
            optionalMpa.ifPresent(mpaList::add);
        }
        return mpaList;
    }

    @Override
    public Mpa findMpaById(Long id) {
        final String sql = "select * from filminator.mpa_rating where mpa_rating_id = ?";
        Optional<Mpa> mpa = jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
        if (mpa.isEmpty()) {
            throw new MpaRatingNotFoundException("Mpa rating with id \"" + id + "\" not found.");
        } else return mpa.get();
    }

    private Optional<Mpa> mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(resultSet.getLong("mpa_rating_id"))
                .name(resultSet.getString("name"))
                .build();
        return Optional.of(mpa);
    }

    @Override
    public void checkMpaExistence(Long id) {
        final String sql = "select COUNT(mpa.mpa_rating_id) " +
                "from filminator.mpa_rating as mpa " +
                "where mpa.mpa_rating_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new MpaRatingNotFoundException("MPA rating with id \"" + id + "\" not found.");
        }
    }

}
