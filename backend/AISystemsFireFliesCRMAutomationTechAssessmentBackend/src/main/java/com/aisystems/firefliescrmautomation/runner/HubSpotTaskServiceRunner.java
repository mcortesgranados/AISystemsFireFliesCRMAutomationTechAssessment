package com.aisystems.firefliescrmautomation.runner;

import com.aisystems.firefliescrmautomation.service.HubSpotTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner to test HubSpotTaskService on application startup.
 * <p>
 * Automatically creates a sample task in HubSpot and prints the response.
 * </p>
 *
 * @author Manuela Cortés Granados
 * @since 9 December 2025
 */
@Component
public class HubSpotTaskServiceRunner implements CommandLineRunner {

    @Autowired
    private HubSpotTaskService hubSpotTaskService;

    @Override
    public void run(String... args) {
        //runSampleTaskCreation();
    }

    // Extracted for clarity and potential reuse
    private void runSampleTaskCreation() {
        String description = "Follow up with client";
        String dueDate = "2025-12-10";
        String priority = "high";
        String assignee = ""; // Optional: provide HubSpot owner ID if available
        System.out.println("Testing HubSpotTaskService.createTask...");
        String response = hubSpotTaskService.createTask(description, dueDate, priority, assignee);
        System.out.println("HubSpot API response: " + response);
    }
}
