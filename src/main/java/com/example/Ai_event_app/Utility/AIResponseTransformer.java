package com.example.Ai_event_app.Utility;

import com.example.Ai_event_app.DTO.Respone.AIResponse;
import com.example.Ai_event_app.Model.Venue;

import java.util.List;
import java.util.stream.Collectors;

public class AIResponseTransformer {
    public static AIResponse CreatorAIs(Venue venues)
    {
        return AIResponse.builder()
                .query(
                        venues.getQuery() != null
                                ? venues.getQuery().getQueryText()
                                : null
                )
                .estimatedCost(venues.getEstimatedCost())
                .location(venues.getLocation())
                .venueName(venues.getVenueName())
                .whyItFits(venues.getWhyItFits())
                .build();
    }

    public static List<AIResponse> CreatorAI(List<Venue> venues){
        return venues.stream().map(AIResponseTransformer::CreatorAIs).collect(Collectors.toList());
    }
}

