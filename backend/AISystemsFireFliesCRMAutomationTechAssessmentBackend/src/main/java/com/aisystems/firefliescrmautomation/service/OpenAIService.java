package com.aisystems.firefliescrmautomation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import com.aisystems.firefliescrmautomation.service.HubSpotTaskService;
import com.aisystems.firefliescrmautomation.dto.HubSpotTaskCreationReport;

/**
 * Service for integrating with the OpenAI API to generate chat completions.
 * <p>
 * This service uses the OpenAI chat completions endpoint and the gpt-4o-mini model.
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
     * The OpenAI chat completions endpoint URL.
     */
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    @Autowired
    private HubSpotTaskService hubSpotTaskService;

    /**
     * Calls the OpenAI chat-completions endpoint with the given prompt and returns the generated text.
     * <p>
     * Uses gpt-4o-mini with up to 512 max tokens on the first attempt.
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

        // First attempt
        String text = fetchCompletion(restTemplate, headers, prompt, 512);
        if (text != null && !text.trim().isEmpty()) {
            return text.trim();
        }

        // Retry once with stronger instruction to avoid empty completions
        String retryPrompt = prompt + "\nPlease respond with a short, non-empty completion.";
        text = fetchCompletion(restTemplate, headers, retryPrompt, 256);
        if (text != null && !text.trim().isEmpty()) {
            return text.trim();
        }

        return "No response from OpenAI.";
    }

    /**
     * Internal helper to post a completion request and extract the first text choice.
     */
    private String fetchCompletion(RestTemplate restTemplate, HttpHeaders headers, String prompt, int maxTokens) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("temperature", 0.7);
        body.put("max_tokens", maxTokens);
        body.put("messages", java.util.List.of(
                java.util.Map.of("role", "user", "content", prompt)
        ));

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object choices = response.getBody().get("choices");
                if (choices instanceof java.util.List && !((java.util.List<?>) choices).isEmpty()) {
                    Object first = ((java.util.List<?>) choices).get(0);
                    if (first instanceof Map) {
                        Object message = ((Map<?, ?>) first).get("message");
                        if (message instanceof Map) {
                            Object content = ((Map<?, ?>) message).get("content");
                            return content != null ? content.toString() : null;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            // Let caller handle fallback if an exception occurs.
        }
        return null;
    }

    /**
     * Extracts action items from a Fireflies meeting transcript using OpenAI.
     * Returns a list of action items with smart features: priority, deadline, assignee.
     * Handles errors gracefully.
     *
     * @param transcript The meeting transcript text.
     * @return List of action items, each as a map with keys: description, priority, deadline, assignee.
     * @author Manuela Cortés Granados (manuelcortesgranados@gmail.com)
     * @since 9 December 2025 GMT -5 Bogotá DC Colombia
     */
    public List<Map<String, Object>> extractActionItemsFromTranscript(String transcript) {
        List<Map<String, Object>> actionItems = new ArrayList<>();

        String extractionPrompt = "Extract all action items from the following meeting transcript. " +
            "For each action item, provide: description, priority (HIGH/MEDIUM/LOW), deadline (if mentioned), and assignee (if mentioned). " +
            "Return the result as a JSON array of objects with keys: description, priority, deadline, assignee. " +
            "Transcript: " + transcript;

        try {
            String json = getCompletion(extractionPrompt);
            if (json != null && !json.trim().isEmpty()) {
                // Strip Markdown code fences if present
                json = json.trim();
                if (json.startsWith("```")) {
                    json = json.replaceFirst("```json", "")
                               .replaceFirst("```", "");
                }
                // Try to parse the JSON array
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    actionItems = mapper.readValue(json.trim(), mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                } catch (Exception parseEx) {
                    // If parsing fails, return a single item with raw text
                    Map<String, Object> raw = new HashMap<>();
                    raw.put("raw_output", json.trim());
                    actionItems.add(raw);
                }
            }
        } catch (Exception ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to extract action items: " + ex.getMessage());
            actionItems.add(error);
        }
        return actionItems;
    }

    /**
     * Extracts action items from a transcript and creates corresponding HubSpot tasks.
     * Returns a report DTO with aggregated status.
     * @param baseTranscript meeting transcript text
     * @return HubSpotTaskCreationReport summarizing the operation
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 9 December 2025 GMT 9:52 AM -5 Bogotá DC Colombia
     */
    public HubSpotTaskCreationReport createTasksFromTranscript(String baseTranscript) {
        List<String> responses = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<Map<String, Object>> actionItems = new ArrayList<>();
        List<Map<String, Object>> taskResults = new ArrayList<>();
        try {
            actionItems = extractActionItemsFromTranscript(baseTranscript);
            responses = hubSpotTaskService.createTasksFromActionItems(actionItems);
            // Build user-friendly task result summary
            for (int i = 0; i < responses.size(); i++) {
                Map<String, Object> result = new HashMap<>();
                if (i < actionItems.size()) {
                    result.putAll(actionItems.get(i));
                }
                String raw = responses.get(i);
                result.put("hubspotRawResponse", raw);
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    Map<?, ?> parsed = mapper.readValue(raw, Map.class);
                    Object id = parsed.get("id");
                    Object url = parsed.get("url");
                    result.put("hubspotTaskId", id);
                    result.put("hubspotTaskUrl", url);
                } catch (Exception ignored) {
                    // keep raw response only
                }
                taskResults.add(result);
            }
        } catch (Exception ex) {
            errors.add("Failed to create tasks: " + ex.getMessage());
        }

        int totalRequested = actionItems != null ? actionItems.size() : 0;
        int totalFailed = errors.size();
        int totalSucceeded = Math.max(0, totalRequested - totalFailed);

        return new HubSpotTaskCreationReport(totalRequested, totalSucceeded, totalFailed, responses, errors, actionItems, taskResults);
    }

    /**
     * Generates a random sample meeting transcript with similar structure and action items
     * to the provided Fireflies transcript, using ChatGPT.
     * @return A random sample transcript as a String.
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 5 December 2025 11:25 AM GMT -5 Bogotá DC Colombia
     */
    public String generateRandomSampleTranscript() {
        String baseTranscript = "Meeting Date: November 15, 2024\n" +
            "Participants: Lisa, Jennifer (Real Estate Agent)\n" +
            "Lisa: Hi Jennifer, thanks for hopping on this call. I wanted to walk through where we are with your Follow Up Boss setup and make sure we're on track for your go-live date.\n" +
            "Jennifer: Absolutely! I'm excited to finally get this rolling. I've been so overwhelmed trying to manage my leads manually.\n" +
            "Lisa: Totally understandable. So, based on what we discussed last week, here's what we need to knock out over the next two weeks.\n" +
            "Lisa: First, we need to finalize your lead source tracking. I'll need you to send me a list of all your lead sources—Zillow, Realtor.com, your website, referrals, open houses, etc.—by Friday.\n" +
            "Jennifer Martinez: Got it. I'll pull that together and email it to you by end of week.\n" +
            "Lisa: Perfect. Second, we need to set up your automated drip campaigns. I'm going to need you to review the email templates I sent you last Monday and let me know if you want to make any changes. Can you get me your feedback by Wednesday?\n" +
            "Jennifer Martinez: Yes, I'll review those tonight and send you my edits by Wednesday morning.\n" +
            "Lisa: Awesome. Third, we need to schedule a training session for your assistant, Sarah, so she knows how to use Follow Up Boss for lead entry and task management. Can you have her pick a time on my calendar for next week?\n" +
            "Jennifer Martinez: Absolutely. I'll have her book something today.\n" +
            "Lisa: Great. And lastly, I want to make sure we're integrating your showing software—ShowingTime, right?—with Follow Up Boss so that when a showing gets scheduled, it automatically creates a follow-up task. I'll handle the technical setup, but I'll need your ShowingTime login credentials. Can you send those to me via our secure portal by Thursday?\n" +
            "Jennifer Martinez: Yep, I'll do that tomorrow.\n" +
            "Lisa: Perfect. So just to recap: lead source list by Friday, email template feedback by Wednesday, Sarah books her training for next week, and ShowingTime credentials by Thursday. Does that all sound doable?\n" +
            "Jennifer Martinez: Yes, totally doable. I really appreciate you breaking this down for me.\n" +
            "Lisa: Of course! That's what we're here for. I'll check in with you on Friday to make sure everything's on track, and we should be good to go live by December 1st.\n" +
            "Jennifer Martinez: Sounds great. Thanks, Lisa!\n" +
            "Lisa: Anytime. Talk soon!";
        String prompt = "Generate a random sample meeting transcript with similar structure, participants, and action items as the following transcript. Change names, dates, and details, but keep the format and number of action items. Transcript: " + baseTranscript;
        try {
            String generated = getCompletion(prompt + " Return only the transcript text lines. Do not include explanations or JSON.");
            if (generated == null || generated.trim().isEmpty()) {
                // Retry once with a stricter instruction if the first call came back empty.
                generated = getCompletion(prompt + " Respond with transcript text only. Include speaker names and action items. Do not return empty text.");
            }
            // If OpenAI still returns an empty/blank payload, fall back to the static example so the endpoint isn't empty.
            if (generated == null || generated.trim().isEmpty()) {
                return baseTranscript;
            }
            return generated.trim();
        } catch (Exception ex) {
            // Always return content even if the upstream OpenAI call fails.
            return baseTranscript;
        }
    }

    /**
     * Returns the full OpenAI API response for a random sample meeting transcript.
     * @return Map containing the full OpenAI API response.
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 5 December 2025 11:25 AM GMT -5 Bogotá DC Colombia
     */
    public Map<String, Object> generateRandomSampleTranscriptFull() {
        String baseTranscript = "Meeting Date: November 15, 2024\n" +
            "Participants: Lisa, Jennifer (Real Estate Agent)\n" +
            "Lisa: Hi Jennifer, thanks for hopping on this call. I wanted to walk through where we are with your Follow Up Boss setup and make sure we're on track for your go-live date.\n" +
            "Jennifer: Absolutely! I'm excited to finally get this rolling. I've been so overwhelmed trying to manage my leads manually.\n" +
            "Lisa: Totally understandable. So, based on what we discussed last week, here's what we need to knock out over the next two weeks.\n" +
            "Lisa: First, we need to finalize your lead source tracking. I'll need you to send me a list of all your lead sources—Zillow, Realtor.com, your website, referrals, open houses, etc.—by Friday.\n" +
            "Jennifer Martinez: Got it. I'll pull that together and email it to you by end of week.\n" +
            "Lisa: Perfect. Second, we need to set up your automated drip campaigns. I'm going to need you to review the email templates I sent you last Monday and let me know if you want to make any changes. Can you get me your feedback by Wednesday?\n" +
            "Jennifer Martinez: Yes, I'll review those tonight and send you my edits by Wednesday morning.\n" +
            "Lisa: Awesome. Third, we need to schedule a training session for your assistant, Sarah, so she knows how to use Follow Up Boss for lead entry and task management. Can you have her pick a time on my calendar for next week?\n" +
            "Jennifer Martinez: Absolutely. I'll have her book something today.\n" +
            "Lisa: Great. And lastly, I want to make sure we're integrating your showing software—ShowingTime, right?—with Follow Up Boss so that when a showing gets scheduled, it automatically creates a follow-up task. I'll handle the technical setup, but I'll need your ShowingTime login credentials. Can you send those to me via our secure portal by Thursday?\n" +
            "Jennifer Martinez: Yep, I'll do that tomorrow.\n" +
            "Lisa: Perfect. So just to recap: lead source list by Friday, email template feedback by Wednesday, Sarah books her training for next week, and ShowingTime credentials by Thursday. Does that all sound doable?\n" +
            "Jennifer Martinez: Yes, totally doable. I really appreciate you breaking this down for me.\n" +
            "Lisa: Of course! That's what we're here for. I'll check in with you on Friday to make sure everything's on track, and we should be good to go live by December 1st.\n" +
            "Jennifer Martinez: Sounds great. Thanks, Lisa!\n" +
            "Lisa: Anytime. Talk soon!";
        String prompt = "Generate a random sample meeting transcript with similar structure, participants, and action items as the following transcript. Change names, dates, and details, but keep the format and number of action items. Transcript: " + baseTranscript;
        Instant start = Instant.now();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("messages", java.util.List.of(
                java.util.Map.of("role", "user", "content", prompt)
        ));
        body.put("temperature", 0.7);
        body.put("max_tokens", 500);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, request, Map.class);
        Instant end = Instant.now();
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> bodyMap = response.getBody();
            attachExecutionMetadata(bodyMap, start, end);
            return bodyMap;
        }
        Map<String, Object> error = new HashMap<>();
        error.put("error", "No response from OpenAI or request failed.");
        attachExecutionMetadata(error, start, end);
        return error;
    }

    private void attachExecutionMetadata(Map<String, Object> container, Instant start, Instant end) {
        container.put("executionTimestamp", end.toString());
        container.put("executionDurationMs", Duration.between(start, end).toMillis());
    }
}
