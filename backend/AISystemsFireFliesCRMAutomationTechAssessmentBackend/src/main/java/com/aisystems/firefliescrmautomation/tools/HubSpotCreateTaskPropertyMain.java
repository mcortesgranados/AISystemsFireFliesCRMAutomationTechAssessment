package com.aisystems.firefliescrmautomation.tools;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple standalone runner to create a custom task property in HubSpot.
 * Expects the private app token in the environment variable HUBSPOT_API_KEY.
 * The property created is an enumeration named "prioridad_1" with options High, Medium, and Low.
 * The property is intended to categorize tasks by their priority level.
 * @author Manuela Cortés Granados  (manuelacortesgranados@gmail.com
 * @since 9 December 2025 10:00 AM GMT -5 Bogotá DC Colombia)
 */
public class HubSpotCreateTaskPropertyMain {

    private static final String PROPERTIES_URL = "https://api.hubapi.com/crm/v3/properties/tasks";

    public static void main(String[] args) {
        String apiKey = System.getenv("HUBSPOT_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("HUBSPOT_API_KEY environment variable is not set.");
            System.exit(1);
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("name", "prioridad_1");
        body.put("label", "prioridad_1");
        body.put("description", "prioridad_1");
        body.put("groupName", "taskinformation");
        body.put("type", "enumeration");
        body.put("fieldType", "select");
        body.put("options", List.of(
                option("High", "HIGH"),
                option("Medium", "MEDIUM"),
                option("Low", "LOW")
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(PROPERTIES_URL, HttpMethod.POST, request, String.class);
            System.out.println("Status: " + response.getStatusCode().value());
            System.out.println(response.getBody());
        } catch (Exception ex) {
            System.err.println("Failed to create property: " + ex.getMessage());
            System.exit(1);
        }
    }

    private static Map<String, Object> option(String label, String value) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("label", label);
        opt.put("value", value);
        return opt;
    }
}
