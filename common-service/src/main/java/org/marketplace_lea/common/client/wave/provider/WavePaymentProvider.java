package org.marketplace_lea.common.client.wave.provider;

import org.marketplace_lea.common.client.wave.dto.WaveCheckout;
import org.marketplace_lea.common.client.wave.dto.WavePayoutResponse;
import org.marketplace_lea.common.client.wave.form.WavePayoutForm;
import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;

public interface WavePaymentProvider {
    WaveCheckout processPayment(TransactionV2Entity transaction);

    boolean verifyPayment(String transactionId);

    WavePayoutResponse payout(WavePayoutForm form, String correlationId);
}
