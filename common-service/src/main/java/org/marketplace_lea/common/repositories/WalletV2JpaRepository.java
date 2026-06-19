package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.dtos.ConsomWalletStatsDTO;
import org.marketplace_lea.common.dtos.wallets.WalletBalanceDTO;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WalletV2JpaRepository extends JpaRepository<WalletV2Entity, String> {
    /**
     * Récupère les wallets d'un type donné pour une liste de comptes.
     * Permet d'éviter les requêtes N+1 lors du calcul des statistiques réseau.
     *
     * @param accountIds identifiants des comptes
     * @param type       type de wallet recherché
     * @return liste des wallets correspondants
     */
    @Query("SELECT w FROM WalletV2 w WHERE w.walletType = :type AND w.account.id IN :accountIds")
    List<WalletV2Entity> findByAccountIdInAndType(@Param("accountIds") List<String> accountIds, @Param("type") WalletV2Type type);

    @Query("SELECT w FROM WalletV2 w WHERE w.walletType = :type AND w.account.id = :account_id")
    Optional<WalletV2Entity> getByAccountIdAndType(@Param("account_id") String accountId, @Param("type") WalletV2Type walletType);


    @Query("SELECT new org.marketplace_lea.common.dtos.wallets.WalletBalanceDTO(w.id, w.investedCoinAmount, w.balance, w.account.id, w.account.login, w.numberAndOperator) FROM WalletV2 w WHERE w.walletType = :type AND w.investedCoinAmount > 0")
    Page<WalletBalanceDTO> findAvailableConsomPointsForSale(Pageable pageable);

    @Query("SELECT new org.marketplace_lea.common.dtos.wallets.WalletBalanceDTO(w.id, w.investedCoinAmount, w.balance, w.account.id, w.account.login, w.numberAndOperator) FROM WalletV2 w WHERE w.walletType = :type AND w.balance > 0")
    Page<WalletBalanceDTO> findWalletsByBalancePositive(@Param("type") WalletV2Type type, Pageable pageable);

    @Query("SELECT SUM(w.balance) FROM WalletV2 w WHERE w.walletType = :type")
    BigDecimal sumBalanceByType(@Param("type") WalletV2Type type);

    @Query("SELECT SUM(w.totalDrawnAmount) FROM WalletV2 w WHERE w.walletType = :type")
    BigDecimal sumTotalDrawnAmountByType(@Param("type") WalletV2Type type);

    @Query("SELECT new org.marketplace_lea.common.dtos.ConsomWalletStatsDTO(SUM(w.balance), SUM(w.totalDrawnAmount)) FROM WalletV2 w WHERE w.walletType = :type")
    Optional<ConsomWalletStatsDTO> getConsomWalletStats(@Param("type") WalletV2Type type);
}
