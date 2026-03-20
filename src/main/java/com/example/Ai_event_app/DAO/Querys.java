package com.example.Ai_event_app.DAO;

import com.example.Ai_event_app.Model.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Querys extends JpaRepository<Query, Long> {
}
