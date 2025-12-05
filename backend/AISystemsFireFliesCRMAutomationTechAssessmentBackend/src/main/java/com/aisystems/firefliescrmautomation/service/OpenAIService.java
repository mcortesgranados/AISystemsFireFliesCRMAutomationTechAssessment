package com.aisystems.firefliescrmautomation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for integrating with the OpenAI API to generate text completions.
 * <p>
 * This service uses the OpenAI completions endpoint and supports the instruct-compatible turbo model.
 * The API key is injected from the system environment variable OPENAI_API_KEY for security.
 * </p>
 *
 * Example usage:
 * <pre>
 *     String result = openAIService.getCompletion("Hello, world!");
 * </pre>
 *
 * @author Manuela Cortés Granados (manuelcortesgranados@gmail.com)
 * @since 5 December 2025 11:10 AM GMT -5 Bogotá DC Colombia
 */
@Service
public class OpenAIService {

    /**
     * The OpenAI API key, injected from the system environment variable OPENAI_API_KEY.
     */
    @Value("${OPENAI_API_KEY}")
    private String openaiApiKey;

    /**
     * The OpenAI completions endpoint URL.
     */
    private static final String OPENAI_URL = "https://api.openai.com/v1/completions";

    /**
     * Calls the OpenAI completions endpoint with the given prompt and returns the generated text.
     * <p>
     * Uses the instruct-compatible turbo model (gpt-3.5-turbo-instruct). The maximum number of tokens is set to 100.
     * </p>
     *
     * @param prompt The prompt to send to OpenAI for text generation.
     * @return The generated text from OpenAI, or a message if no response is received.
     * @author Manuela Cortés Granados (manuelcortesgranados@gmail.com)
     * @since 5 December 2025 11:12 AM GMT -5 Bogotá DC Colombia
     */
    public String getCompletion(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        Map<String, Object> body = new HashMap<>();
        // text-davinci-003 is deprecated; use the instruct-compatible turbo model instead
        body.put("model", "gpt-3.5-turbo-instruct");
        body.put("prompt", prompt);
        body.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, request, Map.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object choices = response.getBody().get("choices");
            if (choices instanceof java.util.List && !((java.util.List<?>) choices).isEmpty()) {
                Object first = ((java.util.List<?>) choices).get(0);
                if (first instanceof Map) {
                    Object text = ((Map<?, ?>) first).get("text");
                    return text != null ? text.toString() : "";
                }
            }
        }
        return "No response from OpenAI.";
    }
}
