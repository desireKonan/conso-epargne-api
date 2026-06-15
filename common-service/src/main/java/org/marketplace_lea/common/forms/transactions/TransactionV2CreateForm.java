package org.marketplace_lea.common.forms.transactions;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marketplace_lea.common.entities.transaction.TransactionStatus;
import org.marketplace_lea.common.entities.transaction.TransactionType;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionV2CreateForm {
    @NotBlank(message = "L'ID de la source est obligatoire")
    private String sourceWalletId;

    @NotBlank(message = "L'ID de la destination est obligatoire")
    private String destinationWalletId;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phoneNumber;

    @NotBlank(message = "La référence de paiement est obligatoire")
    private String paymentReference;

    private String objectId;
    private String description;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    private BigDecimal amount;

    @NotBlank(message = "La devise est obligatoire")
    private String currency;

    @Positive(message = "Le montant en jetons doit être positif")
    private BigDecimal coinAmount;

    private Float fees;

    @NotBlank(message = "Le type de transaction est obligatoire")
    private TransactionType transactionType;

    @NotBlank(message = "Le type de transaction est obligatoire")
    private TransactionStatus transactionStatus;
}