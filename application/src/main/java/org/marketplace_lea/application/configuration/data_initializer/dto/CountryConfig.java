package org.marketplace_lea.application.configuration.data_initializer.dto;

public record CountryConfig(
    Long id,
    String callingCode,
    String code,
    boolean enabled,
    String label,
    Integer phoneNumberLength
) {}