package org.marketplace_lea.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link Country}
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto implements Serializable {
    private Long id;
    private String label;
    private String code;
    private String callingCode;
    private String phoneNumberLength;
}