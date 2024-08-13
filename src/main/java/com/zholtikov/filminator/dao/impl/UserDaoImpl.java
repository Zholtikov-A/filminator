package com.zholtikov.filminator.dao.impl;

import com.zholtikov.filminator.dao.UserDao;
import com.zholtikov.filminator.exceptions.CustomValidationException;
import com.zholtikov.filminator.exceptions.UserNotFoundException;
import com.zholtikov.filminator.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

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
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) throws CustomValidationException {

        String sql = "insert into filminator.users(email, login, name, birthday) " +
                "values(?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        Long key = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return findUserById(key);
    }

    @Override
    public User update(User user) {
        String sql = "update filminator.users set email = ?, " +
                "login = ?, name = ?, birthday = ?";
        jdbcTemplate.update(sql, user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday());
        return findUserById(user.getId());
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from filminator.users";
        List<Optional<User>> queryResult = jdbcTemplate.query(sql, this::mapRowToUser);
        List<User> users = new ArrayList<>();
        for (Optional<User> optionalUser : queryResult) {
            optionalUser.ifPresent(users::add);
        }
        return users;
    }

    @Override
    public User findUserById(Long id) {
        final String sql = "select * from filminator.users where user_id = ?";
        Optional<User> optionalUser = jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with id \"" + id + "\" not found.");
        } else {
            return optionalUser.get();
        }
    }

    public void addFriend(Long userId, Long friendId) {
        String sql = "insert into filminator.friendship_user_to_user_link(user_id, friend_id) " +
                "values(?,?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            return stmt;
        });
    }

    @Override
    public List<User> findFriends(Long userId) {
        final String sql = "select * " +
                "from filminator.users as u " +
                "where u.USER_ID in (select f.friend_id " +
                "from filminator.friendship_user_to_user_link as f " +
                "where f.user_id = ?);";

        List<Optional<User>> queryResult = jdbcTemplate.query(sql, this::mapRowToUser, userId);
        List<User> users = new ArrayList<>();
        for (Optional<User> optionalUser : queryResult) {
            optionalUser.ifPresent(users::add);
        }
        return users;
    }

    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        final String sql = "select u.* " +
                "from filminator.friendship_user_to_user_link as f1 " +
                "join filminator.friendship_user_to_user_link as f2 on f2.friend_id = f1.friend_id " +
                "join filminator.users as u on f1.friend_id = u.user_id " +
                "where f1.user_id = ? " +
                "and f2.user_id = ? ; ";

        List<Optional<User>> queryResult = jdbcTemplate.query(sql, this::mapRowToUser, userId, otherUserId);
        List<User> users = new ArrayList<>();
        for (Optional<User> optionalUser : queryResult) {
            optionalUser.ifPresent(users::add);
        }
        return users;
    }

    public User removeFriend(Long userId, Long friendId) {
        final String sql = "delete from filminator.friendship_user_to_user_link " +
                "where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
        return findUserById(userId);
    }

    private Optional<User> mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getLong("USER_ID"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        return Optional.of(user);
    }

    @Override
    public void checkUserExistence(Long id) {
        final String sql = "select COUNT(u.user_id) from filminator.users as u where user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new UserNotFoundException("User with id \"" + id + "\" not found.");
        }
    }

}
