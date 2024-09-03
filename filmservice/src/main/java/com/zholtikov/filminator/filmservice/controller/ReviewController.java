package com.zholtikov.filminator.filmservice.controller;


import com.zholtikov.filminator.filmservice.model.Review;
import com.zholtikov.filminator.filmservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "Requests for film reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Add new review to database")
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping
    @Operation(summary = "Update review in database")
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review by id")
    public void deleteReview(@PathVariable("id") @Positive Long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review by id")
    public Review getReview(@PathVariable("id") @Positive Long reviewId){
        return reviewService.getReview(reviewId);
    }

    @PutMapping("/{id}/like/{userId}")
    @Operation(summary = "Like other user review")
    public Review likeReview(@PathVariable("id") @Positive Long reviewId, @PathVariable("userId") Long userId) {
       return reviewService.likeReview(reviewId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @Operation(summary = "Remove like from other user review")
    public Review removeLike(@PathVariable("id") @Positive Long reviewId, @PathVariable("userId") Long userId) {
       return reviewService.removeLike(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    @Operation(summary = "Dislike other user review")
    public Review dislikeReview(@PathVariable("id") @Positive Long reviewId, @PathVariable("userId") Long userId) {
      return   reviewService.dislikeReview(reviewId, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    @Operation(summary = "Remove dislike from other user review")
    public Review removeDislike(@PathVariable("id") @Positive Long reviewId, @PathVariable("userId") Long userId) {
       return reviewService.removeDislike(reviewId, userId);
    }

    @GetMapping
    @Operation(summary = "Get list of film reviews by film id")
    public List<Review> getReviews(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "0") Long filmId
    ) {
        return reviewService.getReviews(filmId, count);
    }
}
