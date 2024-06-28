package com.zholtikov.filminator.controller;


import com.zholtikov.filminator.model.Review;
import com.zholtikov.filminator.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        System.out.println("input");
        System.out.println(review);
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") @Positive Long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @GetMapping("/{id}")
    public Review getReview(@PathVariable("id") @Positive Long reviewId){
        return reviewService.getReview(reviewId);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review likeReview(@PathVariable("id") @Positive Long reviewId, @PathVariable("userId") Long userId) {
       return reviewService.likeReview(reviewId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Review removeLike(@PathVariable("id") @Positive Long reviewId, @PathVariable("userId") Long userId) {
       return reviewService.removeLike(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review dislikeReview(@PathVariable("id") @Positive Long reviewId, @PathVariable("userId") Long userId) {
      return   reviewService.dislikeReview(reviewId, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Review removeDislike(@PathVariable("id") @Positive Long reviewId, @PathVariable("userId") Long userId) {
       return reviewService.removeDislike(reviewId, userId);
    }

    @GetMapping
    public List<Review> getReviews(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "0") Long filmId
    ) {
        return reviewService.getReviews(filmId, count);
    }
}
