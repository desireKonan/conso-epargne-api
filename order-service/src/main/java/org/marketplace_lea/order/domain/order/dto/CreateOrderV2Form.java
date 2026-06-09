package org.marketplace_lea.order.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOrderV2Form(
    @NotBlank(message = "Customer ID is required")
    String customerId,
    
    String voucherId,
    
    @NotBlank(message = "Address is required")
    String address,
    
    @NotNull(message = "Latitude is required")
    BigDecimal latitude,
    
    @NotNull(message = "Longitude is required")
    BigDecimal longitude,
    
    @NotNull(message = "Total amount is required")
    float totalAmount,
    
    float fees,
    
    float onlineFees,
    
    @NotNull(message = "Delivery fees is required")
    float deliveryFees,
    
    boolean hasOnlinePayment,
    
    @NotBlank(message = "Provider is required")
    String provider
) {

    public double determineLatitude() {
        return this.latitude != null ? this.latitude.doubleValue() : 0.0;
    }

    public double determineLongitude() {
        return this.longitude != null ? this.longitude.doubleValue() : 0.0;
    }
}
