package org.marketplace_lea.common.forms;

import jakarta.validation.constraints.NotNull;

public record OrderForm(
        @NotNull(message = "L'addresse de livraison ne doit pas être nulle !")
        Long idAddress,

        @NotNull(message = "Veuillez entrer la methode de payment !")
        Long idPaymentMethod,

        String idVoucher,

        @NotNull(message = "Veuillez entrer une date de livraison !")
        String deliveryDate,

        float latitude,

        float longitude
) {


    public boolean isNotNull() {
        return this.idVoucher != null && !this.idVoucher.isEmpty();
    }
}
