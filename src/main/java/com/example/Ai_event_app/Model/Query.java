package com.example.Ai_event_app.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "queries")
public class Query {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queryId;

    private String queryText;

    @OneToMany(mappedBy = "query", cascade = CascadeType.ALL)
    private List<Venue> venues;

    // getters & setters
}