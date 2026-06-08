package org.marketplace_lea.order.domain.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.marketplace_lea.order.common.entities.inventory.ProductType;

import java.math.BigDecimal;

public record UpdateProductV2Form(
    @NotBlank(message = "L'ID de l'étagère est obligatoire") String shelfId,
    @NotBlank(message = "Le label est obligatoire") String label,
    @NotBlank(message = "La description est obligatoire") String description,
    double weight,
    @NotBlank(message = "La marque est obligatoire") String brand,
    @NotNull(message = "Le prix d'achat est obligatoire") BigDecimal purchasePrice,
    @NotNull(message = "La marge Conso Epargne est obligatoire") BigDecimal consoEpargneMargin,
    @NotNull(message = "La commission compte est obligatoire") BigDecimal comissionAccountAmount,
    @NotNull(message = "Le prix de vente est obligatoire") BigDecimal price,
    @NotNull(message = "Le montant d'épargne est obligatoire") BigDecimal savingAmount,
    @NotNull(message = "Le type de produit est obligatoire") ProductType type,
    boolean soldOut,
    boolean activeCashback
) {}
