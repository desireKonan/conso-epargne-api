package org.marketplace_lea.common.services.transaction;

import org.marketplace_lea.common.dtos.transactions.TransactionDTO;
import org.marketplace_lea.common.forms.transactions.TransactionV2CreateForm;
import org.marketplace_lea.common.forms.transactions.TransactionV2SearchCriteria;
import org.marketplace_lea.common.forms.transactions.ValidateTransactionForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
public interface TransactionV2Service {
    TransactionDTO createTransaction(TransactionV2CreateForm form);
    
    TransactionDTO validateTransaction(ValidateTransactionForm form);

    Optional<TransactionDTO> findTransactionById(String id);

    Page<TransactionDTO> searchTransactions(TransactionV2SearchCriteria criteria, Pageable pageable);
    
    void deleteTransaction(String id);
}