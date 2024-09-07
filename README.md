# Filminator 🎥

Проект социальной сети, которая поможет выбрать кино на основе того, какие фильмы вы и ваши друзья смотрите
и какие оценки им ставите. Пользователи могут добавляться в друзья, добавлять редактировать фильмы,
оставлять отзывы и оценки, осуществлять поиск фильмов, получать рекомендации к просмотру и выборки самых популярных фильмов.

<div>
<img width="1024" alt="Racoon and cards of movies" src="assets/Filminator landing.png">
</div>

## Архитектура 🧩
Два микросервиса, БД и Kafka-server. Упаковано в docker-container.

## Основная функциональность
* Регистрация и получение информации о зарегистрированных пользователях
* Добавление и удаление пользователей в список друзей
* Пользователь может получить список своих друзей, а также список друзей, общих с другим пользователем
* Добавление, обновление и удаление фильмов
* Фильмы классифицируются по жанрам и возрастным рейтингам
* Пользователи могут ставить лайки
* Получение подборки самых популярных фильмов
## Дополнительная функциональность
* Ведение журнала действий пользователя
* Выдача истории действий пользователя.

## API (Swagger)
Во время работы проекта API доступно по ссылке:
http://localhost:8081/swagger-ui/index.html

## 🛠 Tech & Tools

<div>
      <img src="https://github.com/Salaia/icons/blob/main/green/Java.png?raw=true" title="Java" alt="Java" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/SPRING%20boot.png?raw=true" title="Spring Boot" alt="Spring Boot" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/Maven.png?raw=true" title="Apache Maven" alt="Apache Maven" height="40"/>
    <img src="https://github.com/Salaia/icons/blob/main/green/JDBC.png?raw=true" title="JDBC" alt="JDBC" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/PostgreSQL.png?raw=true" title="PostgreSQL" alt="PostgreSQL" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/Swagger.png?raw=true" title="Swagger" alt="Swagger" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/Kafka-3.png?raw=true" title="Kafka" alt="Kafka" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/Lombok.png?raw=true" title="Lombok" alt="Lombok" height="40"/>
</div>

## Планы по развитию
* Заменить в модуле Eventservice JBDC Template на Data JPA c реализацией Hibernate
* Проработать варианты развертывания и взаимодействия модулей
