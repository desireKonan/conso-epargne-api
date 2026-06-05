package org.marketplace_lea.common.client.paystack.service.impl;

import org.marketplace_lea.common.client.paystack.PaystackClient;
import org.marketplace_lea.common.client.paystack.exception.PaystackException;
import org.marketplace_lea.common.client.paystack.form.CustomerForm;
import org.marketplace_lea.common.client.paystack.form.InitializeTransactionForm;
import org.marketplace_lea.common.client.paystack.response.CustomerResponse;
import org.marketplace_lea.common.client.paystack.response.InitializeTransactionResponse;
import org.marketplace_lea.common.client.paystack.response.PaystackResponse;
import org.marketplace_lea.common.client.paystack.response.TransactionListResponse;
import org.marketplace_lea.common.client.paystack.response.TransactionVerifyResponse;
import org.marketplace_lea.common.client.paystack.service.PaystackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPaystackService implements PaystackService {
    private final PaystackClient paystackClient;

    @Override
    public PaystackResponse<InitializeTransactionResponse> initTransaction(InitializeTransactionForm form, String correlationId) {
        log.info("[DefaultPaystackService.initTransaction][correlationId={}] Initialize payement for email {}", correlationId, form.email());
        try {
            var paystackResponse = paystackClient.initializeTransaction(form);
            log.info("[DefaultPaystackService.initTransaction][correlationId={}] Status={} Message={} - Payment successful='{}'", correlationId, paystackResponse.status(), paystackResponse.message(), paystackResponse.data());
            return paystackResponse;
        } catch (PaystackException e) {
            log.error("[DefaultPaystackService.initTransaction][correlationId={}] Error while initializing payement: {}", correlationId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public PaystackResponse<TransactionVerifyResponse> verifyTransaction(String reference, String correlationId) {
        log.info("[DefaultPaystackService.verifyTransaction][correlationId={}] Verify Transaction for email {}", correlationId, reference);
        try {
            var paystackResponse = paystackClient.verifyTransaction(reference);
            log.info("[DefaultPaystackService.verifyTransaction][correlationId={}] Status={} Message={} - Verify transaction='{}'", correlationId, paystackResponse.status(), paystackResponse.message(), paystackResponse.data());
            return paystackResponse;
        } catch (PaystackException e) {
            log.error("[DefaultPaystackService.verifyTransaction][correlationId={}] Error while verifying transaction: {}", correlationId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public PaystackResponse<TransactionListResponse> getTransactions(int page, int perPage, String correlationId) {
        log.info("[DefaultPaystackService.getTransactions][correlationId={}] Get transactions {}", correlationId, page);
        try {
            var paystackResponse = paystackClient.listTransactions(page, perPage);
            log.info("[DefaultPaystackService.getTransactions][correlationId={}] Status={} Message={} - Get transactions='{}'", correlationId, paystackResponse.status(), paystackResponse.message(), paystackResponse.data());
            return paystackResponse;
        } catch (PaystackException e) {
            log.error("[DefaultPaystackService.getTransactions][correlationId={}] Error while getting transactions: {}", correlationId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public PaystackResponse<TransactionVerifyResponse> findTransaction(String transactionId, String correlationId) {
        return null;
    }

    @Override
    public PaystackResponse<CustomerResponse> createCustomer(CustomerForm form, String correlationId) {
        return null;
    }

    @Override
    public PaystackResponse<CustomerResponse> findCustomer(String email, String correlationId) {
        return null;
    }
}
