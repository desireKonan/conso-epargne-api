package org.marketplace_lea.common.client.paystack.form;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data
public class PlanForm {
    @NotBlank
    private String name;
    
    @NotBlank
    private String interval; // daily, weekly, monthly, quarterly, annually
    
    @Min(100)
    private Integer amount;
    
    private Boolean send_invoices = true;
    private Boolean send_sms = true;
    private String currency = "NGN";
    private Integer invoice_limit = 0;
}
