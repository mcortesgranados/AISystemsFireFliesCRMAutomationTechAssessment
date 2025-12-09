package com.aisystems.firefliescrmautomation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        String extractionPrompt = "Extract all action items from the following meeting transcript. " +
            "For each action item, provide: description, priority (HIGH/MEDIUM/LOW), deadline (if mentioned), and assignee (if mentioned). " +
            "Return the result as a JSON array of objects with keys: description, priority, deadline, assignee. " +
            "Transcript: " + transcript;

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo-instruct");
        body.put("prompt", extractionPrompt);
        body.put("max_tokens", 500);

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object choices = response.getBody().get("choices");
                if (choices instanceof java.util.List && !((java.util.List<?>) choices).isEmpty()) {
                    Object first = ((java.util.List<?>) choices).get(0);
                    if (first instanceof Map) {
                        Object text = ((Map<?, ?>) first).get("text");
                        if (text != null) {
                            String json = text.toString().trim();
                            // Try to parse the JSON array
                            try {
                                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                                actionItems = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                            } catch (Exception parseEx) {
                                // If parsing fails, return a single item with raw text
                                Map<String, Object> raw = new HashMap<>();
                                raw.put("raw_output", json);
                                actionItems.add(raw);
                            }
                        }
                    }
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
     * Generates a random sample meeting transcript with similar structure and action items
     * to the provided Fireflies transcript, using ChatGPT.
     * @return A random sample transcript as a String.
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
        return getCompletion(prompt);
    }

    /**
     * Returns the full OpenAI API response for a random sample meeting transcript.
     * @return Map containing the full OpenAI API response.
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
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo-instruct");
        body.put("prompt", prompt);
        body.put("max_tokens", 500);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, request, Map.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        Map<String, Object> error = new HashMap<>();
        error.put("error", "No response from OpenAI or request failed.");
        return error;
    }
}
