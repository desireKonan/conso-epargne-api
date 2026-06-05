package org.marketplace_lea.common.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectRecipientDTO {
    private Long id;
    private String contact;
    private String fullName;
}
