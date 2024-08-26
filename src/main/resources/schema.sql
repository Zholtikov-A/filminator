
DROP SCHEMA IF  EXISTS filminator CASCADE;

CREATE SCHEMA IF NOT EXISTS filminator;

CREATE TABLE IF NOT EXISTS filminator.users
(
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(254) NOT NULL UNIQUE,
    login VARCHAR(40) NOT NULL UNIQUE,
    name VARCHAR(40) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS filminator.genre
(
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS filminator.mpa_rating
(
    mpa_rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL
);

create table IF NOT EXISTS filminator.directors
(
    director_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
      name VARCHAR(50) NOT NULL

);

--create table IF NOT EXISTS filminator.directors
--(
  --  director_id INTEGER auto_increment,
 --   name      VARCHAR_IGNORECASE(50),
 --   constraint DIRECTOR_PK
  --      primary key (director_id)
--);

CREATE TABLE IF NOT EXISTS filminator.films
(
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa_rating_id INTEGER,
    rate INTEGER,
    FOREIGN KEY (mpa_rating_id) REFERENCES filminator.mpa_rating(mpa_rating_id)
);

CREATE TABLE IF NOT EXISTS filminator.likes_films_users_link
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (film_id) REFERENCES filminator.films(film_id) ON delete CASCADE,
  FOREIGN KEY (user_id) REFERENCES filminator.users(user_id)
);

CREATE TABLE IF NOT EXISTS filminator.friendship_user_to_user_link
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES filminator.users(user_id) ON delete CASCADE,
    FOREIGN KEY (friend_id) REFERENCES filminator.users(user_id) ON delete CASCADE
);

CREATE TABLE IF NOT EXISTS filminator.films_genre_link
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    FOREIGN KEY (film_id) REFERENCES filminator.films(film_id) ON delete CASCADE,
    FOREIGN KEY (genre_id) REFERENCES filminator.genre(genre_id)
);

create table IF NOT EXISTS filminator.film_directors
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id     INTEGER not null,
    director_id INTEGER not null,
       FOREIGN KEY (film_id) REFERENCES filminator.films(film_id) ON delete CASCADE,
        FOREIGN KEY (director_id) REFERENCES filminator.directors(director_id)
);




-- create table IF NOT EXISTS filminator.film_directors
--(
 --   film_id     INTEGER not null,
  --  director_id INTEGER not null,
   -- constraint FILM_DIRECTORS_PK
    --    primary key (FILM_ID, DIRECTOR_ID),
  --  constraint FILM_DIRECTORS_FILM_ID_FK
   --     foreign key (FILM_ID) references filminator.films,
  --  constraint FILM_DIRECTORS_DIRECTOR_ID_FK
   --     foreign key (DIRECTOR_ID) references filminator.directors
    --        on update set null on delete set null
--);

create table IF NOT EXISTS filminator.reviews
(
    --review_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    REVIEW_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    CONTENT   CHARACTER VARYING(5000) not null,
    POSITIVE  BOOLEAN                 not null,
    USER_ID   INTEGER                 not null,
    FILM_ID   INTEGER                 not null,
 --   constraint REVIEW_PK
  --      primary key (REVIEW_ID),
  --  constraint REVIEW_USERS_USER_ID_FK
  --      foreign key (USER_ID) references USERS (USER_ID) ON delete CASCADE,
  --  constraint REVIEW_FILM_FILM_ID_FK_2
   --     foreign key (FILM_ID) references FILMS (FILM_ID) ON delete CASCADE
   FOREIGN KEY (user_id) REFERENCES filminator.users(user_id) ON delete CASCADE,
   FOREIGN KEY (film_id) REFERENCES filminator.films(film_id) ON delete CASCADE
);

create table IF NOT EXISTS filminator.reviews_users_link
(
  --  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,

    REVIEW_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    USER_ID   INTEGER not null,
    HELPFUL   BOOLEAN not null,
  --  constraint REVIEW_LIKES_PK
   --     primary key (REVIEW_ID, USER_ID),
   -- constraint REVIEW_LIKES_USERS_USER_ID_FK
   --     foreign key (USER_ID) references USERS (USER_ID) ON delete CASCADE,
  --  constraint REVIEW_LIKES_REVIEW_REVIEW_ID_FK_2
   --     foreign key (REVIEW_ID) references REVIEWS (REVIEW_ID) ON delete CASCADE

   FOREIGN KEY (review_id) REFERENCES filminator.reviews(review_id) ON delete CASCADE,
  FOREIGN KEY (user_id) REFERENCES filminator.users(user_id) ON delete CASCADE

);

create type filminator.EVENT as ENUM ('SCORE', 'FRIEND', 'REVIEW', 'LIKE');
create type filminator.OPERATION as ENUM ('ADD', 'UPDATE', 'REMOVE');

create table IF NOT EXISTS filminator.events
(
    EVENT_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    TIMESTAMP  bigint not null,
    USER_ID  INTEGER not null,
    ENTITY_ID  INTEGER not null,
    OPERATION  OPERATION,
    EVENT_TYPE  EVENT,
   foreign key (user_id) references filminator.users (user_id)
);

ALTER TABLE filminator.films_genre_link
ADD UNIQUE (film_id, genre_id)
;

ALTER TABLE filminator.film_directors
ADD UNIQUE (film_id, director_id)
;

