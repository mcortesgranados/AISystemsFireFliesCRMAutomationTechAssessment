package com.aisystems.firefliescrmautomation.controller;

import com.aisystems.firefliescrmautomation.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

/**
 * OpenAI Test Controller for API endpoints
 * @author Manuela CortAcs Granados (manuelacortesgranados@gmail.com)
 * @since 5 December 2025 11:15 AM GMT -5 BogotA­ DC Colombia
 */

@RestController
@RequestMapping("/api/openai")
@Tag(name = "OpenAI Test Controller", description = "Endpoints for testing OpenAI integration")
public class OpenAITestController {

    @Autowired
    private OpenAIService openAIService;

    /**
     * Test OpenAI completion
     * @author Manuela CortAcs Granados (manuelacortesgranados@gmail.com)
     * @since 5 December 2025 11:17 AM GMT -5 BogotA­ DC Colombia
     * @param prompt
     * @param arg0
     * @return
     */

    @Operation(summary = "Get OpenAI completion", description = "Returns a completion from OpenAI for the given prompt.")
    @GetMapping("/test")
    public String testOpenAI(
            @Parameter(description = "Prompt to send to OpenAI", example = "Hello from AI Systems")
            @RequestParam(value = "prompt", required = false) String prompt,
            @Parameter(description = "Alternative prompt parameter (e.g., arg0)", example = "Hola")
            @RequestParam(value = "arg0", required = false) String arg0) {
        String effective = (prompt != null && !prompt.isBlank()) ? prompt : arg0;
        if (effective == null || effective.isBlank()) {
            effective = "Hello from AI Systems";
        }
        return openAIService.getCompletion(effective);
    }

    /**
     * Extract action items from transcript
     * @author Manuela CortAcs Granados (manuelacortesgranados@gmail.com)
     * @since 5 December 2025 11:20 AM GMT -5 BogotA­ DC Colombia
     * @param transcript
     * @return
     * @author Manuela CortAcs Granados (manuelacortesgranados@gmail.com)
     * @since 9 December 2025 08:04 AM GMT -5 BogotA­ DC Colombia
     */

    @Operation(
            summary = "Extract action items from transcript",
            description = "Uses OpenAI to extract action items with smart features from a Fireflies meeting transcript.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Meeting transcript text",
                    required = true,
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(
                                    name = "Sample transcript",
                                    value = "Meeting Date: November 15, 2024\\nParticipants: Lisa, Jennifer (Real Estate Agent)\\nLisa: Hi Jennifer, thanks for hopping on this call. I wanted to walk through where we are with your Follow Up Boss setup and make sure we're on track for your go-live date.\\nJennifer: Absolutely! I'm excited to finally get this rolling. I've been so overwhelmed trying to manage my leads manually.\\nLisa: Totally understandable. So, based on what we discussed last week, here's what we need to knock out over the next two weeks.\\nLisa: First, we need to finalize your lead source tracking. I'll need you to send me a list of all your lead sources - Zillow, Realtor.com, your website, referrals, open houses, etc. - by Friday.\\nJennifer Martinez: Got it. I'll pull that together and email it to you by end of week.\\nLisa: Perfect. Second, we need to set up your automated drip campaigns. I'm going to need you to review the email templates I sent you last Monday and let me know if you want to make any changes. Can you get me your feedback by Wednesday?\\nJennifer Martinez: Yes, I'll review those tonight and send you my edits by Wednesday morning.\\nLisa: Awesome. Third, we need to schedule a training session for your assistant, Sarah, so she knows how to use Follow Up Boss for lead entry and task management. Can you have her pick a time on my calendar for next week?\\nJennifer Martinez: Absolutely. I'll have her book something today.\\nLisa: Great. And lastly, I want to make sure we're integrating your showing software - ShowingTime, right? - with Follow Up Boss so that when a showing gets scheduled, it automatically creates a follow-up task. I'll handle the technical setup, but I'll need your ShowingTime login credentials. Can you send those to me via our secure portal by Thursday?\\nJennifer Martinez: Yep, I'll do that tomorrow.\\nLisa: Perfect. So just to recap: lead source list by Friday, email template feedback by Wednesday, Sarah books her training for next week, and ShowingTime credentials by Thursday. Does that all sound doable?\\nJennifer Martinez: Yes, totally doable. I really appreciate you breaking this down for me.\\nLisa: Of course! That's what we're here for. I'll check in with you on Friday to make sure everything's on track, and we should be good to go live by December 1st.\\nJennifer Martinez: Sounds great. Thanks, Lisa!\\nLisa: Anytime. Talk soon!"
                            )
                    )
            )
    )
    @PostMapping("/extract-action-items")
    public List<Map<String, Object>> extractActionItems(@RequestBody String transcript) {
        return openAIService.extractActionItemsFromTranscript(transcript);
    }

    /**
     * Generate random sample transcript
     * @author Manuela CortAcs Granados (manuelacortesgranados@gmail.com)
     * @since 5 December 2025 11:25 AM GMT -5 BogotA­ DC Colombia
     * @return
     */

    @Operation(summary = "Generate random sample transcript", description = "Uses ChatGPT to generate a random sample meeting transcript with similar structure and action items as the provided Fireflies transcript.")
    @GetMapping("/generate-sample-transcript")
    public String generateSampleTranscript() {
        return openAIService.generateRandomSampleTranscript();
    }

    /**
     * Generate random sample transcript (full response)
     * @author Manuela CortAcs Granados (manuelacortesgranados@gmail.com)
     * @since 5 December 2025 11:30 AM GMT -5 BogotA­ DC Colombia
     * @return
     */

    @Operation(summary = "Generate random sample transcript (full response)", description = "Returns the full OpenAI API response for a random sample meeting transcript.")
    @GetMapping("/generate-sample-transcript-full")
    public Map<String, Object> generateSampleTranscriptFull() {
        return openAIService.generateRandomSampleTranscriptFull();
    }
}
