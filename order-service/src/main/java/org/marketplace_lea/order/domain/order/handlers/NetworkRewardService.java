package org.marketplace_lea.order.domain.order.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.entities.CurrencyValue;
import org.marketplace_lea.common.entities.account.AccountSponsorshipEntity;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.entities.transaction.TransactionType;
import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Type;
import org.marketplace_lea.common.repositories.TransactionV2JpaRepository;
import org.marketplace_lea.common.repositories.account.AccountSponsorshipJpaRepository;
import org.marketplace_lea.common.services.wallet.WalletV2Service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NetworkRewardService {
    private final WalletV2Service walletService;
    private final TransactionV2JpaRepository transactionService;
    private final AccountSponsorshipJpaRepository sponsorshipRepository;

    /**
     * Distribue les récompenses au réseau de parrainage.
     *
     * @param account   le compte à l'origine de la récompense
     * @param amount    le montant à distribuer
     * @param maxLevel  le nombre maximum de niveaux à remonter
     */
    public void dispatchToNetwork(AccountV2Entity account, float amount, int maxLevel) {
        try {
            List<WalletV2Entity> wallets = new ArrayList<>();
            List<TransactionV2Entity> transactions = new ArrayList<>();

            // Récupérer le parent direct
            Optional<AccountSponsorshipEntity> currentSponsorship = sponsorshipRepository.getActiveSponsorByChildId(account.getId());
            int level = 1;

            while (currentSponsorship.isPresent() && level <= maxLevel) {
                AccountV2Entity parent = currentSponsorship.get().getParent();

                // Vérifier que le parent n'est pas le compte système
                if (parent.hasSystemType()) {
                    log.info("[NetworkRewardService.dispatchToNetwork] Reached system account at level {}, stopping", level);
                    break;
                }

                WalletV2Entity wallet = walletService.getOrCreateWallet(parent, CurrencyValue.LEA, WalletV2Type.PERSONAL);

                if (level == 1) {
                    wallet.addToFirstLevelSaving(amount);
                    log.debug("[NetworkRewardService.dispatchToNetwork] Added {} to first level saving for parent {}", amount, parent.getId());
                }

                wallet.addToNetworkSaving(amount);

                TransactionV2Entity transaction = buildTransaction(null, wallet, amount, TransactionType.NETWORK_PAYMENT, account.getLogin());
                transactions.add(transaction);
                wallets.add(wallet);

                log.info("[NetworkRewardService.dispatchToNetwork] Added {} to parent {} at level {}",
                    amount, parent.getId(), level);

                // Passer au niveau supérieur
                currentSponsorship = sponsorshipRepository.getActiveSponsorByChildId(parent.getId());
                level++;
            }

            if (!wallets.isEmpty()) {
                walletService.save(wallets);
                transactions.forEach(TransactionV2Entity::validate);
                transactionService.saveAll(transactions);
                log.info("[NetworkRewardService.dispatchToNetwork] Successfully dispatched {} to {} sponsors",
                    amount, wallets.size());
            } else {
                log.info("[NetworkRewardService.dispatchToNetwork] No eligible sponsors found for account {}", account.getId());
            }

        } catch (Exception e) {
            log.error("[NetworkRewardService.dispatchToNetwork] Error dispatching to network for account {}: {}",
                account.getId(), e.getMessage(), e);
            throw new ConsoEpargneException("Failed to dispatch to network !", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private TransactionV2Entity buildTransaction(WalletV2Entity source, WalletV2Entity destination, double amount, TransactionType type, String phoneNumber) {
        return TransactionV2Entity.builder()
                .source(source)
                .destination(destination)
                .amount(BigDecimal.valueOf(amount))
                .transactionType(type)
                .phoneNumber(phoneNumber)
                .build();
    }
}