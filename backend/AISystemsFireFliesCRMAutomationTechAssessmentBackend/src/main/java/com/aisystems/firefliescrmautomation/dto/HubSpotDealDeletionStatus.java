package com.aisystems.firefliescrmautomation.dto;

/**
 * Represents the outcome of an attempt to delete a single HubSpot deal.
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 11 December 2025 2:17 AM GMT -5 Bogotá DC Colombia
 */
public class HubSpotDealDeletionStatus {

    private final String dealId;
    private final boolean deleted;
    private final String message;

    public HubSpotDealDeletionStatus(String dealId, boolean deleted, String message) {
        this.dealId = dealId;
        this.deleted = deleted;
        this.message = message;
    }

    public String getDealId() {
        return dealId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getMessage() {
        return message;
    }
}
