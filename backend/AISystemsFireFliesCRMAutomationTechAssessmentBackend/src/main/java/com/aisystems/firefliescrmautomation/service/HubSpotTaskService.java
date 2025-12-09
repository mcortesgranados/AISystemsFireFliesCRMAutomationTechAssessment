package com.aisystems.firefliescrmautomation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for creating tasks in HubSpot via the HubSpot API.
 * <p>
 * Requires the environment variable HUBSPOT_API_KEY (private app token) to be set.
 * </p>
 *
 * Example usage:
 * <pre>
 *     hubSpotTaskService.createTask("Follow up with client", "2025-12-10", "high", "jane.doe@example.com");
 * </pre>
 *
 * @author Manuela Cortés Granados
 * @since 9 December 2025
 */
@Service
public class HubSpotTaskService {

    @Value("${HUBSPOT_API_KEY}")
    private String hubspotApiKey;

    private static final String HUBSPOT_TASKS_URL = "https://api.hubapi.com/crm/v3/objects/tasks";

    /**
     * Creates a task in HubSpot with the given details.
     *
     * @param description The task description.
     * @param dueDate     The due date (ISO 8601 format, e.g., 2025-12-10).
     * @param priority    The priority (e.g., high, medium, low).
     * @param assignee    The assignee's email (optional).
     * @return The HubSpot API response as a string.
     */
    public String createTask(String description, String dueDate, String priority, String assignee) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(hubspotApiKey);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hs_task_body", description);
        properties.put("hs_task_priority", priority != null ? priority.toUpperCase() : "NONE"); // Ensure uppercase and default to NONE
        properties.put("hs_timestamp", System.currentTimeMillis());
        if (assignee != null && !assignee.isEmpty()) {
            properties.put("hubspot_owner_id", assignee); // You may need to resolve email to owner ID
        }

        Map<String, Object> body = new HashMap<>();
        body.put("properties", properties);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(HUBSPOT_TASKS_URL, request, String.class);
        return response.getBody();
    }

    /**
     * Creates multiple tasks in HubSpot from a list of action items.
     * Each action item should have keys: description, priority, deadline, assignee.
     * @param actionItems List of action item maps
     * @return List of HubSpot API responses
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
}
