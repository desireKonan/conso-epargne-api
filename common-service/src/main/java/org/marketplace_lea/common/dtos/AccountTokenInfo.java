package org.marketplace_lea.common.dtos;

public record AccountTokenInfo(
        String id,
        String accountTypeId,        // Identifiant du type de compte (ex: "CUSTOMER", "PARTNER", "SYSTEM")
        String affiliationCode,
        String countryCode,
        String login,
        String notificationToken,
        Boolean blocked
) {
}