package com.realnet.OpenAi.Services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiServices {

    @Value("${chatgpt.api.url}")
    private String apiUrl;

    @Value("${chatgpt.api.key}")
    private String apiKey;

    public String getChatGPTResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // Create body
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo"); // Updated model
        body.put("prompt", prompt);
        body.put("max_tokens", 150);
        body.put("temperature", 0.7);

        // Build request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Make API call
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

        return response.getBody();
    }
}
