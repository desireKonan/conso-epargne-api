// PaymentProcessor.java
package org.marketplace_lea.common.client.no_wallet.service;

import org.marketplace_lea.common.client.no_wallet.form.NoWalletForm;
import org.marketplace_lea.common.client.no_wallet.response.NoWalletPaymentResponse;

public interface NoWalletPaymentService {
    NoWalletPaymentResponse processPayment(String transactionId, NoWalletForm noWalletForm);
}