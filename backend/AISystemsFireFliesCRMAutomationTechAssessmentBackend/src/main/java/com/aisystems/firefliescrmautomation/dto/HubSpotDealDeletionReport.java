package com.aisystems.firefliescrmautomation.dto;

import java.util.List;

/**
 * Summary of a bulk HubSpot deal deletion operation.
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 11 December 2025 2:16 AM GMT -5 Bogotá DC Colombia
 */
public class HubSpotDealDeletionReport {

    private final int totalFound;
    private final int totalDeleted;
    private final int totalFailed;
    private final List<HubSpotDealDeletionStatus> statuses;
    private final List<String> fetchErrors;

    public HubSpotDealDeletionReport(int totalFound,
                                     int totalDeleted,
                                     int totalFailed,
                                     List<HubSpotDealDeletionStatus> statuses,
                                     List<String> fetchErrors) {
        this.totalFound = totalFound;
        this.totalDeleted = totalDeleted;
        this.totalFailed = totalFailed;
        this.statuses = statuses;
        this.fetchErrors = fetchErrors;
    }

    public int getTotalFound() {
        return totalFound;
    }

    public int getTotalDeleted() {
        return totalDeleted;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public List<HubSpotDealDeletionStatus> getStatuses() {
        return statuses;
    }

    public List<String> getFetchErrors() {
        return fetchErrors;
    }
}
