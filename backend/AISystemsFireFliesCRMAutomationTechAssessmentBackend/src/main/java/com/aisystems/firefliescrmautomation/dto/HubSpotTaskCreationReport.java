package com.aisystems.firefliescrmautomation.dto;

/**
 * Report for the creation of tasks in HubSpot.
 * Contains details about the task creation process, including success and error counts.
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 9 December 2025 7:57 AM GMT -5 Bogotá DC Colombia
 */

import java.util.List;
import java.util.Map;

public class HubSpotTaskCreationReport {
    private int totalRequested;
    private int totalSucceeded;
    private int totalFailed;
    private List<String> responses;
    private List<String> errors;
    private List<Map<String, Object>> actionItems;
    private List<Map<String, Object>> taskResults;

    public HubSpotTaskCreationReport(int totalRequested,
                                     int totalSucceeded,
                                     int totalFailed,
                                     List<String> responses,
                                     List<String> errors,
                                     List<Map<String, Object>> actionItems,
                                     List<Map<String, Object>> taskResults) {
        this.totalRequested = totalRequested;
        this.totalSucceeded = totalSucceeded;
        this.totalFailed = totalFailed;
        this.responses = responses;
        this.errors = errors;
        this.actionItems = actionItems;
        this.taskResults = taskResults;
    }

    public int getTotalRequested() { return totalRequested; }
    public int getTotalSucceeded() { return totalSucceeded; }
    public int getTotalFailed() { return totalFailed; }
    public List<String> getResponses() { return responses; }
    public List<String> getErrors() { return errors; }
    public List<Map<String, Object>> getActionItems() { return actionItems; }
    public List<Map<String, Object>> getTaskResults() { return taskResults; }

    public void setTotalRequested(int totalRequested) { this.totalRequested = totalRequested; }
    public void setTotalSucceeded(int totalSucceeded) { this.totalSucceeded = totalSucceeded; }
    public void setTotalFailed(int totalFailed) { this.totalFailed = totalFailed; }
    public void setResponses(List<String> responses) { this.responses = responses; }
    public void setErrors(List<String> errors) { this.errors = errors; }
    public void setActionItems(List<Map<String, Object>> actionItems) { this.actionItems = actionItems; }
    public void setTaskResults(List<Map<String, Object>> taskResults) { this.taskResults = taskResults; }
}
