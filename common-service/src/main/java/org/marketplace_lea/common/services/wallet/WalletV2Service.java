package org.marketplace_lea.common.services.wallet;

import org.marketplace_lea.common.dtos.ConsomWalletDTO;
import org.marketplace_lea.common.dtos.ConsomWalletStatsDTO;
import org.marketplace_lea.common.dtos.wallets.WalletDTO;
import org.marketplace_lea.common.dtos.wallets.WalletV2DTO;
import org.marketplace_lea.common.entities.CurrencyValue;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des portefeuilles (wallets) version 2.
 * 
 * <p>Responsabilités :
 * <ul>
 *   <li>Création et récupération des wallets par type pour un compte</li>
 *   <li>Opérations sur les wallets : envoi, vente, achat de points ConsoM</li>
 *   <li>Conversion de points en LEA</li>
 *   <li>Statistiques consolidées</li>
 * </ul>
 * </p>
 */
public interface WalletV2Service {
    /**
     * Récupère ou crée un wallet du type spécifié pour un compte.
     * 
     * @param account    le compte propriétaire
     * @param walletType le type de wallet (PERSONAL, LEA, CONSOM, etc.)
     * @return le wallet (existant ou nouvellement créé)
     */
    WalletV2Entity getOrCreateWallet(AccountV2Entity account, CurrencyValue currencyValue, WalletV2Type walletType);

    /**
     * Récupère ou crée un wallet du type spécifié pour un compte.
     *
     * @param walletId   le wallet Id
     * @return le wallet (existant ou nouvellement créé)
     */
    WalletV2Entity getById(String walletId);


    /**
     * Crée un nouveau wallet pour un compte.
     * 
     * @param walletType type de wallet
     * @param account    compte propriétaire
     * @return le wallet créé et persisté
     */
    WalletV2Entity createNewWallet(WalletV2Type walletType, CurrencyValue devise, AccountV2Entity account);

    /**
     * Envoie des points ConsoM d'un expéditeur à un destinataire.
     * 
     * @param senderAccount   compte expéditeur
     * @param receiverAccount compte destinataire
     * @param amount          montant à envoyer
     * @param removeSent      si true, ajoute le montant à totalAmountSend de l'expéditeur
     * @return message de confirmation
     */
    String sendConsomPoints(AccountV2Entity senderAccount, AccountV2Entity receiverAccount, float amount, boolean removeSent);

    /**
     * Vend des points ConsoM (les place en investedCoinAmount).
     * 
     * @param sellerAccount     compte vendeur
     * @param amount            montant à vendre
     * @param numberAndOperator information opérateur (stockée dans le wallet)
     */
    void sellConsomPoints(AccountV2Entity sellerAccount, BigDecimal amount, String numberAndOperator);

    /**
     * Achète des points ConsoM auprès d'un vendeur.
     * 
     * @param buyerAccount  compte acheteur
     * @param sellerAccount compte vendeur
     * @param amount        montant à acheter
     */
    void buyConsomPoints(AccountV2Entity buyerAccount, AccountV2Entity sellerAccount, BigDecimal amount);

    /**
     * Applique un débit au wallet source.
     *
     * @param transaction  transaction
     */
    void applyDebitToSource(TransactionV2Entity transaction);

    /**
     * Applique un débit au wallet destination.
     *
     * @param transaction (Transaction).
     */
    void applyCreditToDestination(TransactionV2Entity transaction);

    /**
     * Applique un débit au wallet destination.
     *
     * @param transaction (Transaction).
     */
    void saveWallets(TransactionV2Entity transaction);

    /**
     * Retourne la liste paginée des comptes ayant des points ConsoM disponibles à la vente
     * (ceux dont investedCoinAmount > 0).
     * 
     * @param pageable critères de pagination
     * @return page de WalletAccountV2DTO
     */
    Page<WalletV2DTO> getAvailableConsomPointsForSale(Pageable pageable);

    /**
     * Retourne la liste paginée des comptes ayant un solde ConsoM positif.
     * 
     * @param pageable critères de pagination
     * @return page de WalletAccountV2DTO
     */
    Page<WalletV2DTO> getPeopleWithConsomPoints(Pageable pageable);

    /**
     * Récupère le DTO d'un wallet ConsoM pour un compte donné.
     * 
     * @param account le compte
     * @return DTO contenant les informations du wallet ConsoM, ou null si inexistant
     */
    Optional<ConsomWalletDTO> getConsomWalletByAccountId(AccountV2Entity account);

    /**
     * Convertit des points (depuis le PersonalWallet) en LEA (LeaWallet) et consomme des ConsoM.
     * 
     * @param account le compte utilisateur
     * @param points  nombre de points à convertir (doit être positif)
     * @return message de confirmation
     */
    String convertPointsToLea(AccountV2Entity account, int points);

    /**
     * Met à jour plusieurs wallets
     *
     * @return List<WalletV2DTO>
     */
    List<WalletDTO> save(List<WalletV2Entity> walletV2Entities);

    /**
     * Retourne les totaux globaux des wallets ConsoM sous forme de DTO.
     * 
     * @return ConsomWalletStatsDTO
     */
    ConsomWalletStatsDTO getStatsWallet();
}