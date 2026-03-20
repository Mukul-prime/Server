package com.example.Ai_event_app.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venueId;

    private String venueName;
    private String location;
    private Double estimatedCost;

    @Lob
    private String whyItFits;

    @ManyToOne
    @JoinColumn(name = "query_id")
    private Query query;

    // getters & setters
}