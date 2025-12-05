package com.aisystems.firefliescrmautomation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Test Controller for API endpoints
 * @author Manuela Cortés Granados
 * @since 5 December 2025 4:01 AM GMT -5 Bogotá DC Colombia
 */

@RestController
@Tag(name = "Test API", description = "API for string operations")
public class TestController {

    @Operation(summary = "Concatenate two strings", description = "Returns the concatenation of str1 and str2.")
    @GetMapping("/api/concat")
    public String concatStrings(
            @Parameter(description = "First string") @RequestParam("str1") String str1,
            @Parameter(description = "Second string") @RequestParam("str2") String str2) {
        return str1 + str2;
    }
}
