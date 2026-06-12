package org.marketplace_lea.order.domain.order.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class CreateOrderV2Form {
    @NotBlank(message = "Customer ID is required")
    private String customerId;

    private String voucherId;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Latitude is required")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required")
    private BigDecimal longitude;

    @NotNull(message = "Total amount is required")
    private float totalAmount;

    private float fees;

    private float onlineFees;

    @NotNull(message = "Delivery fees is required")
    private float deliveryFees;

    private boolean hasOnlinePayment;

    @NotBlank(message = "Provider is required")
    private String provider;

    public double determineLatitude() {
        return this.latitude != null ? this.latitude.doubleValue() : 0.0;
    }

    public double determineLongitude() {
        return this.longitude != null ? this.longitude.doubleValue() : 0.0;
    }
}
