package com.example.Ai_event_app.DTO.Respone;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AIResponse {
    private String query;
    private String venueName;
    private String location;
    private Double estimatedCost;
    private String whyItFits;
}
