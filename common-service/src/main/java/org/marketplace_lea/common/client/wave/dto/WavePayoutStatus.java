package org.marketplace_lea.common.client.wave.dto;

public enum WavePayoutStatus {
    SUCCESS("succeeded"),
    FAILED("failed"),
    PENDING("processing");

    private final String label;

    WavePayoutStatus(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}
