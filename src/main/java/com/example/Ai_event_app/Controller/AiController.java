package com.example.Ai_event_app.Controller;


import com.example.Ai_event_app.DTO.Request.QueryRequestai;
import com.example.Ai_event_app.DTO.Respone.AIResponse;
import com.example.Ai_event_app.Service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/ai")
@Slf4j
@RequiredArgsConstructor

public class AiController {

    private final AiService aiService;

    @PostMapping("/")
    public ResponseEntity<?> Airesult(@RequestBody QueryRequestai queryRequestai){
        log.info(queryRequestai.toString());
       try {


           AIResponse aiResponse = aiService.Creators(queryRequestai.getQuery());
           return ResponseEntity.ok(aiResponse);
       }
       catch (Exception e){
           log.error("AI request failed", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }


    }

    @GetMapping("/AI")
    public ResponseEntity<List<AIResponse>> GetAllResults(){
        List<AIResponse> list = aiService.GetAlldata();

        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


}
