package com.zholtikov.filminator.filmservice.service;

import com.zholtikov.filminator.filmservice.dao.DirectorDao;
import com.zholtikov.filminator.filmservice.dao.FilmDao;
import com.zholtikov.filminator.filmservice.dao.UserDao;
import com.zholtikov.filminator.filmservice.kafka.producer.KafkaProducer;
import com.zholtikov.filminator.filmservice.model.EventEntityType;
import com.zholtikov.filminator.filmservice.model.EventMessage;
import com.zholtikov.filminator.filmservice.model.EventOperation;
import com.zholtikov.filminator.filmservice.model.Film;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmService {

    @Autowired
    private KafkaProducer kafkaProducer;


    FilmDao filmDao;
    UserDao userDao;
    DirectorDao directorDao;



/*    public List<Film> findPopularFilms(Integer count) {
        List<Film> films = filmDao.findAll();
        if (films.isEmpty()) {
            String message = "Film collection is empty.";
            log.debug(message);
            throw new FilmNotFoundException(message);
        }
        return films.stream()
                .sorted((film1, film2) -> film2.getRate() - film1.getRate())
                .limit(count)
                .collect(Collectors.toList());
    }*/

    public Film create(Film film) {

  /*      String message = "Create film " + film.getName();
        kafkaProducer.sendMessage("string-topic", message);


        EventMessage msgProd = EventMessage.builder().message("Name of DTO").version("1.0").build();
        kafkaProducer.sendMessage("dto-topic", msgProd);*/


        log.info("Film " + film.getName() + " was successfully saved!");
        return filmDao.create(film);
    }

    public Film update(Film film) {

        filmDao.checkFilmExistence(film.getId());
        log.info("Film " + film.getName() + " was successfully updated!");
        return filmDao.update(film);
    }

    public List<Film> findAll() {
        return filmDao.findAll();
    }

    public Film findFilmById(Long id) {
        filmDao.checkFilmExistence(id);
        return filmDao.findFilmById(id);
    }

    public List<Film> getDirectorsFilms(Long directorId, String sortBy) {
        directorDao.checkDirectorExistence(directorId);
        if (sortBy.equals("likes")) {
            sortBy = "rate";
        } else sortBy = "f.release_date";
        return filmDao.getDirectorsFilms(directorId, sortBy);
    }

    public void deleteFilm(Long id) {
        filmDao.deleteFilm(id);
    }

    public void deleteAllFilms() {
        filmDao.deleteAllFilms();
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        userDao.checkUserExistence(userId);
        userDao.checkUserExistence(friendId);
        return filmDao.getCommonFilms(userId, friendId);
    }

    public List<Film> getPopularByGenreAndYear(int year, Long genreId, int count) {
        if (year == 0 && genreId == 0) {
            return filmDao.getPopularFilms(count);
        } else if (genreId == 0) {
            return filmDao.getPopularByYear(year, count);
        } else if (year == 0) {
            return filmDao.getPopularByGenre(genreId, count);
        } else {
            return filmDao.getPopularByGenreAndYear(year, genreId, count);
        }
    }

    public List<Film> getRecommendFilms(Long userId) {
        userDao.checkUserExistence(userId);
        return filmDao.getRecommendFilms(userId);
    }

    public Film addLike(Long filmId, Long userId) {
        userDao.checkUserExistence(userId);
        filmDao.checkFilmExistence(filmId);
        filmDao.addLike(filmId, userId);


        EventMessage eventMessage = EventMessage.builder().userId(userId).targetId(filmId)
                .operation(EventOperation.ADD).type(EventEntityType.LIKE).build();
        kafkaProducer.sendEventMessage("event-topic", eventMessage);
        // eventDao.addLike(userId, filmId);

        log.info("Like was added to film");
        return filmDao.findFilmById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        userDao.checkUserExistence(userId);
        filmDao.checkFilmExistence(filmId);
        filmDao.removeLike(filmId, userId);

        EventMessage eventMessage = EventMessage.builder().userId(userId).targetId(filmId)
                .operation(EventOperation.REMOVE).type(EventEntityType.LIKE).build();
        kafkaProducer.sendEventMessage("event-topic", eventMessage);
        //eventDao.removeLike(userId, filmId);

        log.info("Like was removed from film");
        return filmDao.findFilmById(filmId);
    }


/*    public void scoreFilm(Long filmId, int userId, int score) {
        userDao.checkUserExistence(userId);
        filmDao.checkFilmExistence(filmId);
        filmDao.removeLike(filmId, userId);
     //   eventStorage.addScore(userId, id);
    }*/


}