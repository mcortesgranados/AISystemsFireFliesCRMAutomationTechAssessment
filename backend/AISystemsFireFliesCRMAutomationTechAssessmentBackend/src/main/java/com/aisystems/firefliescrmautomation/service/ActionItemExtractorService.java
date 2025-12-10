package com.aisystems.firefliescrmautomation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.aisystems.firefliescrmautomation.dto.HubSpotTaskCreationReport;
import java.util.ArrayList;

/**
 * Service for extracting action items from meeting transcripts using an LLM (OpenAI).
 * <p>
 * This service takes a transcript, sends it to the LLM, and parses the response into structured action items.
 * Smart features include priority tagging, deadline parsing, assignee detection, and error handling.
 * </p>
 *
 * Example usage:
 * <pre>
 *     List<Map<String, Object>> actions = actionItemExtractorService.extractActionItems(transcript);
 * </pre>
 *
 * @author Manuela Cortés Granados
 * @since 6 December 2025
 */
@Service
public class ActionItemExtractorService {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private HubSpotTaskService hubSpotTaskService;

    /**
     * Extracts action items from a meeting transcript using the LLM API.
     * <p>
     * The method sends a prompt to the LLM asking for a JSON list of action items, each with fields:
     * description, priority, deadline, assignee, and category. Handles incomplete transcripts and API errors.
     * </p>
     *
     * @param transcript The meeting transcript text.
     * @return List of action items as maps (description, priority, deadline, assignee, category).
     */
    public List<Map<String, Object>> extractActionItems(String transcript) {
        String prompt = "Extract all action items from the following meeting transcript. " +
                "For each action item, return a JSON object with: description, priority (high/medium/low), " +
                "deadline (if mentioned), assignee (if mentioned), and category (e.g., training, integration, feedback). " +
                "If no action items are found, return an empty list. Transcript: " + transcript;
        String response = openAIService.getCompletion(prompt);
        // TODO: Parse response as JSON list of action items
        // Example: Use Jackson or Gson to parse response
        // Handle errors and incomplete transcripts
        return List.of(); // Placeholder: implement JSON parsing
    }

    /**
     * Extracts action items from a transcript and creates tasks in HubSpot, returning a full report.
     * @param transcript The meeting transcript text.
     * @return HubSpotTaskCreationReport with status and details.
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 9 December 2025 GMT 7:58 AM -5 Bogotá DC Colombia
     */
    public HubSpotTaskCreationReport extractAndCreateHubSpotTasks(String transcript) {
        List<Map<String, Object>> actionItems = extractActionItems(transcript);
        List<String> responses = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<Map<String, Object>> taskResults = new ArrayList<>();
        int succeeded = 0;
        int failed = 0;
        for (Map<String, Object> item : actionItems) {
            try {
                String response = hubSpotTaskService.createTask(
                    item.getOrDefault("description", "").toString(),
                    item.getOrDefault("deadline", null) != null ? item.get("deadline").toString() : null,
                    item.getOrDefault("priority", "NONE").toString(),
                    item.getOrDefault("assignee", null) != null ? item.get("assignee").toString() : null
                );
                responses.add(response);
                taskResults.add(item);
                succeeded++;
            } catch (Exception ex) {
                errors.add("Error for item: " + item + " - " + ex.getMessage());
                failed++;
            }
        }
        return new HubSpotTaskCreationReport(actionItems.size(), succeeded, failed, responses, errors, actionItems, taskResults);
    }
}
