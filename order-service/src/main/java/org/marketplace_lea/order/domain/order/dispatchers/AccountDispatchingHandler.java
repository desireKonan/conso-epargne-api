package org.marketplace_lea.order.domain.order.dispatchers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.entities.account.AccountSponsorshipEntity;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.entities.subscription.ConsoSubscriptionV2Entity;
import org.marketplace_lea.common.entities.subscription.ConsoSubscriptionType;
import org.marketplace_lea.common.repositories.ConsoSubscriptionRepository;
import org.marketplace_lea.common.repositories.account.AccountSponsorshipJpaRepository;
import org.marketplace_lea.order.common.entities.order.OrderItemV2Entity;
import org.marketplace_lea.order.domain.order.handlers.CommissionHandler;
import org.marketplace_lea.order.domain.order.handlers.impl.DefaultPersonalSavingHandler;
import org.marketplace_lea.prometheus.domain.parameter_config.services.ParameterConfigService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.PARTNER;
import static org.marketplace_lea.common.common.constants.ConsoEpargneConstants.SYSTEM_ACCOUNT_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDispatchingHandler {

    // Clés de configuration
    private static final String CONFIG_CASHBACK_ACTIVE = "commission.active.cashback";
    private static final String CONFIG_MAX_NETWORK_LEVEL = "commission.network.max.level";
    private static final String CONFIG_ADHERANT_KIT_COMMISSION_RATE = "commission.rate.adherant.kit";
    private static final String CONFIG_ADHERENT_COMMISSION_RATE = "commission.rate.adherent";
    private static final String CONFIG_SOLIDARITY_FUND_RATE = "commission.rate.solidarity.fund";
    private static final String CONFIG_NETWORK_DISPATCH_RATE = "commission.rate.network.dispatch";
    private static final String CONFIG_OR_SUBSCRIPTION_MULTIPLIER = "subscription.or.multiplier";

    // Valeurs par défaut
    private static final int DEFAULT_MAX_NETWORK_LEVEL = 1;
    private static final double DEFAULT_ADHERANT_KIT_COMMISSION_RATE = 0.08;
    private static final double DEFAULT_ADHERENT_COMMISSION_RATE = 0.06;
    private static final double DEFAULT_SOLIDARITY_FUND_RATE = 0.5;
    private static final double DEFAULT_NETWORK_DISPATCH_RATE = 0.2;
    private static final double DEFAULT_OR_SUBSCRIPTION_MULTIPLIER = 2.0;

    private final ConsoSubscriptionRepository subscriptionRepository;
    private final AccountSponsorshipJpaRepository sponsorshipRepository;
    private final ParameterConfigService configService;
    private final NetworkRewardDispatcher networkRewarDispatcher;
    private final DefaultPersonalSavingHandler defaultPersonalSavingHandler;
    private final CommissionHandler commissionService;

    /**
     * Redistribue les économies générées par une commande.
     *
     * @param account           le compte qui a effectué l'achat
     * @param totalSavingAmount le montant total d'économie généré
     */
    public void dispatchSaving(AccountV2Entity account, CustomerV2Entity customer, float totalSavingAmount) {
        if (account == null) {
            log.warn("[AccountDispatchingHandler.dispatchSaving] Account is null, skipping dispatch");
            return;
        }

        try {
            boolean isCashbackActive = isCashbackActive();
            List<ConsoSubscriptionV2Entity> subscriptions = getCurrentSubscriptions(customer);

            log.info("[AccountDispatchingHandler.dispatchSaving] Account: {}, Cashback active: {}, Subscriptions count: {}",
                    account.getId(), isCashbackActive, subscriptions.size());

            float adjustedAmount = totalSavingAmount;

            // Application du bonus pour les abonnements OR
            if (isCashbackActive || !subscriptions.isEmpty()) {
                adjustedAmount = applySubscriptionBonus(subscriptions, totalSavingAmount);
            }

            // Redistribution selon le type d'abonnement
            if (!subscriptions.isEmpty()) {
                handleSubscriptionBasedDispatching(account, adjustedAmount, subscriptions.getFirst());
            } else {
                handleStandardDispatching(account, adjustedAmount);
            }

        } catch (Exception e) {
            log.error("[AccountDispatchingHandler.dispatchSaving] Error processing dispatch for account {}: {}",
                    account.getId(), e.getMessage(), e);
        }
    }

    /**
     * Redistribue les économies générées par l'achat d'un kit d'adhésion.
     *
     * @param account   le compte qui a effectué l'achat
     * @param orderItem l'item de commande correspondant au kit d'adhésion
     */
    public void dispatchSavingForAdhesionKit(AccountV2Entity account, CustomerV2Entity customer, Optional<OrderItemV2Entity> orderItem) {
        if (account == null) {
            log.warn("[AccountDispatchingHandler.dispatchSavingForAdhesionKit] Account is null, skipping dispatch");
            return;
        }

        // Récupérer le parent via la relation de parrainage
        Optional<AccountV2Entity> parent = getSponsorParent(account);
        if (parent.isEmpty()) {
            log.info("[AccountDispatchingHandler.dispatchSavingForAdhesionKit] No sponsor parent found for account {}", account.getId());
            return;
        }

        AccountV2Entity sponsorParent = parent.get();

        try {
            boolean isCashbackActive = isCashbackActive();
            List<ConsoSubscriptionV2Entity> subscriptions = getCurrentSubscriptions(customer);

            log.info("[AccountDispatchingHandler.dispatchSavingForAdhesionKit] Account: {}, Sponsor Parent: {}, Cashback active: {}, Subscriptions count: {}",
                    account.getId(), sponsorParent.getId(), isCashbackActive, subscriptions.size());

            if (isCashbackActive || !subscriptions.isEmpty()) {
                processAdhesionKitCommission(account, sponsorParent, orderItem, subscriptions.getFirst());
            }

        } catch (Exception e) {
            log.error("[AccountDispatchingHandler.dispatchSavingForAdhesionKit] Error processing for account {}: {}",
                    account.getId(), e.getMessage(), e);
        }
    }

    /**
     * Applique le bonus de souscription sur le montant.
     */
    private float applySubscriptionBonus(List<ConsoSubscriptionV2Entity> subscriptions, float amount) {
        if (subscriptions.isEmpty()) {
            return amount;
        }

        ConsoSubscriptionV2Entity subscription = subscriptions.getFirst();
        if (ConsoSubscriptionType.OR.value().equals(subscription.getType())) {
            double multiplier = configService.getDoubleValueOrDefault(
                    CONFIG_OR_SUBSCRIPTION_MULTIPLIER,
                    DEFAULT_OR_SUBSCRIPTION_MULTIPLIER
            );
            float doubledAmount = (float) (amount * multiplier);
            log.info("[AccountDispatchingHandler.applySubscriptionBonus] OR subscription bonus applied: {} -> {}",
                    amount, doubledAmount);
            return doubledAmount;
        }

        return amount;
    }

    /**
     * Gère la redistribution basée sur le type d'abonnement.
     */
    private void handleSubscriptionBasedDispatching(
            AccountV2Entity account,
            float amount,
            ConsoSubscriptionV2Entity subscription
    ) {
        if (ConsoSubscriptionType.SOLIDARITY_FUND.value().equals(subscription.getType())) {
            double solidarityRate = configService.getDoubleValueOrDefault(
                    CONFIG_SOLIDARITY_FUND_RATE,
                    DEFAULT_SOLIDARITY_FUND_RATE
            );
            float amountToDispatch = (float) (amount * solidarityRate);
            int maxLevel = configService.getIntValueOrDefault(
                    CONFIG_MAX_NETWORK_LEVEL,
                    DEFAULT_MAX_NETWORK_LEVEL
            );

            log.info("[AccountDispatchingHandler.handleSubscriptionBasedDispatching] Solidarity fund: dispatching {} to network",
                    amountToDispatch);
            networkRewarDispatcher.dispatchToNetwork(account, amountToDispatch, maxLevel);
        } else {
            log.info("[AccountDispatchingHandler.handleSubscriptionBasedDispatching] Standard subscription: adding {} to personal saving",
                    amount);
            defaultPersonalSavingHandler.addToPersonalSaving(account.getId(), amount);
        }
    }

    /**
     * Gère la redistribution standard (sans abonnement actif).
     */
    private void handleStandardDispatching(AccountV2Entity account, float amount) {
        log.info("[AccountDispatchingHandler.handleStandardDispatching] Adding {} to personal saving", amount);
        defaultPersonalSavingHandler.addToPersonalSaving(account.getId(), amount);
    }

    /**
     * Traite les commissions pour l'achat d'un kit d'adhésion.
     */
    private void processAdhesionKitCommission(
            AccountV2Entity account,
            AccountV2Entity sponsorParent,
            Optional<OrderItemV2Entity> orderItem,
            ConsoSubscriptionV2Entity subscription
    ) {
        float commissionAmount = calculateAdhesionKitCommission(orderItem);

        if (ConsoSubscriptionType.SOLIDARITY_FUND.value().equals(subscription.getType())) {
            double solidarityRate = configService.getDoubleValueOrDefault(
                    CONFIG_SOLIDARITY_FUND_RATE,
                    DEFAULT_SOLIDARITY_FUND_RATE
            );
            float amountToDispatch = (float) (commissionAmount * solidarityRate);
            int maxLevel = configService.getIntValueOrDefault(
                    CONFIG_MAX_NETWORK_LEVEL,
                    DEFAULT_MAX_NETWORK_LEVEL
            );

            log.info("[AccountDispatchingHandler.processAdhesionKitCommission] Solidarity fund: dispatching {} to network",
                    amountToDispatch);
            networkRewarDispatcher.dispatchToNetwork(account, amountToDispatch, maxLevel);
        } else {
            // Vérifier si le parent est un partenaire
            if (isPartnerAccount(sponsorParent)) {
                // Commission pour le parrain (partenaire)
                commissionService.addPartnerCommission(sponsorParent.getId(), account.getLogin(), commissionAmount);
            }

            // Commission pour l'adhérent (toujours ajoutée)
            float adherentCommission = calculateAdherentCommission(orderItem);
            commissionService.addCommission(account.getId(), adherentCommission);

            log.info("[AccountDispatchingHandler.processAdhesionKitCommission] Commissions distributed - Sponsor Parent: {}, Adherent: {}",
                    commissionAmount, adherentCommission);
        }
    }

    /**
     * Calcule la commission pour le kit d'adhésion (parrain).
     */
    private float calculateAdhesionKitCommission(Optional<OrderItemV2Entity> orderItem) {
        double commissionRate = configService.getDoubleValueOrDefault(
                CONFIG_ADHERANT_KIT_COMMISSION_RATE,
                DEFAULT_ADHERANT_KIT_COMMISSION_RATE
        );
        return orderItem
                .map(OrderItemV2Entity::getProduct)
                .map(product -> (float) (product.getPrice().doubleValue() * commissionRate))
                .orElse(0.0f);
    }

    /**
     * Calcule la commission pour l'adhérent.
     */
    private float calculateAdherentCommission(Optional<OrderItemV2Entity> orderItem) {
        double commissionRate = configService.getDoubleValueOrDefault(
                CONFIG_ADHERENT_COMMISSION_RATE,
                DEFAULT_ADHERENT_COMMISSION_RATE
        );
        return orderItem.map(OrderItemV2Entity::getProduct)
                .map(product -> (float) (product.getPrice().doubleValue() * commissionRate))
                .orElse(0.0f);
    }

    /**
     * Vérifie si le cashback est actif.
     */
    private boolean isCashbackActive() {
        return configService.getBooleanValueOrDefault(CONFIG_CASHBACK_ACTIVE, false);
    }

    /**
     * Vérifie si un compte est de type partenaire.
     */
    private boolean isPartnerAccount(AccountV2Entity account) {
        return Optional.ofNullable(account.getAccountType())
                .map(accountType -> PARTNER.equals(accountType.getId()))
                .orElse(false);
    }

    /**
     * Vérifie si un compte est le compte système.
     */
    private boolean isSystemAccount(AccountV2Entity account) {
        return Optional.ofNullable(account.getAccountType())
                .map(accountType -> SYSTEM_ACCOUNT_ID.equals(accountType.getId()))
                .orElse(false);
    }

    /**
     * Récupère le parent parrain via la table de parrainage.
     */
    private Optional<AccountV2Entity> getSponsorParent(AccountV2Entity account) {
        return sponsorshipRepository.getActiveSponsorByChildId(account.getId())
                .map(AccountSponsorshipEntity::getParent);
    }

    /**
     * Récupère les souscriptions actives d'un compte.
     */
    private List<ConsoSubscriptionV2Entity> getCurrentSubscriptions(CustomerV2Entity customer) {
        String customerId = Optional.ofNullable(customer)
                .map(CustomerV2Entity::getId)
                .orElse("NONE");
        return subscriptionRepository.getByCustomerId(customerId);
    }
}