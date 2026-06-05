package org.marketplace_lea.prometheus.domain.localization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {
    private Long id;
    private String label;
    private String code;
    private String callingCode;
    private Integer phoneNumberLength;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime disabledAt;
}