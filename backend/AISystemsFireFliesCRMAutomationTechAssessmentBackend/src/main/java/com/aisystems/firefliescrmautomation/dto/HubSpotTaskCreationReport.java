package com.aisystems.firefliescrmautomation.dto;

/**
 * Report for the creation of tasks in HubSpot.
 * Contains details about the task creation process, including success and error counts.
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 9 December 2025 7:57 AM GMT -5 Bogotá DC Colombia
 */

import java.util.List;

public class HubSpotTaskCreationReport {
    private int totalRequested;
    private int totalSucceeded;
    private int totalFailed;
    private List<String> responses;
    private List<String> errors;

    public HubSpotTaskCreationReport(int totalRequested, int totalSucceeded, int totalFailed, List<String> responses, List<String> errors) {
        this.totalRequested = totalRequested;
        this.totalSucceeded = totalSucceeded;
        this.totalFailed = totalFailed;
        this.responses = responses;
        this.errors = errors;
    }

    public int getTotalRequested() { return totalRequested; }
    public int getTotalSucceeded() { return totalSucceeded; }
    public int getTotalFailed() { return totalFailed; }
    public List<String> getResponses() { return responses; }
    public List<String> getErrors() { return errors; }

    public void setTotalRequested(int totalRequested) { this.totalRequested = totalRequested; }
    public void setTotalSucceeded(int totalSucceeded) { this.totalSucceeded = totalSucceeded; }
    public void setTotalFailed(int totalFailed) { this.totalFailed = totalFailed; }
    public void setResponses(List<String> responses) { this.responses = responses; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
