package org.marketplace_lea.common.forms.transactions;

public record ValidateTransactionForm(
        String transactionId,
        ValidationTransactionStatus status,
        String rejectionMessage
) {
}
