package org.marketplace_lea.common.services.wallet.impl;

import org.marketplace_lea.common.common.constants.ConsoEpargneConstants;
import org.marketplace_lea.common.common.exceptions.AmountCannotBeNegative2Exception;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.common.exceptions.ConsoOperationWalletException;
import org.marketplace_lea.common.common.exceptions.ConsoWalletException;
import org.marketplace_lea.common.common.utils.ConvertValueConsoUtils;
import org.marketplace_lea.common.dtos.ConsomWalletDTO;
import org.marketplace_lea.common.dtos.ConsomWalletStatsDTO;
import org.marketplace_lea.common.dtos.wallets.WalletBalanceDTO;
import org.marketplace_lea.common.dtos.wallets.WalletDTO;
import org.marketplace_lea.common.entities.CurrencyValue;
import org.marketplace_lea.common.entities.CurrencyV2Entity;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Type;
import org.marketplace_lea.common.mapper.WalletMapper;
import org.marketplace_lea.common.repositories.CurrencyJpaRepository;
import org.marketplace_lea.common.repositories.WalletV2JpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.services.wallet.WalletV2Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Implémentation du service de gestion des wallets V2.
 *
 * <p>Cette version utilise une seule entité {@link WalletV2Entity} et un seul repository,
 * ce qui simplifie la persistance par rapport à l'ancienne architecture avec classes filles.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletV2ServiceImpl implements WalletV2Service {
    private final WalletV2JpaRepository walletRepository;
    private final CurrencyJpaRepository currencyRepository;
    private final WalletMapper walletMapper;

    // ==================== Méthodes publiques principales ====================

    @Override
    public WalletV2Entity getOrCreateWallet(AccountV2Entity account, CurrencyValue currencyValue, WalletV2Type walletType) {
        Objects.requireNonNull(account, "Account ne peut pas être null");
        Objects.requireNonNull(walletType, "WalletType ne peut pas être null");

        Optional<WalletV2Entity> existingWallet = walletRepository.getByAccountIdAndType(account.getId(), walletType);
        if (existingWallet.isPresent()) {
            log.debug("Wallet existant trouvé pour account={}, type={}", account.getLogin(), walletType);
            return existingWallet.get();
        }

        log.info("Création d'un nouveau wallet type={} pour account={}", walletType, account.getLogin());
        return createNewWallet(walletType, currencyValue, account);
    }

    @Override
    public WalletV2Entity getById(String walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ConsoEpargneException("Wallet source non trouvé: " + walletId, HttpStatus.NOT_FOUND));
    }

    @Override
    public WalletV2Entity getByAccountIdAndType(String accountId, WalletV2Type type) {
        return walletRepository.getByAccountIdAndType(accountId, type)
                .orElseThrow(() -> new ConsoEpargneException("Wallet source non trouvé: " + accountId, HttpStatus.NOT_FOUND));
    }


    @Override
    @Transactional
    public WalletV2Entity createNewWallet(WalletV2Type walletType, CurrencyValue currencyValue, AccountV2Entity account) {
        validateAccount(account);
        var currency = getCorrectCurrency(currencyValue)
                .orElseThrow(() -> new ConsoEpargneException("La devise n'existe pas !", HttpStatus.INTERNAL_SERVER_ERROR));

        WalletV2Entity wallet = buildWalletEntity(account, walletType, currency);

        // L'ID est généré par le repository (via @PrePersist ou générateur externe)
        WalletV2Entity saved = walletRepository.save(wallet);
        log.info("[WalletV2Service] Nouveau wallet créé - id={}, type={}, account={}",
                saved.getId(), walletType, account.getLogin());
        return saved;
    }

    @Override
    @Transactional
    public String sendConsomPoints(AccountV2Entity senderAccount, AccountV2Entity receiverAccount, float amount, boolean removeSent) {
        WalletV2Entity senderWallet = getOrCreateWallet(senderAccount, CurrencyValue.CONSOM, WalletV2Type.CONSOM);
        WalletV2Entity receiverWallet = getOrCreateWallet(receiverAccount, CurrencyValue.CONSOM, WalletV2Type.CONSOM);

        BigDecimal amountBD = BigDecimal.valueOf(amount);

        // Vérifier solde suffisant chez l'expéditeur
        if (senderWallet.getBalance().compareTo(amountBD) < 0) {
            throw new ConsoWalletException("Solde insuffisant pour envoyer " + amount + " ConsoM");
        }

        // Débiter l'expéditeur
        senderWallet.setBalance(senderWallet.getBalance().subtract(amountBD));
        if (removeSent) {
            senderWallet.setTotalAmountSend(senderWallet.getTotalAmountSend().add(amountBD));
        }

        // Créditer le destinataire
        receiverWallet.setBalance(receiverWallet.getBalance().add(amountBD));
        receiverWallet.setTotalAmountReceived(receiverWallet.getTotalAmountReceived().add(amountBD));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        log.info("Envoi réussi : {} ConsoM de {} vers {}", amount, senderAccount.getLogin(), receiverAccount.getLogin());
        return "Envoi réussi.";
    }

    @Override
    @Transactional
    public void sellConsomPoints(AccountV2Entity sellerAccount, BigDecimal amount, String numberAndOperator) {
        WalletV2Entity sellerWallet = getOrCreateWallet(sellerAccount, CurrencyValue.CONSOM, WalletV2Type.CONSOM);

        // Mise à jour du numéro/opérateur (concaténation si nécessaire, comme dans l'ancien code)
        String existing = sellerWallet.getNumberAndOperator();
        if (existing == null || existing.isBlank()) {
            sellerWallet.setNumberAndOperator(numberAndOperator);
        } else if (!existing.contains(numberAndOperator)) {
            sellerWallet.setNumberAndOperator(existing + "," + numberAndOperator);
        }

        // Vérifier solde suffisant
        if (sellerWallet.getBalance().compareTo(amount) < 0) {
            throw new ConsoOperationWalletException("Solde insuffisant pour vendre " + amount + " ConsoM");
        }

        // Débiter la balance et créditer investedCoinAmount
        sellerWallet.setBalance(sellerWallet.getBalance().subtract(amount));
        sellerWallet.setInvestedCoinAmount(sellerWallet.getInvestedCoinAmount().add(amount));

        walletRepository.save(sellerWallet);
        log.info("Vente de {} ConsoM par {}", amount, sellerAccount.getLogin());
    }

    @Override
    @Transactional
    public void buyConsomPoints(AccountV2Entity buyerAccount, AccountV2Entity sellerAccount, BigDecimal amount) {
        WalletV2Entity buyerWallet = getOrCreateWallet(buyerAccount, CurrencyValue.CONSOM, WalletV2Type.CONSOM);
        WalletV2Entity sellerWallet = getOrCreateWallet(sellerAccount, CurrencyValue.CONSOM, WalletV2Type.CONSOM);

        // Vérifier que le vendeur a assez de points investis
        if (sellerWallet.getInvestedCoinAmount().compareTo(amount) < 0) {
            throw new ConsoOperationWalletException("Le vendeur n'a pas assez de points ConsoM investis");
        }

        // Retirer des points investis du vendeur
        sellerWallet.setInvestedCoinAmount(sellerWallet.getInvestedCoinAmount().subtract(amount));
        // Optionnel : retirer aussi de la balance ? Dans l'ancien code on ne modifiait que investi.
        // Ici on suit la logique originale : seul investedCoinAmount est modifié chez le vendeur.
        // Chez l'acheteur : on ajoute à totalAmountReceivedFromSell ? L'ancien code utilisait addToReceivedFromSell.
        // Dans WalletV2Entity, il n'y a pas de champ spécifique "receivedFromSell". On peut utiliser totalAmountReceived.
        buyerWallet.setTotalAmountReceived(buyerWallet.getTotalAmountReceived().add(amount));
        // Optionnel : ajouter à la balance de l'acheteur ? L'ancien code ne le faisait pas explicitement,
        // car l'acheteur utilise ses propres points. Mais logique métier : l'acheteur reçoit des points.
        // Nous ajoutons à la balance pour être cohérent.
        buyerWallet.setBalance(buyerWallet.getBalance().add(amount));

        walletRepository.save(sellerWallet);
        walletRepository.save(buyerWallet);

        log.info("Achat réussi : {} ConsoM de {} par {}", amount, sellerAccount.getLogin(), buyerAccount.getLogin());
    }

    @Override
    public void applyDebitToSource(TransactionV2Entity transaction) {
        BigDecimal debitAmount = transaction.retrieveAmountToDebit();
        var walletSource = transaction.getSource();

        if (transaction.hasLeaToPointTransfert()) {
            // Case 1: LEA Coin to Point transfer - debit coinAmount
            walletSource.removeFromBalance(debitAmount);
        } else if (transaction.hasProfitsToLeaTransfert()) {
            // Case 2: Profit to LEA transfer - debit from investment profits
            walletSource.removeFromInvestmentProfitsBalance(transaction.getAmount());
        } else {
            // Case 3: Standard transaction
            walletSource.removeFromBalance(debitAmount);
        }

        log.info("Débit appliqué: {} pour la transaction {}", debitAmount, transaction.getId());
        this.saveWallets(transaction);
    }

    @Override
    public void applyCreditToDestination(TransactionV2Entity transaction) {
        BigDecimal creditAmount = transaction.retrieveAmountToCredit();
        var destination = transaction.getDestination();
        destination.addToBalance(creditAmount);

        log.info("Crédit appliqué: {} pour le wallet {}", creditAmount, transaction.getId());
        var destinationSaved = this.walletRepository.save(destination);
        log.info("Crédit appliqué: {} pour le wallet {}", creditAmount, destinationSaved.getId());
    }

    @Override
    public void saveWallets(TransactionV2Entity transaction) {
        Set<WalletV2Entity> walletsToSave = new HashSet<>();

        if (transaction.getSource() != null) {
            walletsToSave.add(transaction.getSource());
        }
        if (transaction.getDestination() != null) {
            walletsToSave.add(transaction.getDestination());
        }

        if (!walletsToSave.isEmpty()) {
            walletRepository.saveAll(walletsToSave);
            log.info("Portefeuilles sauvegardés pour la transaction {}", transaction.getId());
        }
    }

    @Override
    public Page<WalletBalanceDTO> getAvailableConsomPointsForSale(Pageable pageable) {
        return walletRepository.findAvailableConsomPointsForSale(pageable);
    }

    @Override
    public Page<WalletBalanceDTO> getPeopleWithConsomPoints(Pageable pageable) {
        return walletRepository.findWalletsByBalancePositive(WalletV2Type.CONSOM, pageable);
    }

    @Override
    public Optional<ConsomWalletDTO> getConsomWalletByAccountId(AccountV2Entity account) {
        Optional<WalletV2Entity> wallet = walletRepository.getByAccountIdAndType(account.getId(), WalletV2Type.CONSOM);
        return wallet.map(w -> new ConsomWalletDTO(
                w.getId(),
                w.getInvestedCoinAmount(),
                w.getBalance(),
                w.getAccount().getId(),
                w.getReservedAmount()
        ));
    }

    @Override
    @Transactional
    public String convertPointsToLea(AccountV2Entity account, int points) {
        if (points <= 0) {
            throw new AmountCannotBeNegative2Exception("Le nombre de points doit être positif !");
        }

        // Récupération des wallets nécessaires
        Optional<WalletV2Entity> personalWalletFound = walletRepository.getByAccountIdAndType(account.getId(), WalletV2Type.PERSONAL);
        Optional<WalletV2Entity> leaWalletFound = walletRepository.getByAccountIdAndType(account.getId(), WalletV2Type.LEA);
        Optional<WalletV2Entity> consomWalletFound = walletRepository.getByAccountIdAndType(account.getId(), WalletV2Type.CONSOM);

        /// On vérifie que les wallets existent.
        verifyWallet(personalWalletFound);
        verifyWallet(leaWalletFound);
        verifyWallet(consomWalletFound);

        var personalWallet = personalWalletFound.get();
        var leaWallet = leaWalletFound.get();
        var consomWallet = consomWalletFound.get();

        BigDecimal pointsBD = BigDecimal.valueOf(points);
        // Vérifier que le personalWallet a assez de points
        if (personalWallet.getBalance().compareTo(pointsBD) < 0) {
            throw new AmountCannotBeNegative2Exception("Fonds insuffisants en points personnels");
        }

        // Conversion
        BigDecimal leaAmount = ConvertValueConsoUtils.convertPointToLea(points);
        int consomAmount = ConvertValueConsoUtils.convertPointToConsom(points);
        BigDecimal consomAmountBD = BigDecimal.valueOf(consomAmount);

        // Vérifier que le wallet ConsoM a assez de points (dans l'ancien code on validait aussi)
        if (consomWallet.getBalance().compareTo(consomAmountBD) < 0) {
            throw new AmountCannotBeNegative2Exception("Fonds insuffisants en ConsoM !");
        }

        // Mise à jour des soldes
        personalWallet.setBalance(personalWallet.getBalance().subtract(pointsBD));
        leaWallet.setBalance(leaWallet.getBalance().add(leaAmount));
        consomWallet.setBalance(consomWallet.getBalance().subtract(consomAmountBD));

        walletRepository.save(personalWallet);
        walletRepository.save(leaWallet);
        walletRepository.save(consomWallet);

        log.info("Conversion réussie pour {} : {} points -> {} LEA, -{} ConsoM",
                account.getLogin(), points, leaAmount, consomAmount);
        return String.format("Conversion réussie : %s Pts → %s Lea et -%s ConsoM.", points, leaAmount, consomAmount);
    }

    @Override
    public List<WalletDTO> save(List<WalletV2Entity> walletV2Entities) {
        List<WalletV2Entity> walletsSaved = walletRepository.saveAll(walletV2Entities);
        return walletsSaved.stream()
                .map(walletMapper::toDto)
                .toList();
    }

    @Override
    public ConsomWalletStatsDTO getStatsWallet() {
        Optional<ConsomWalletStatsDTO> walletStatsDTO = walletRepository.getConsomWalletStats(WalletV2Type.CONSOM);
        return walletStatsDTO.orElse(ConsomWalletStatsDTO.ZERO());
    }

    // ==================== Méthodes privées de validation ====================

    private void validateAccount(AccountV2Entity account) {
        if (account == null) {
            throw new ConsoWalletException("Le compte ne peut pas être null");
        }
        // Optionnel : vérifier que l'entité est managée ou possède un ID
        if (account.getId() == null) {
            throw new ConsoWalletException("Le compte doit être persisté avant de créer un wallet");
        }
    }

    private Optional<CurrencyV2Entity> getCorrectCurrency(CurrencyValue devise) {
        if (Objects.isNull(devise)) {
            return currencyRepository.findById(ConsoEpargneConstants.DEFAULT_DEVISE);
        }
        return currencyRepository.findById(devise.name());
    }

    private void verifyWallet(Optional<WalletV2Entity> walletV2Entity) {
        if (walletV2Entity.isEmpty()) {
            throw new ConsoEpargneNotFoundDataException("Le wallet n'existe pas !");
        }
    }

    private WalletV2Entity buildWalletEntity(AccountV2Entity account, WalletV2Type type, CurrencyV2Entity currency) {
        WalletV2Entity wallet = new WalletV2Entity();
        wallet.setAccount(account);
        wallet.setWalletType(type);
        wallet.setCurrency(currency.getCode());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setTotalAmountSend(BigDecimal.ZERO);
        wallet.setTotalAmountReceived(BigDecimal.ZERO);
        wallet.setInvestedCoinAmount(BigDecimal.ZERO);
        wallet.setPartnerBonusAmount(BigDecimal.ZERO);
        wallet.setPersonalSavingAmount(BigDecimal.ZERO);
        wallet.setFirstLevelSavingAmount(BigDecimal.ZERO);
        wallet.setNetworkSavingAmount(BigDecimal.ZERO);
        wallet.setSpecialBonusAmount(BigDecimal.ZERO);
        wallet.setVoucherAmount(BigDecimal.ZERO);
        wallet.setTotalDrawnAmount(BigDecimal.ZERO);
        wallet.setReservedAmount(BigDecimal.ZERO);
        wallet.setNumberAndOperator(""); // initialisé vide
        return wallet;
    }
}