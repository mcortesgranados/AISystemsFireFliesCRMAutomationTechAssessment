package com.aisystems.firefliescrmautomation.tools;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Standalone runner that collects all HubSpot deals via GET /crm/v3/objects/deals and deletes each one.
 * Expects the private app token in {@code HUBSPOT_API_KEY}.
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
 */
public class HubSpotDeleteDealsMain {

    private static final String HUBSPOT_DEALS_URL = "https://api.hubapi.com/crm/v3/objects/deals";
    private static final int PAGE_SIZE = 100;

    /**
     * Main entry point. Fetches all HubSpot deal IDs and deletes them one by one.
     * Expects HUBSPOT_API_KEY environment variable to be set.
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
     *
     * @param args Command-line arguments (not used)
     */
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
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        List<String> dealIds = fetchAllDealIds(restTemplate, headers);
        if (dealIds.isEmpty()) {
            System.out.println("No deals found.");
            return;
        }

        System.out.printf("Found %d deals to delete.%n", dealIds.size());
        deleteDeals(restTemplate, headers, dealIds);
    }

    /**
     * Fetches all deal IDs from HubSpot using pagination.
     *
     * @param restTemplate RestTemplate for HTTP requests
     * @param headers      HTTP headers with authentication
     * @return List of deal IDs as strings
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
     */
    private static List<String> fetchAllDealIds(RestTemplate restTemplate, HttpHeaders headers) {
        List<String> ids = new ArrayList<>();
        String after = null;

        do {
            String url = buildDealsUrl(after);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            Map<String, Object> body = response.getBody();
            if (body == null) {
                break;
            }

            List<Map<String, Object>> results = toListOfMaps(body.get("results"));
            for (Map<String, Object> deal : results) {
                if (deal.containsKey("id")) {
                    ids.add(deal.get("id").toString());
                }
            }

            Map<String, Object> paging = toMap(body.get("paging"));
            Map<String, Object> next = paging != null ? toMap(paging.get("next")) : null;
            after = next != null ? safeToString(next.get("after")) : null;
        } while (after != null);

        return ids;
    }

    /**
     * Deletes all deals in HubSpot for the given list of deal IDs.
     *
     * @param restTemplate RestTemplate for HTTP requests
     * @param headers      HTTP headers with authentication
     * @param dealIds      List of deal IDs to delete
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
     */
    private static void deleteDeals(RestTemplate restTemplate, HttpHeaders headers, List<String> dealIds) {
        for (String id : dealIds) {
            String deleteUrl = HUBSPOT_DEALS_URL + "/" + id;
            HttpEntity<Void> request = new HttpEntity<>(headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
                System.out.printf("Deleted deal %s (status %d).%n", id, response.getStatusCode().value());
            } catch (Exception ex) {
                System.err.printf("Failed to delete deal %s: %s%n", id, ex.getMessage());
            }
        }
    }

    /**
     * Builds the URL for fetching deals from HubSpot, with optional pagination.
     *
     * @param after The paging cursor (null for first page)
     * @return The full URL as a string
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
     */
    private static String buildDealsUrl(String after) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(HUBSPOT_DEALS_URL)
                .queryParam("limit", PAGE_SIZE);
        if (after != null) {
            builder.queryParam("after", after);
        }
        return builder.toUriString();
    }

    /**
     * Safely casts an object to a list of maps (used for parsing JSON response).
     *
     * @param raw The raw object to cast
     * @return List of maps, or empty list if not a list
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
     */
    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> toListOfMaps(Object raw) {
        if (raw instanceof List) {
            return (List<Map<String, Object>>) raw;
        }
        return Collections.emptyList();
    }

    /**
     * Safely casts an object to a map (used for parsing JSON response).
     *
     * @param raw The raw object to cast
     * @return Map, or null if not a map
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> toMap(Object raw) {
        if (raw instanceof Map) {
            return (Map<String, Object>) raw;
        }
        return null;
    }

    /**
     * Safely converts an object to a string, returning null if the object is null.
     *
     * @param value The object to convert
     * @return String representation or null
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)    
     * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
     */
    private static String safeToString(Object value) {
        return value == null ? null : value.toString();
    }
}
