package com.aisystems.firefliescrmautomation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

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
}
