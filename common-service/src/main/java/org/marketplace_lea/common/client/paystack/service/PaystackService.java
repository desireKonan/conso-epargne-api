package org.marketplace_lea.common.client.paystack.service;

import org.marketplace_lea.common.client.paystack.form.CustomerForm;
import org.marketplace_lea.common.client.paystack.form.InitializeTransactionForm;
import org.marketplace_lea.common.client.paystack.response.CustomerResponse;
import org.marketplace_lea.common.client.paystack.response.InitializeTransactionResponse;
import org.marketplace_lea.common.client.paystack.response.PaystackResponse;
import org.marketplace_lea.common.client.paystack.response.TransactionListResponse;
import org.marketplace_lea.common.client.paystack.response.TransactionVerifyResponse;

public interface PaystackService {
    PaystackResponse<InitializeTransactionResponse> initTransaction(InitializeTransactionForm form, String correlationId);

    PaystackResponse<TransactionVerifyResponse> verifyTransaction(String reference, String correlationId);

    PaystackResponse<TransactionListResponse> getTransactions(int page, int perPage, String correlationId);

    PaystackResponse<TransactionVerifyResponse> findTransaction(String transactionId, String correlationId);

    PaystackResponse<CustomerResponse> createCustomer(CustomerForm form, String correlationId);

    PaystackResponse<CustomerResponse> findCustomer(String email, String correlationId);
}
