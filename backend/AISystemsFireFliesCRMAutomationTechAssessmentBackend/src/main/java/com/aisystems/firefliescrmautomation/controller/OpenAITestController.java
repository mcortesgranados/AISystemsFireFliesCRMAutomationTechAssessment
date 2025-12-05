package com.aisystems.firefliescrmautomation.controller;

import com.aisystems.firefliescrmautomation.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAI Test Controller for API endpoints
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 5 December 2025 11:15 AM GMT -5 Bogotá DC Colombia
 */

@RestController
@RequestMapping("/api/openai")
@Tag(name = "OpenAI Test Controller", description = "Endpoints for testing OpenAI integration")
public class OpenAITestController {

    @Autowired
    private OpenAIService openAIService;

    /**
     * Test OpenAI completion
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 5 December 2025 11:17 AM GMT -5 Bogotá DC Colombia
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
}
