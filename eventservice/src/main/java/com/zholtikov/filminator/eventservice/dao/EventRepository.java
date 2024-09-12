package com.zholtikov.filminator.eventservice.dao;

import com.zholtikov.filminator.eventservice.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {


    List<Event> findAllByUserId(Long userId);

}
