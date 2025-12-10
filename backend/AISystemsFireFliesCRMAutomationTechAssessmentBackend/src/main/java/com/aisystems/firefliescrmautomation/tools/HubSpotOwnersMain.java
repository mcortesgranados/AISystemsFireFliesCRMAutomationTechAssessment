package com.aisystems.firefliescrmautomation.tools;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Simple standalone runner to fetch HubSpot owners via GET https://api.hubapi.com/owners/v2/owners.
 * Expects the private app token in the environment variable HUBSPOT_API_KEY.
 * @author Manuela Cortés Granados  (manuelacortesgranados@gmail.com)
 * @since 9 December 2025 11:00 AM GMT -5 Bogotá DC Colombia
 */
public class HubSpotOwnersMain {

    // Prefer v3 owners endpoint
    private static final String OWNERS_URL = "https://api.hubapi.com/crm/v3/owners/";

    public static void main(String[] args) {
        String apiKey = System.getenv("HUBSPOT_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("HUBSPOT_API_KEY environment variable is not set.");
            System.exit(1);
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(OWNERS_URL, HttpMethod.GET, request, String.class);
            System.out.println("Status: " + response.getStatusCode().value());
            System.out.println(response.getBody());
        } catch (Exception ex) {
            System.err.println("Failed to fetch owners: " + ex.getMessage());
            System.exit(1);
        }
    }
}
