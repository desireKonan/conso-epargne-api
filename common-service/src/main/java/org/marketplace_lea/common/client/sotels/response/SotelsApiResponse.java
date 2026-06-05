package org.marketplace_lea.common.client.sotels.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SotelsApiResponse {
    private boolean success;
    private String message;
    private String yourReference;
    private String ourReference;
}
