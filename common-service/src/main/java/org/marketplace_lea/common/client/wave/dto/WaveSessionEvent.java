package org.marketplace_lea.common.client.wave.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaveSessionEvent {
    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("data")
    private WaveCheckout data;

    public boolean isExpired() {
        return "checkout.session.expired".equals(type);
    }

    public boolean isSuccess() {
        return "checkout.session.completed".equals(type);
    }

    public boolean isFailed() {
        return "checkout.session.payment_failed".equals(type);
    }


    public String rejectionMessage() {
        if(isFailed()) {
            return "Wave - Paiement échoué !";
        } else if(isSuccess()) {
            return "";
        } else {
            return "Wave - Transaction expirée !";
        }
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}