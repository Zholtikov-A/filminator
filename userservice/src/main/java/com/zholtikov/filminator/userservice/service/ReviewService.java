package com.zholtikov.filminator.userservice.service;

import com.zholtikov.filminator.userservice.dao.EventDao;
import com.zholtikov.filminator.userservice.dao.FilmDao;
import com.zholtikov.filminator.userservice.dao.ReviewDao;
import com.zholtikov.filminator.userservice.dao.UserDao;
import com.zholtikov.filminator.userservice.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    ReviewDao reviewDao;
    UserDao userDao;
    FilmDao filmDao;
    private final EventDao eventDao;

    @Autowired
    public ReviewService(ReviewDao reviewDao, UserDao userDao,
                         FilmDao filmDao, EventDao eventDao) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.filmDao = filmDao;
        this.eventDao = eventDao;
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
               eventDao.addReview(addedReview.getUserId(), addedReview.getReviewId());
        return addedReview;
    }

    public Review updateReview(Review review) {
        reviewDao.checkReviewExistence(review.getReviewId());
        userDao.checkUserExistence(review.getUserId());
        filmDao.checkFilmExistence(review.getFilmId());
        Review updatedReview = reviewDao.updateReview(review);

            eventDao.updateReview(updatedReview.getUserId(), updatedReview.getReviewId());
        return updatedReview;
    }

    public void deleteReview(Long reviewId) {
        reviewDao.checkReviewExistence(reviewId);
        Review deletedReview = reviewDao.getReview(reviewId);
        reviewDao.deleteReview(reviewId);
        eventDao.removeReview(deletedReview.getUserId(), reviewId);
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

