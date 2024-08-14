package com.zholtikov.filminator.dao.impl;

import com.zholtikov.filminator.dao.ReviewDao;
import com.zholtikov.filminator.exceptions.ReviewNotFoundException;
import com.zholtikov.filminator.model.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review addReview(Review review) {
        String sql = "insert into filminator.reviews (CONTENT, POSITIVE, USER_ID, FILM_ID)" +
                "values (?, ?, ?, ?) ";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"review_id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            return stmt;
        }, keyHolder);

        Long key = Objects.requireNonNull(keyHolder.getKey()).longValue();
        review.setReviewId(key);
        log.info("Добавили новое ревью с id{} ", key);
        return getReview(key);
    }

    @Override
    public Review updateReview(Review review) throws ReviewNotFoundException {

        String sql = "update filminator.reviews set CONTENT = ?, POSITIVE = ? , REVIEW_ID = ?";
        int rowsUpdated = jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());
        //   int rowsUpdated = jdbcTemplate.update(sql);
        if (rowsUpdated == 0) {
            throw new ReviewNotFoundException("Ревью с id " + review.getReviewId() + " не найден");
        }
        log.info("Обновлено ревью с id {}", review.getReviewId());
        return getReview(review.getReviewId());
    }


    @Override
    public void deleteReview(Long id) {
        String sql = "delete from filminator.reviews where REVIEW_ID = ?";
        jdbcTemplate.update(sql, id);
        log.info("Удалили ревью с id{} " + id);
    }

    @Override
    public Review getReview(Long reviewId) {
        final String sql = "select r.review_id, r.content, r.positive, r.user_id, r.film_id, " +

                " coalesce(sum(case when rul.helpful then 1  when not rul.helpful then -1 end), 0 ) as useful" +

                // "  coalesce((sum(case when rul.helpful then 2 end) - sum(case when not rul.helpful then 10 end)), 0 ) as useful" + // works too

                " from filminator.reviews as r " +
                "left join filminator.reviews_users_link as rul on r.review_id = rul.review_id " +
                "where r.review_id = " + reviewId.intValue() +
                " group by r.review_id ";

        log.info("Получили ревью с id{} " + reviewId);
        Optional<Review> optionalReview = jdbcTemplate.queryForObject(sql, this::makeReview);
        if (optionalReview.isEmpty()) {
            throw new ReviewNotFoundException("Review with id \"" + reviewId + "\" not found.");
        } else {
            System.out.println(optionalReview.get());
            return optionalReview.get();
        }
    }

    @Override
    public List<Review> getAllReviews(int count) {
        log.info("Получили все ревью");

        final String sql = "select r.review_id, r.content, r.positive, r.user_id, r.film_id, " +
                " coalesce(sum(case when rul.helpful then 1  when not rul.helpful then -1 end), 0 ) as useful" +
                " from filminator.reviews as r " +
                "left join filminator.reviews_users_link as rul on r.review_id = rul.review_id " +
                " group by r.review_id " +
                " order by useful desc, r.review_id asc  " +
                " limit " + count;
        List<Optional<Review>> queryResult = jdbcTemplate.query(sql, this::makeReview);
        List<Review> reviews = new ArrayList<>();
        for (Optional<Review> optionalReview : queryResult) {
            optionalReview.ifPresent(reviews::add);
        }
        return reviews;
    }

    @Override
    public List<Review> getFilmReviews(Long filmId, int count) {

        final String sql = "select r.review_id, r.content, r.positive, r.user_id, r.film_id, " +
                " coalesce(sum(case when rul.helpful then 1  when not rul.helpful then -1 end), 0 ) as useful" +
                " from filminator.reviews as r " +
                "left join filminator.reviews_users_link as rul on r.review_id = rul.review_id " +
                "where r.film_id = " + filmId +
                " group by r.review_id " +
                " order by useful desc, r.review_id asc" +
                " limit " + count;
        List<Optional<Review>> queryResult = jdbcTemplate.query(sql, this::makeReview);
        List<Review> reviews = new ArrayList<>();
        for (Optional<Review> optionalReview : queryResult) {
            optionalReview.ifPresent(reviews::add);
        }
        return reviews;
    }

    @Override
    public Review likeReview(Long reviewId, Long userId) {
        String sqlAddLike = "insert into filminator.reviews_users_link (review_id, user_id, helpful) values (?, ?, true)";
        jdbcTemplate.update(sqlAddLike, reviewId, userId);
        log.info("Лайк ревью с ID: " + reviewId);
        return getReview(reviewId);
    }

    @Override
    public Review removeLike(Long reviewId, Long userId) {
        String sqlRemoveLike = "delete from filminator.reviews_users_link " +
                "where review_id = ? and user_id = ?";
        jdbcTemplate.update(sqlRemoveLike, reviewId, userId);
        log.info("Удалили лайк ревью с ID: " + reviewId);
        return getReview(reviewId);
    }

    @Override
    public Review dislikeReview(Long reviewId, Long userId) {
        String sqlAddLike = "insert into filminator.reviews_users_link (review_id, user_id, helpful) values (?, ?, false)";
        jdbcTemplate.update(sqlAddLike, reviewId, userId);
        log.info("Поcтавили дислайк ревью с ID: " + reviewId);
        return getReview(reviewId);
    }

    @Override
    public Review removeDislike(Long reviewId, Long userId) {
        String sqlRemoveLike = "delete from filminator.reviews_users_link " +
                "where review_id = ? and user_id = ?";
        jdbcTemplate.update(sqlRemoveLike, reviewId, userId);
        log.info("Удалили дисклайк ревью с ID: " + reviewId);
        return getReview(reviewId);
    }

    Optional<Review> makeReview(ResultSet rs, int rowNum) throws SQLException {
        Review reviewBuilt = Review.builder()
                .reviewId(rs.getLong("review_id"))
                .content(rs.getString("CONTENT"))
                .isPositive(rs.getBoolean("POSITIVE"))
                .userId(rs.getLong("USER_ID"))
                .filmId(rs.getLong("FILM_ID"))
                .useful(rs.getInt("USEFUL"))
                .build();

        return Optional.of(reviewBuilt);
    }

    @Override
    public void checkReviewExistence(Long reviewId) {
        final String sql = "select COUNT(r.review_id) from filminator.reviews as r where review_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, reviewId);
        if (count == null || count == 0) {
            throw new ReviewNotFoundException("Review with id \"" + reviewId + "\" not found.");
        }
    }

}
