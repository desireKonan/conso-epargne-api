package org.marketplace_lea.common.client.sotels.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SotelsApiStatus {
    private String yourReference;
    private float amount;
    private String status;
    private String description;
    @JsonProperty("Message")
    private String message;

    public boolean isSucceed() {
        return status != null && status.equals("01");
    }

    public boolean isCanceled() {
        return status != null && status.equals("02");
    }

    public boolean isPending() {
        return status != null && status.equals("03");
    }

    public boolean isFailed() {
        return status != null && status.equals("04");
    }
}
