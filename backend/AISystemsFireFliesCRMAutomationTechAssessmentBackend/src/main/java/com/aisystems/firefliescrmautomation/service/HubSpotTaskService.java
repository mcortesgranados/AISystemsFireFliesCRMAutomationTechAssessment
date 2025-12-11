package com.aisystems.firefliescrmautomation.service;

import com.aisystems.firefliescrmautomation.dto.HubSpotDealDeletionReport;
import com.aisystems.firefliescrmautomation.dto.HubSpotDealDeletionStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for creating deals in HubSpot via the HubSpot API.
 * <p>
 * Requires the environment variable HUBSPOT_API_KEY (private app token) to be set.
 * </p>
 *
 * Example usage:
 * <pre>
 *     hubSpotTaskService.createTask("Follow up with client", "2025-12-10", "high", "80047009");
 * </pre>
 *
 * @author Manuela CortAcs Granados
 * @since 9 December 2025
 */
@Service
public class HubSpotTaskService {

    @Value("${HUBSPOT_API_KEY}")
    private String hubspotApiKey;

    private static final String HUBSPOT_DEALS_URL = "https://api.hubapi.com/crm/v3/objects/deals";

    /**
     * Creates a deal in HubSpot with the given details.
     *
     * ai_systems_description
     * ai_systems_assignee
     * ai_systems_priority
     * ai_systems_deadline
     * 
     * 
     * @param description Deal name/description.
     * @param dueDate     Target close date (ISO 8601 or epoch ms).
     * @param priority    Priority (stored as custom field ai_systems_priority).
     * @param assignee    HubSpot owner id (numeric). If non-numeric, it is ignored.
     * @return The HubSpot API response as a string.
     * @author
     * Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 9 December 2025
     */
    public String createTask(String description, String dueDate, String priority, String assignee) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(hubspotApiKey);

        Map<String, Object> properties = new HashMap<>();
        properties.put("ai_systems_description", description);
        properties.put("ai_systems_deadline", dueDate);
        properties.put("ai_systems_assignee", assignee);
        properties.put("ai_systems_priority", priority != null ? priority.toUpperCase() : "NONE");



        Map<String, Object> body = new HashMap<>();
        body.put("properties", properties);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(HUBSPOT_DEALS_URL, request, String.class);
        return response.getBody();
    }

    /**
     * Creates multiple deals in HubSpot from a list of action items.
     * Each action item should have keys: description, priority, deadline, assignee.
     * @param actionItems List of action item maps
     * @return List of HubSpot API responses
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 9 December 2025
     */
    public List<String> createTasksFromActionItems(List<Map<String, Object>> actionItems) {
        List<String> responses = new java.util.ArrayList<>();
        for (Map<String, Object> item : actionItems) {
            String description = item.getOrDefault("description", "").toString();
            String priority = item.getOrDefault("priority", "NONE").toString();
            String deadline = item.getOrDefault("deadline", null) != null ? item.get("deadline").toString() : null;
            String assignee = item.getOrDefault("assignee", null) != null ? item.get("assignee").toString() : null;
            String response = createTask(description, deadline, priority, assignee);
            responses.add(response);
        }
        return responses;
    }

    /**
     * Fetches every deal in HubSpot, logs the identifiers, and deletes them.
     * <p>
     * This is the same pagination/DELETE flow that the standalone tool executed.
     * </p>
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
     */
    public HubSpotDealDeletionReport deleteAllHubSpotDeals() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(hubspotApiKey);

        List<String> dealIds = new ArrayList<>();
        String after = null;
        List<String> fetchErrors = new ArrayList<>();

        do {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(HUBSPOT_DEALS_URL)
                    .queryParam("limit", 100);
            if (after != null) {
                builder.queryParam("after", after);
            }

            ResponseEntity<Map> response;
            try {
                response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                        new HttpEntity<>(headers), Map.class);
            } catch (Exception ex) {
                fetchErrors.add("Failed to paginate deals: " + ex.getMessage());
                break;
            }

            Map<String, Object> body = response.getBody();
            if (body == null) {
                fetchErrors.add("HubSpot deals response body was empty.");
                break;
            }

            Object resultsObj = body.get("results");
            if (resultsObj instanceof List) {
                for (Object deal : (List<?>) resultsObj) {
                    if (deal instanceof Map) {
                        Object idValue = ((Map<?, ?>) deal).get("id");
                        if (idValue != null) {
                            dealIds.add(idValue.toString());
                        }
                    }
                }
            }

            after = null;
            Object pagingObj = body.get("paging");
            if (pagingObj instanceof Map) {
                Object nextObj = ((Map<?, ?>) pagingObj).get("next");
                if (nextObj instanceof Map) {
                    Object afterValue = ((Map<?, ?>) nextObj).get("after");
                    if (afterValue != null) {
                        after = afterValue.toString();
                    }
                }
            }
        } while (after != null);

        List<HubSpotDealDeletionStatus> statuses = new ArrayList<>();
        int deletedCount = 0;
        int failedCount = 0;
        for (String id : dealIds) {
            String deleteUrl = HUBSPOT_DEALS_URL + "/" + id;
            try {
                ResponseEntity<String> deleteResponse = restTemplate.exchange(deleteUrl, HttpMethod.DELETE,
                        new HttpEntity<>(headers), String.class);
                boolean success = deleteResponse.getStatusCode().is2xxSuccessful();
                String message = "Status " + deleteResponse.getStatusCode().value();
                statuses.add(new HubSpotDealDeletionStatus(id, success, message));
                if (success) {
                    deletedCount++;
                } else {
                    failedCount++;
                }
            } catch (Exception ex) {
                failedCount++;
                statuses.add(new HubSpotDealDeletionStatus(id, false, ex.getMessage()));
            }
        }

        return new HubSpotDealDeletionReport(
                dealIds.size(),
                deletedCount,
                failedCount,
                statuses,
                fetchErrors
        );
    }

    /**
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 11 December 2025 01:53 AM GMT -5 Bogotá DC Colombia
     * @param raw
     * @return
     */

    // Convert human-friendly or ISO strings to epoch millis if possible; otherwise return null.
    private Long tryParseCloseDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            // Try numeric epoch ms
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException ignore) {
            // Try ISO-8601 date/time
            try {
                return java.time.Instant.parse(raw.trim()).toEpochMilli();
            } catch (Exception ignore2) {
                // Try LocalDate with midnight UTC
                try {
                    return java.time.LocalDate.parse(raw.trim()).atStartOfDay(java.time.ZoneOffset.UTC).toInstant().toEpochMilli();
                } catch (Exception ignore3) {
                    return null;
                }
            }
        }
    }
}
