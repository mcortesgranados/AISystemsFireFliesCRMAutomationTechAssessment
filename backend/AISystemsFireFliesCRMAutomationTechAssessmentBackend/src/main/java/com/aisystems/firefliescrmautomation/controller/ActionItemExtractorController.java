package com.aisystems.firefliescrmautomation.controller;

import com.aisystems.firefliescrmautomation.service.ActionItemExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for extracting action items from meeting transcripts using AI.
 * <p>
 * Accepts a transcript via POST and returns a list of structured action items.
 * </p>
 *
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 6 December 2025 GMT -5 Bogotá DC Colombia
 */
@RestController
@RequestMapping("/api/action-items")
@Tag(name = "Action Item Extractor", description = "Extracts action items from meeting transcripts using AI")
public class ActionItemExtractorController {

    @Autowired
    private ActionItemExtractorService actionItemExtractorService;

    /**
     * Extracts action items from the provided transcript.
     *
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 6 December 2025 GMT -5 Bogotá DC Colombia
     * @param transcript The meeting transcript text.
     * @return List of action items as JSON.
     */
    @Operation(summary = "Extract action items from transcript", description = "Returns a list of action items with smart features parsed from the transcript.")
    @PostMapping("/extract")
    public List<Map<String, Object>> extractActionItems(@RequestBody String transcript) {
        return actionItemExtractorService.extractActionItems(transcript);
    }
}
