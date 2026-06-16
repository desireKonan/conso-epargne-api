package org.marketplace_lea.common.forms.transactions;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionV2UpdateForm {
    private String objectId;
    private String description;
    
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    private BigDecimal amount;
    
    private BigDecimal coinAmount;
    private Float fees;
}