package org.marketplace_lea.common.services.transaction.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.dtos.transactions.TransactionDTO;
import org.marketplace_lea.common.entities.transaction.TransactionStatus;
import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import org.marketplace_lea.common.forms.transactions.TransactionV2CreateForm;
import org.marketplace_lea.common.forms.transactions.TransactionV2SearchCriteria;
import org.marketplace_lea.common.forms.transactions.ValidateTransactionForm;
import org.marketplace_lea.common.forms.transactions.ValidationTransactionStatus;
import org.marketplace_lea.common.mapper.TransactionV2Mapper;
import org.marketplace_lea.common.repositories.TransactionV2Repository;
import org.marketplace_lea.common.repositories.WalletV2JpaRepository;
import org.marketplace_lea.common.services.transaction.TransactionV2Service;
import org.marketplace_lea.common.services.transaction.specifications.TransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionV2ServiceImpl implements TransactionV2Service {
    private final TransactionV2Repository transactionRepository;
    private final WalletV2JpaRepository walletRepository;
    private final TransactionV2Mapper transactionMapper;

    @Override
    public TransactionDTO createTransaction(TransactionV2CreateForm form) {
        log.info("[TransactionV2Service] Creating new transaction with reference: {}", form.getPaymentReference());

        // Vérifier l'existence des wallets
        WalletV2Entity source = walletRepository.findById(form.getSourceWalletId())
                .orElseThrow(() -> new ConsoEpargneException("Wallet source non trouvé: " + form.getSourceWalletId(), HttpStatus.NOT_FOUND));

        WalletV2Entity destination = walletRepository.findById(form.getDestinationWalletId())
                .orElseThrow(() -> new ConsoEpargneException("Wallet destination non trouvé: " + form.getDestinationWalletId(), HttpStatus.NOT_FOUND));

        validateSameSource(source, destination);

        // Mapper le formulaire vers l'entité
        TransactionV2Entity transaction = transactionMapper.toDto(form);
        
        // Remplacer les wallets proxy par les vrais objets
        transaction.setSource(source);
        transaction.setDestination(destination);

        TransactionV2Entity saved = transactionRepository.save(transaction);
        log.info("[TransactionV2Service] Transaction created with ID: {}", saved.getId());

        return transactionMapper.toDto(saved);
    }

    @Override
    public TransactionDTO validateTransaction(ValidateTransactionForm form) {
        String options = ValidationTransactionStatus.VALIDATE.equals(form.status()) ? "Validate" : "Invalidate";
        log.info("[TransactionV2Service] {} transaction: {}", options, form.transactionId());

        TransactionV2Entity transaction = transactionRepository.findById(form.transactionId())
                .orElseThrow(() -> new RuntimeException("Transaction non trouvée: " + form.transactionId()));

        validateTransaction(form.transactionId(), options, transaction.getTransactionStatus());

        if(ValidationTransactionStatus.VALIDATE.equals(form.status())) {
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            transaction.setValidatedAt(LocalDateTime.now());
        } else {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction.setFailedAt(LocalDateTime.now());

            String currentDesc = transaction.getDescription();
            transaction.setDescription(currentDesc != null ? currentDesc + " - Rejeté: " + form.rejectionMessage() : "Rejeté: " + form.rejectionMessage());
        }

        if(transaction.hasSameSource()) {
            log.error("Error ! Failed to create transaction. Wallet must be different !");
            throw new ConsoEpargneException("Failed to create transaction. wallets must be different !", HttpStatus.BAD_REQUEST);
        }

        TransactionV2Entity transactionUpdated = transactionRepository.save(transaction);
        log.info("[TransactionV2Service] Transaction validated: {}", transactionUpdated.getId());

        return transactionMapper.toDto(transactionUpdated);
    }

    @Override
    public Optional<TransactionDTO> findTransactionById(String id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toDto);
    }

    @Override
    public Page<TransactionDTO> searchTransactions(TransactionV2SearchCriteria criteria, Pageable pageable) {
        Specification<TransactionV2Entity> spec = TransactionSpecification.buildSpecification(criteria);
        return transactionRepository.findAll(spec, pageable)
                .map(transactionMapper::toDto);
    }


    /// Méthodes privées.
    public void validateSameSource(WalletV2Entity source, WalletV2Entity destination) {
        if (source.equals(destination)) {
            log.info("[TransactionV2Service] No transaction with same wallet (source and destination) cannot be done !");
            throw new ConsoEpargneException("Impossible de créer une transaction avec le même wallet source et destination !", HttpStatus.BAD_REQUEST);
        }
    }


    public void validateTransaction(String transactionId, String options, TransactionStatus status) {
        if (!TransactionStatus.PENDING.equals(status) && !TransactionStatus.IN_PROGRESS.equals(status)) {
            log.info("[TransactionV2Service] {} transaction: {}, this transaction cannot be {}", options, transactionId, options);
            throw new ConsoEpargneException("Impossible de valider une transaction déjà traitée !", HttpStatus.BAD_REQUEST);
        }
    }
}