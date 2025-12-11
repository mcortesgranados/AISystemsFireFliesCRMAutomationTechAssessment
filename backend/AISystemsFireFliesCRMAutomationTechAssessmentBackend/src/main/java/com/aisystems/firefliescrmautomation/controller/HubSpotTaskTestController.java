package com.aisystems.firefliescrmautomation.controller;

import com.aisystems.firefliescrmautomation.dto.HubSpotDealDeletionReport;
import com.aisystems.firefliescrmautomation.service.HubSpotTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for testing automatic task creation in HubSpot.
 * <p>
 * Exposes an endpoint to create a task in HubSpot with sample data.
 * </p>
 *
 * @author Manuela Cortés Granados
 * @since 9 December 2025
 */
@RestController
@RequestMapping("/api/hubspot")
@Tag(name = "HubSpot Task Test Controller", description = "Test endpoint for creating tasks in HubSpot")
public class HubSpotTaskTestController {

    @Autowired
    private HubSpotTaskService hubSpotTaskService;

    /**
     * Creates a task in HubSpot with the provided details.
     *
     * @param description The task description.
     * @param dueDate     The due date (ISO 8601 format).
     * @param priority    The priority (high, medium, low).
     * @param assignee    The assignee's email (optional).
     * @return The HubSpot API response as a string.
     */
    @Operation(summary = "Create a task in HubSpot", description = "Creates a new task in HubSpot with the given details.")
    @PostMapping("/create-task")
    public String createTask(
            @RequestParam String description,
            @RequestParam String dueDate,
            @RequestParam String priority,
            @RequestParam(required = false) String assignee) {
        return hubSpotTaskService.createTask(description, dueDate, priority, assignee);
    }

    /**
     * Delete all HubSpot deals
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 11 December 2025 02:07 AM GMT -5 Bogotá DC Colombia
     */

    @Operation(
        summary = "(deleteAllHubSpotDeals) Delete all HubSpot deals",
        description = "(deleteAllHubSpotDeals) Deletes every deal in HubSpot using pagination and logs the result."
    )
    @DeleteMapping("/delete-all-deals")
    public HubSpotDealDeletionReport deleteAllHubSpotDeals() {
        return hubSpotTaskService.deleteAllHubSpotDeals();
    }



}
