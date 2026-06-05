package org.marketplace_lea.common.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemDTO {
    @NotBlank(message = "Veuillez enter un produit.")
    private String idProduct;

    @Min(value = 1, message = "Veuillez entrer une quantité supérieure ou égale à un(1).")
    private int quantity;
}
