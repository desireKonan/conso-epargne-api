package org.marketplace_lea.order.domain.order.handlers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Type;
import org.marketplace_lea.common.forms.transactions.TransactionV2CreateForm;
import org.marketplace_lea.common.forms.transactions.ValidateTransactionForm;
import org.marketplace_lea.common.forms.transactions.ValidationTransactionStatus;
import org.marketplace_lea.common.services.transaction.TransactionV2Service;
import org.marketplace_lea.common.services.wallet.WalletV2Service;
import org.marketplace_lea.order.domain.order.handlers.PersonalSavingHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.marketplace_lea.common.entities.transaction.TransactionType.PERSONAL_SAVING;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPersonalSavingHandler implements PersonalSavingHandler {
    private final WalletV2Service walletService;
    private final TransactionV2Service transactionService;

    @Override
    @Transactional
    public void addToPersonalSaving(String accountId, float amount) {
        try {
            WalletV2Entity wallet = walletService.getByAccountIdAndType(accountId, WalletV2Type.PERSONAL);
            var transaction = transactionService.createTransaction(TransactionV2CreateForm.builder()
                    .sourceWalletId(null)
                    .destinationWalletId(wallet.getId())
                    .amount(BigDecimal.valueOf(amount))
                    .transactionType(PERSONAL_SAVING)
                    .build()
            );
            wallet.addToPersonalSaving(amount);
            transactionService.validateTransaction(new ValidateTransactionForm(transaction.getId(), ValidationTransactionStatus.VALIDATE, ""));
            log.info("[PersonalSavingService.addToPersonalSaving] Added {} to personal saving for account {}",
                    amount, accountId);
        } catch (Exception e) {
            log.error("[PersonalSavingService.addToPersonalSaving] Error: {}", e.getMessage(), e);
            throw new ConsoEpargneException("Failed to add personal saving !", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}