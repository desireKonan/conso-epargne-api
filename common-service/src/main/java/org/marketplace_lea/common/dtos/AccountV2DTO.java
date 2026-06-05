package org.marketplace_lea.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountV2DTO {
    private String id;
    private String accountTypeId;        // Identifiant du type de compte (ex: "CUSTOMER", "PARTNER", "SYSTEM")
    private String affiliationCode;
    private String countryCode;
    private String login;
    private String notificationToken;
    private Boolean blocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}