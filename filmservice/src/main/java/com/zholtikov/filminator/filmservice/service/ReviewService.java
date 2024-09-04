package com.zholtikov.filminator.filmservice.service;

import com.zholtikov.filminator.filmservice.dao.EventDao;
import com.zholtikov.filminator.filmservice.dao.FilmDao;
import com.zholtikov.filminator.filmservice.dao.ReviewDao;
import com.zholtikov.filminator.filmservice.dao.UserDao;
import com.zholtikov.filminator.filmservice.kafka.producer.KafkaProducer;
import com.zholtikov.filminator.filmservice.model.EventMessage;
import com.zholtikov.filminator.filmservice.model.EventOperation;
import com.zholtikov.filminator.filmservice.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    ReviewDao reviewDao;
    UserDao userDao;
    FilmDao filmDao;
    KafkaProducer kafkaProducer;

    @Autowired
    public ReviewService(ReviewDao reviewDao, UserDao userDao, FilmDao filmDao, KafkaProducer kafkaProducer) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.filmDao = filmDao;
        this.kafkaProducer = kafkaProducer;
    }



/*    public ReviewService(ReviewDao reviewDao, @Qualifier("userDao") UserDao userDao,
                         @Qualifier("filmDao") FilmDao filmDao, EventDao eventDao) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.filmDao = filmDao;
        this.eventDao = eventDao;
    }*/


    public Review addReview(Review review) {
        userDao.checkUserExistence(review.getUserId());
        filmDao.checkFilmExistence(review.getFilmId());
        Review addedReview = reviewDao.addReview(review);
        EventMessage eventMessage = EventMessage.builder().userId(review.getUserId()).targetId(review.getReviewId())
                .operation(EventOperation.ADD_REVIEW).build();
        kafkaProducer.sendEventMessage("event-topic", eventMessage);
        return addedReview;
    }

    public Review updateReview(Review review) {
        reviewDao.checkReviewExistence(review.getReviewId());
        userDao.checkUserExistence(review.getUserId());
        filmDao.checkFilmExistence(review.getFilmId());
        Review updatedReview = reviewDao.updateReview(review);

        EventMessage eventMessage = EventMessage.builder().userId(review.getUserId()).targetId(review.getReviewId())
                .operation(EventOperation.UPDATE_REVIEW).build();
        kafkaProducer.sendEventMessage("event-topic", eventMessage);
        return updatedReview;
    }

    public void deleteReview(Long reviewId) {
        reviewDao.checkReviewExistence(reviewId);
        Review deletedReview = reviewDao.getReview(reviewId);
        reviewDao.deleteReview(reviewId);
        EventMessage eventMessage = EventMessage.builder().userId(deletedReview.getUserId()).targetId(deletedReview.getReviewId())
                .operation(EventOperation.REMOVE_REVIEW).build();
        kafkaProducer.sendEventMessage("event-topic", eventMessage);
    }

    public Review getReview(Long reviewId) {
        reviewDao.checkReviewExistence(reviewId);
        return reviewDao.getReview(reviewId);
    }

    public Review likeReview(Long reviewId, Long userId) {
        reviewDao.checkReviewExistence(reviewId);
        userDao.checkUserExistence(userId);
        return reviewDao.likeReview(reviewId, userId);
    }

    public Review removeLike(Long reviewId, Long userId) {
        reviewDao.checkReviewExistence(reviewId);
        userDao.checkUserExistence(userId);
        return reviewDao.removeLike(reviewId, userId);
    }

    public Review dislikeReview(Long reviewId, Long userId) {
        reviewDao.checkReviewExistence(reviewId);
        userDao.checkUserExistence(userId);
        return reviewDao.dislikeReview(reviewId, userId);
    }

    public Review removeDislike(Long reviewId, Long userId) {
        reviewDao.checkReviewExistence(reviewId);
        userDao.checkUserExistence(userId);
        return reviewDao.removeDislike(reviewId, userId);
    }

    public List<Review> getReviews(Long filmId, int count) {
        if (filmId == 0 || filmDao.findFilmById(filmId) == null) {
            return reviewDao.getAllReviews(count);
        } else {
            return reviewDao.getFilmReviews(filmId, count);
        }
    }

    private void validateLike(Long reviewId, Long userId) {
        reviewDao.getReview(reviewId);
        userDao.findUserById(userId);
    }

    private void validateReview(Review review) {
        userDao.findUserById(review.getUserId());
        filmDao.findFilmById(review.getFilmId());
    }
}

