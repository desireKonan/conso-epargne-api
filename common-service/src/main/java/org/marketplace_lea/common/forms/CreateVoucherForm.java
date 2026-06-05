package org.marketplace_lea.common.forms;

import org.marketplace_lea.common.common.constants.ConsoEpargneConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateVoucherForm(
                @NotNull(message = "Le montant ne doit pas être nul") @Min(value = ((int) ConsoEpargneConstants.MIN_VOUCHER_AMOUNT), message = "Le montant doit être supérieur à "
                                + ConsoEpargneConstants.MIN_VOUCHER_AMOUNT + " Pts") float amount) {
}
