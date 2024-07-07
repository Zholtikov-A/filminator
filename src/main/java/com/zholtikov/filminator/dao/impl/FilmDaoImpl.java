package com.zholtikov.filminator.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zholtikov.filminator.dao.FilmDao;
import com.zholtikov.filminator.exceptions.CustomValidationException;
import com.zholtikov.filminator.exceptions.FilmNotFoundException;
import com.zholtikov.filminator.model.Director;
import com.zholtikov.filminator.model.Film;
import com.zholtikov.filminator.model.Genre;
import com.zholtikov.filminator.model.Mpa;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDaoImpl userDao;

    @Override
    public Film create(Film film) throws CustomValidationException {
        final String sqlFilm = "insert into filminator.films(name, description, release_date, duration, mpa_rating_id, rate) " +
                "values(?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilm, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            stmt.setInt(6, film.getRate());
            return stmt;
        }, keyHolder);

        Long key = Objects.requireNonNull(keyHolder.getKey()).longValue();

        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                final String sqlGenres = "insert into filminator.films_genre_link(film_id, genre_id) " +
                        "values(?,?);";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement((sqlGenres));
                    stmt.setInt(1, key.intValue());
                    stmt.setInt(2, genre.getId().intValue());
                    return stmt;
                });
            }
        }

        if (!film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                final String sqlDirectors = "insert into filminator.film_directors (film_id, director_id) values (?, ?)";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement((sqlDirectors));
                    stmt.setInt(1, key.intValue());
                    stmt.setInt(2, director.getId().intValue());
                    return stmt;
                });
            }
        }


        return findFilmById(key);
    }

    @Override
    public Film update(Film film) {
        String sqlFilmUpdate = "update filminator.films set name = ?, " +
                "description = ?, release_date = ?, duration = ?, " +
                "mpa_rating_id = ?, rate = ?" +
                " where film_id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilmUpdate);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            stmt.setInt(6, film.getRate());
            stmt.setLong(7, film.getId());
            return stmt;
        });

        // delete genres
        final String sqlGenresDelete = "delete from filminator.films_genre_link " +
                "where film_id = " + film.getId();
        jdbcTemplate.update(sqlGenresDelete);

        // record genre list
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                final String sqlGenres = "insert into filminator.films_genre_link(film_id, genre_id) " +
                        "values(?,?);";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement((sqlGenres));
                    stmt.setInt(1, film.getId().intValue());
                    stmt.setInt(2, genre.getId().intValue());
                    return stmt;
                });
            }
        }

        // delete directors
        final String sqlDirectorsDelete = "delete from filminator.film_directors " +
                "where film_id = " + film.getId();
        jdbcTemplate.update(sqlDirectorsDelete);

        if (!film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                final String sqlDirectors = "insert into filminator.film_directors (film_id, director_id) values (?, ?)";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement((sqlDirectors));
                    stmt.setInt(1, film.getId().intValue());
                    stmt.setInt(2, director.getId().intValue());
                    return stmt;
                });
            }
        }

        return findFilmById(film.getId());
    }

    @Override
    public List<Film> findAll() {
        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate, " +

                "json_arrayagg(json_object(" +
                "  KEY 'id' VALUE d.director_id," +
                "  KEY 'name' VALUE d.name" +
                ")) as directors, " +

                "from filminator.films as f " +
                "left join filminator.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filminator.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filminator.genre as g on fgl.genre_id = g.genre_id " +
                "left join filminator.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "left join filminator.film_directors as fd on f.film_id = fd.film_id " +
                "left join filminator.directors as d on fd.director_id = d.director_id " +
                "group by f.film_id ";

        List<Optional<Film>> queryResult = jdbcTemplate.query(sql, this::mapRowToFilm);
        List<Film> films = new ArrayList<>();
        for (Optional<Film> optionalFilm : queryResult) {
            optionalFilm.ifPresent(films::add);
        }
        return films;
    }

    @Override
    public Film findFilmById(Long id) {

        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate, " +

                "json_arrayagg(json_object(" +
                "  KEY 'id' VALUE d.director_id," +
                "  KEY 'name' VALUE d.name" +
                ")) as directors, " +

                "from filminator.films as f " +
                "left join filminator.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filminator.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filminator.genre as g on fgl.genre_id = g.genre_id " +
                "left join filminator.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "left join filminator.film_directors as fd on f.film_id = fd.film_id " +
                "left join filminator.directors as d on fd.director_id = d.director_id " +
                "where f.film_id = ? " +
                "group by f.film_id ";

        Optional<Film> optionalFilm = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        if (optionalFilm.isEmpty()) {
            throw new FilmNotFoundException("Film with id \"" + id + "\" not found.");
        } else {
            return optionalFilm.get();
        }
    }


    @Override
    public Film addLike(Long filmId, Long userId) {
        checkFilmExistence(filmId);
        userDao.checkUserExistence(userId);
        String sql = "insert into filminator.likes_films_users_link(film_id, user_id) " +
                "values(?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, filmId.intValue());
            stmt.setInt(2, userId.intValue());
            return stmt;
        });
        return findFilmById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        checkFilmExistence(filmId);
        userDao.checkUserExistence(userId);
        final String sql = "delete from filminator.likes_films_users_link " +
                "where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        return findFilmById(filmId);
    }

    @Override
    public List<Film> getDirectorsFilms(Long directorId, String sortBy) {
        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate, " +
                "json_arrayagg(json_object(" +
                "  KEY 'id' VALUE d.director_id," +
                "  KEY 'name' VALUE d.name" +
                ")) as directors, " +
                "from filminator.films as f " +
                "left join filminator.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filminator.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filminator.genre as g on fgl.genre_id = g.genre_id " +
                "left join filminator.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "left join filminator.film_directors as fd on f.film_id = fd.film_id " +
                "left join filminator.directors as d on fd.director_id = d.director_id " +
                "where d.director_id = " + directorId +
                " group by f.film_id " +
                "order by " + sortBy + " DESC";

        List<Optional<Film>> queryResult = jdbcTemplate.query(sql, this::mapRowToFilm);
        List<Film> films = new ArrayList<>();
        for (Optional<Film> optionalFilm : queryResult) {
            optionalFilm.ifPresent(films::add);
        }
        return films;
    }

    @Override
    public void checkFilmExistence(Long id) {
        final String sql = "select COUNT(f.film_id) " +
                "from filminator.films as f " +
                "where f.film_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new FilmNotFoundException("Film with id \"" + id + "\" not found.");
        }
    }

    @Override
    public void deleteFilm(Long id) {

        //   String sqlDeleteFilm = "delete from filminator.films where film_id = " + id + " ;" ; // это если через каскад с схеме
        String sql = "delete from filminator.films_genre_link where film_id = " + id +
                " ; delete from filminator.likes_films_users_link where film_id = " + id +
                " ; delete from filminator.film_directors where film_id = " + id +
                " ; delete from filminator.films where film_id = " + id;

        jdbcTemplate.update(sql);
    }

    @Override
    public void deleteAllFilms() {
        String sql = "set referential_integrity false; " +
                "truncate TABLE filminator.films_genre_link; " +
                "truncate TABLE filminator.likes_films_users_link; " +
                "truncate TABLE filminator.film_directors; " +
                "truncate TABLE filminator.films; " +
                "set referential_integrity true; " +
                "alter table filminator.films_genre_link alter column id restart with 1; " +
                "alter table filminator.likes_films_users_link alter column id restart with 1; " +
                "alter table filminator.film_directors alter column id restart with 1; " +
                "alter table filminator.films alter column film_id restart with 1 ";
        jdbcTemplate.update(sql);
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate, " +
                "json_arrayagg(json_object(" +
                "  KEY 'id' VALUE d.director_id," +
                "  KEY 'name' VALUE d.name" +
                ")) as directors, " +
                "from filminator.films as f " +
                "left join filminator.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filminator.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filminator.genre as g on fgl.genre_id = g.genre_id " +
                "left join filminator.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "left join filminator.film_directors as fd on f.film_id = fd.film_id " +
                "left join filminator.directors as d on fd.director_id = d.director_id " +
                "where f.film_id in (select user_likes.film_id " +
                "from filminator.likes_films_users_link as user_likes where user_likes.user_id = " +
                userId + " ) " +
                "and f.film_id in (select friend_likes.film_id " +
                "from filminator.likes_films_users_link as friend_likes where friend_likes.user_id = " +
                friendId + " ) " +
                "group by f.film_id ";
        List<Optional<Film>> queryResult = jdbcTemplate.query(sql, this::mapRowToFilm);
        List<Film> films = new ArrayList<>();
        for (Optional<Film> optionalFilm : queryResult) {
            optionalFilm.ifPresent(films::add);
        }
        return films;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate, " +
                "json_arrayagg(json_object(" +
                "  KEY 'id' VALUE d.director_id," +
                "  KEY 'name' VALUE d.name" +
                ")) as directors, " +
                "from filminator.films as f " +
                "left join filminator.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filminator.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filminator.genre as g on fgl.genre_id = g.genre_id " +
                "left join filminator.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "left join filminator.film_directors as fd on f.film_id = fd.film_id " +
                "left join filminator.directors as d on fd.director_id = d.director_id " +
                "group by f.film_id " +
                "order by count(lk.user_id) DESC " +
                "limit " + count;

        List<Optional<Film>> queryResult = jdbcTemplate.query(sql, this::mapRowToFilm);
        List<Film> films = new ArrayList<>();
        for (Optional<Film> optionalFilm : queryResult) {
            optionalFilm.ifPresent(films::add);
        }
        return films;
    }

    @Override
    public List<Film> getPopularByGenreAndYear(int year, Long genreId, int count) {

        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate, " +
                "json_arrayagg(json_object(" +
                "  KEY 'id' VALUE d.director_id," +
                "  KEY 'name' VALUE d.name" +
                ")) as directors, " +
                "from filminator.films as f " +
                "left join filminator.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filminator.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filminator.genre as g on fgl.genre_id = g.genre_id " +
                "left join filminator.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "left join filminator.film_directors as fd on f.film_id = fd.film_id " +
                "left join filminator.directors as d on fd.director_id = d.director_id " +
                "where f.film_id in (select filminator.films_genre_link.film_id " +
                "from filminator.films_genre_link where filminator.films_genre_link.genre_id = " +
                genreId + "  ) " +
                "and f.film_id in (select filminator.films.film_id " +
                "from filminator.films where year(filminator.films.release_date) = " +
                year + " ) " +
                "group by f.film_id " +
                "order by count(lk.user_id) DESC " +
                "limit " + count;

        List<Optional<Film>> queryResult = jdbcTemplate.query(sql, this::mapRowToFilm);
        List<Film> films = new ArrayList<>();
        for (Optional<Film> optionalFilm : queryResult) {
            optionalFilm.ifPresent(films::add);
        }
        return films;
    }


    @Override
    public List<Film> getPopularByYear(int year, int count) {
        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate, " +
                "json_arrayagg(json_object(" +
                "  KEY 'id' VALUE d.director_id," +
                "  KEY 'name' VALUE d.name" +
                ")) as directors, " +
                "from filminator.films as f " +
                "left join filminator.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filminator.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filminator.genre as g on fgl.genre_id = g.genre_id " +
                "left join filminator.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "left join filminator.film_directors as fd on f.film_id = fd.film_id " +
                "left join filminator.directors as d on fd.director_id = d.director_id " +
                "where year(f.release_date) = " + year + " " +
                "group by f.film_id " +
                "order by count(lk.user_id) DESC " +
                "limit " + count;

        List<Optional<Film>> queryResult = jdbcTemplate.query(sql, this::mapRowToFilm);
        List<Film> films = new ArrayList<>();
        for (Optional<Film> optionalFilm : queryResult) {
            optionalFilm.ifPresent(films::add);
        }
        return films;
    }

    @Override
    public List<Film> getPopularByGenre(Long genreId, int count) {
        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate, " +
                "json_arrayagg(json_object(" +
                "  KEY 'id' VALUE d.director_id," +
                "  KEY 'name' VALUE d.name" +
                ")) as directors, " +
                "from filminator.films as f " +
                "left join filminator.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filminator.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filminator.genre as g on fgl.genre_id = g.genre_id " +
                "left join filminator.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "left join filminator.film_directors as fd on f.film_id = fd.film_id " +
                "left join filminator.directors as d on fd.director_id = d.director_id " +
                "where f.film_id in (select filminator.films_genre_link.film_id " +
                "from filminator.films_genre_link where filminator.films_genre_link.genre_id = " +
                genreId + "  ) " +
                "group by f.film_id " +
                "order by count(lk.user_id) DESC " +
                "limit " + count;

        List<Optional<Film>> queryResult = jdbcTemplate.query(sql, this::mapRowToFilm);
        List<Film> films = new ArrayList<>();
        for (Optional<Film> optionalFilm : queryResult) {
            optionalFilm.ifPresent(films::add);
        }
        return films;
    }


    private Optional<Film> mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getLong("mpa_rating_id"),
                        resultSet.getString("mpa_name")))
                .rate(resultSet.getInt("rate"))
                .build();

        String genresString = resultSet.getString("genres");

        final ObjectMapper objectMapper = new ObjectMapper();
        Genre[] genres = new Genre[10];
        try {
            genres = objectMapper.readValue(genresString, Genre[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Genre genre : genres) {
            if (genre != null) {
                if (genre.getId() != null)
                    film.getGenres().add(genre);
            }
        }

        String directorsString = resultSet.getString("directors");

        final ObjectMapper directorMapper = new ObjectMapper();
        Director[] directors = new Director[10];
        try {
            directors = directorMapper.readValue(directorsString, Director[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Director director : directors) {
            if (director != null) {
                if (director.getId() != null)
                    film.getDirectors().add(director);
            }
        }
        return Optional.of(film);
    }


}
