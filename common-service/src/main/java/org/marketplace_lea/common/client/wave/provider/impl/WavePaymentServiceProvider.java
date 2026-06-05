package org.marketplace_lea.common.client.wave.provider.impl;

import org.marketplace_lea.common.client.wave.WaveClient;
import org.marketplace_lea.common.client.wave.dto.WaveCheckout;
import org.marketplace_lea.common.client.wave.dto.WavePayoutResponse;
import org.marketplace_lea.common.client.wave.form.WaveCheckoutForm;
import org.marketplace_lea.common.client.wave.form.WavePayoutForm;
import org.marketplace_lea.common.client.wave.provider.WavePaymentProvider;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WavePaymentServiceProvider implements WavePaymentProvider {
    private final WaveClient waveClient;

    @Override
    public WaveCheckout processPayment(TransactionV2Entity transaction) {
        try {
            var form = new WaveCheckoutForm(String.valueOf(transaction.correctAmount()), transaction.getId());
            WaveCheckout checkout = waveClient.createSession(form);
            log.info("Wave checkout session with id: '{}' created with success.", checkout.getId());
            return checkout;
        } catch (FeignException.FeignClientException ex) {
            log.error("Wave error while processing payment: {}", ex.getMessage(), ex);
            throw new ConsoEpargneException("Error while processing payment !", HttpStatus.valueOf(ex.status()));
        }
    }

    @Override
    public boolean verifyPayment(String transactionId) {
        try {
            WaveCheckout session = waveClient.getSessionById(transactionId);
            log.info("Wave session : {}", session);
            return "completed".equals(session.getPaymentStatus());
        } catch (FeignException.FeignClientException ex) {
            log.error("Wave error while verifying payment: {}", ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public WavePayoutResponse payout(WavePayoutForm form, String correlationId) {
        try {
            WavePayoutResponse checkout = waveClient.payout(correlationId, form);
            log.info("Wave payout session with id: '{}' created with success.", checkout.id());
            return checkout;
        } catch (FeignException.FeignClientException ex) {
            log.error("Wave error while processing payout: {}", ex.getMessage(), ex);
            throw new ConsoEpargneException("Error while processing payout !", HttpStatus.valueOf(ex.status()));
        }
    }
}
