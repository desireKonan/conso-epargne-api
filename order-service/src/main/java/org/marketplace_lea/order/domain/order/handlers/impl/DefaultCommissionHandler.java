package org.marketplace_lea.order.domain.order.handlers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.entities.transaction.TransactionType;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Type;
import org.marketplace_lea.common.forms.transactions.TransactionV2CreateForm;
import org.marketplace_lea.common.forms.transactions.ValidateTransactionForm;
import org.marketplace_lea.common.forms.transactions.ValidationTransactionStatus;
import org.marketplace_lea.common.services.transaction.TransactionV2Service;
import org.marketplace_lea.common.services.wallet.WalletV2Service;
import org.marketplace_lea.order.domain.order.handlers.CommissionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCommissionHandler implements CommissionHandler {
    private final WalletV2Service walletService;
    private final TransactionV2Service transactionService;

    @Override
    public void addPartnerCommission(String parentId, String phoneNumber, float amount) {
        try {
            WalletV2Entity wallet = walletService.getByAccountIdAndType(parentId, WalletV2Type.PERSONAL);
            var transaction = transactionService.createTransaction(TransactionV2CreateForm.builder()
                    .sourceWalletId(null)
                    .phoneNumber(phoneNumber)
                    .destinationWalletId(wallet.getId())
                    .amount(BigDecimal.valueOf(amount))
                    .transactionType(TransactionType.PARTNER_BONUS)
                    .build()
            );
            wallet.addToPartnerBonusAmount(amount);
            transactionService.validateTransaction(new ValidateTransactionForm(transaction.getId(), ValidationTransactionStatus.VALIDATE, ""));

            log.info("[CommissionService.addPartnerCommission] Added {} commission to parent {} from child (phoneNumber) {}",
                    amount, parentId, phoneNumber);
        } catch (Exception e) {
            log.error("[CommissionService.addPartnerCommission] Error: {}", e.getMessage(), e);
            throw new ConsoEpargneException("Failed to add partner commission !", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void addCommission(String accountId, float amount) {
        try {
            WalletV2Entity wallet = walletService.getByAccountIdAndType(accountId, WalletV2Type.PERSONAL);
            var transaction = transactionService.createTransaction(TransactionV2CreateForm.builder()
                    .sourceWalletId(null)
                    .destinationWalletId(wallet.getId())
                    .amount(BigDecimal.valueOf(amount))
                    .transactionType(TransactionType.COMMISSION)
                    .build()
            );
            wallet.addToBalance(BigDecimal.valueOf(amount));
            transactionService.validateTransaction(new ValidateTransactionForm(transaction.getId(), ValidationTransactionStatus.VALIDATE, ""));

            log.info("[CommissionService.addCommission] Added {} commission to account {}",
                    amount, accountId);
        } catch (Exception e) {
            log.error("[CommissionService.addCommission] Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add commission", e);
        }
    }
}