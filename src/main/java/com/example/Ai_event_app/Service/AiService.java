package com.example.Ai_event_app.Service;

import com.example.Ai_event_app.DAO.Querys;
import com.example.Ai_event_app.DAO.Venues;
import com.example.Ai_event_app.DTO.Respone.AIResponse;
import com.example.Ai_event_app.Model.Query;
import com.example.Ai_event_app.Model.Venue;
import com.example.Ai_event_app.Utility.AIResponseTransformer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {
    private final Querys querys;
    private final Venues venues;
    @Value("${gemini.api.key:}")
    private String apiKey;
    @Value("${gemini.model:gemini-2.5-flash}")
    private String geminiModel;

    private String normalizeJsonPayload(String text) {
        if (text == null) {
            return "";
        }

        String normalized = text.trim();
        if (normalized.startsWith("```")) {
            normalized = normalized.replaceFirst("^```(?:json)?\\s*", "");
            normalized = normalized.replaceFirst("\\s*```$", "");
        }

        return normalized.trim();
    }

    public AIResponse Creators(String userQuery) {
        if (apiKey == null || apiKey.isBlank() || "YOUR_GEMINI_API_KEY".equals(apiKey)) {
            throw new IllegalStateException("Gemini API key is missing. Set gemini.api.key in application.properties or as an environment variable.");
        }

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + geminiModel + ":generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        String prompt = """
            You are an AI event planner.
            Return ONLY raw JSON.
            Do not use markdown.
            Do not wrap the JSON in code fences.
            {
              "venue_name": "",
              "location": "",
              "estimated_cost": 0,
              "why_it_fits": ""
            }

            User request: %s
            """.formatted(userQuery);


        Map<String, Object> requestBody = new HashMap<>();

        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));

        requestBody.put("contents", List.of(content));

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        try {

            String response = restTemplate.postForObject(url, request, String.class);

            System.out.println("GEMINI RESPONSE:\n" + response);

            JsonNode root = mapper.readTree(response);

            String text = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

//            System.out.println("AI TEXT:\n" + text);


            String normalizedText = normalizeJsonPayload(text);
//            System.out.println("AI JSON:\n" + normalizedText);
            JsonNode aiJson = mapper.readTree(normalizedText);

            String venueName = aiJson.get("venue_name").asText();
            String location = aiJson.get("location").asText();
            Double cost = aiJson.get("estimated_cost").asDouble();
            String why = aiJson.get("why_it_fits").asText();

            // Save Query
            Query queryEntity = Query.builder()
                    .queryText(userQuery)
                    .build();

            querys.save(queryEntity);

            // Save Venue
            Venue venue = Venue.builder()
                    .venueName(venueName)
                    .location(location)
                    .estimatedCost(cost)
                    .whyItFits(why)
                    .query(queryEntity)
                    .build();

            venues.save(venue);

            return AIResponseTransformer.CreatorAIs(venue);

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Gemini API request failed: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gemini API failed", e);
        }
    }
    public List<AIResponse> GetAlldata() {
        List<Venue> alls = venues.findAll();
        return AIResponseTransformer.CreatorAI(alls);
    }

}
