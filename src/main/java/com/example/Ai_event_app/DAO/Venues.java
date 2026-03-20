package com.example.Ai_event_app.DAO;

import com.example.Ai_event_app.Model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Venues extends JpaRepository<Venue, Long> {
}
