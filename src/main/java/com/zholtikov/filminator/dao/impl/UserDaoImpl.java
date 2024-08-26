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

/*    @Override
    public User update(User user) {
        String sql = "update filminator.users set email = ?, " +
                "login = ?, name = ?, birthday = ?";
        jdbcTemplate.update(sql, user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday());
        return findUserById(user.getId());
    }*/

    @Override
    public User update(User user) {
/*        String sql ="INSERT INTO filminator.users (user_id, email, login, name, birthday) VALUES (?,?,?,?,?) " +
                "ON CONFLICT (user_id) DO UPDATE SET" +
                " email = EXCLUDED.email, login = EXCLUDED.login, name = EXCLUDED.name, birthday = EXCLUDED.birthday;";*/


      //  String sql = "INSERT INTO filminator.users (user_id, email, login, name) VALUES (1, 'gizmo@Transglobal', 'Gizmo Transglobal', 'fff') ON CONFLICT (user_id) DO UPDATE SET user_id = EXCLUDED.user_id;";
        String sql = "INSERT INTO filminator.users (user_id, email, login, name, birthday) VALUES (1, 'mail@yandex.ru', 'doloreUpdate', 'est adipisicing', '1976-09-20') ON CONFLICT (user_id) DO UPDATE SET user_id = EXCLUDED.user_id, email = EXCLUDED.email, login = EXCLUDED.login, name = EXCLUDED.name, birthday = EXCLUDED.birthday;";

     /*
        String sql ="INSERT INTO filminator.users (user_id, email, login, name, birthday) VALUES (?,?,?,?,?) " +
                "ON CONFLICT (user_id) DO UPDATE SET" +
                "user_id = EXCLUDED.user_id, email = EXCLUDED.email, login = EXCLUDED.login, name = EXCLUDED.name, birthday = EXCLUDED.birthday;";
*/
     //   String sql ="INSERT INTO filminator.users (user_id, email, login, name, birthday) VALUES (1, 'mail@yandex.ru', 'doloreUpdate', 'est adipisicing', '1976-09-20') ON CONFLICT (user_id) DO UPDATE SET email = EXCLUDED.email, login = EXCLUDED.login, name = EXCLUDED.name, birthday = EXCLUDED.birthday;";

       /* String sql = "insert into filminator.users(user_id, email, login, name, birthday) " +
                "values(?,?,?,?,?) on conflict DO UPDATE SET login = 'ddd';"
               ;*/

    /*    String sql = "insert into filminator.users(email, login, name, birthday) " +
                "values(?,?,?,?) ON CONFLICT (email) DO UPDATE SET login = 'ddd';"
                ;*/
        jdbcTemplate.update(sql);

        /*jdbcTemplate.update(sql, user.getId(), user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday());*/
        return findUserById(user.getId());
    }

   // INSERT INTO distributors (did, dname) VALUES (7, 'Redline GmbH')
  //  ON CONFLICT (did) DO NOTHING;

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

    @Override
    public User deleteUser(Long userId) {
            String sql = "update filminator.users set email = 'deleted.user@deleted" + userId + ".del', " +
                    "login = 'deleted_user_" + userId + "', name = 'deleted_user', birthday = '01-01-01'";
            jdbcTemplate.update(sql);
            return findUserById(userId);
        }



       /* String sqlDeleteUser = "delete from USERS where USER_ID = ?";
        String sqlDeleteUserFromFriends1 = "delete from USER_FRIENDS where INITIATOR_ID = ?";
        String sqlDeleteUserFromFriends2 = "delete from USER_FRIENDS where ACCEPTOR_ID = ?";
        String sqlDeleteUserLike = "delete from FILM_SCORES where USER_ID = ?";
        String sqlDeleteReviewLike = "delete from REVIEW_LIKES where USER_ID = ?";
        String sqlDeleteEvent = "delete from EVENTS where USER_ID = ?";*/
        //   String sqlDeleteFilm = "delete from filminator.films where film_id = " + id + " ;" ; // это если через каскад с схеме
   //     String sql = "delete from filminator.films_genre_link where film_id = " + id +

      /*  String sql = "delete from filminator.films_genre_link where film_id = " + id +
                " ; delete from filminator.likes_films_users_link where user_id = " + userId +
                " ; delete from filminator.friendship_user_to_user_link where user_id = " + userId +
                " ; delete from filminator.friendship_user_to_user_link where friend_id = " + userId +
                " ; delete from filminator.users where user_id = " + userId;*/

      //  jdbcTemplate.update(sql);
   // }

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
