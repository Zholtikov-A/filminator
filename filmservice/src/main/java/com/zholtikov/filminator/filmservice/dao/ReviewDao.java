package com.zholtikov.filminator.filmservice.dao;

import com.zholtikov.filminator.filmservice.model.Review;

import java.util.List;

public interface ReviewDao {

    public Review addReview(Review review);

    public Review updateReview(Review review);

    public void deleteReview(Long reviewId);

    public Review getReview(Long reviewId);

    Review likeReview(Long reviewId, Long userId);

    Review removeLike(Long reviewId, Long userId);

    Review dislikeReview(Long reviewId, Long userId);

    Review removeDislike(Long reviewId, Long userId);

    List<Review> getAllReviews(int count);

    List<Review> getFilmReviews(Long filmId, int count);

    void checkReviewExistence(Long id);
}
