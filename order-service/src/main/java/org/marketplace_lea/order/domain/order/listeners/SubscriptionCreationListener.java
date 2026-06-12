package org.marketplace_lea.order.domain.order.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.entities.subscription.ConsoSubscriptionV2Entity;
import org.marketplace_lea.common.repositories.ConsoSubscriptionRepository;
import org.marketplace_lea.order.common.entities.order.OrderItemV2Entity;
import org.marketplace_lea.order.domain.order.events.OrderSubscriptionValidatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionCreationListener {
    private final ConsoSubscriptionRepository consoSubscriptionRepository;

    @EventListener
    @Async("eventTaskExecutor")
    public void handleSubscriptionCreation(OrderSubscriptionValidatedEvent event) {
        List<ConsoSubscriptionV2Entity> currentSubscriptions = consoSubscriptionRepository.getByCustomerId(event.getCustomerId());

        if (!currentSubscriptions.isEmpty()) {
            log.info("[ORDER_SERVICE][CREATE_SUBSCRIPTION] Vous avez déjà une souscription !");
            return;
        }

        /// Si on a acheté
        /// On va créer la souscription en fonction des informations de la commande.
        var adherantKit = event.getOrderItem();
        log.info("[ORDER_SERVICE][CREATE_SUBSCRIPTION] Get Adherant Kit: {}", adherantKit.getId());
        ConsoSubscriptionV2Entity subscription = buildConsoSubscription(adherantKit, event.getCustomerId(), LocalDateTime.now());
        ConsoSubscriptionV2Entity subscriptioncreated = consoSubscriptionRepository.save(subscription);
        log.info("[ORDER_SERVICE][CREATE_SUBSCRIPTION] La souscription {} a bien été effectuée !", subscriptioncreated.getId());
    }


    private ConsoSubscriptionV2Entity buildConsoSubscription(OrderItemV2Entity orderItem, String customerId, LocalDateTime now) {
        var subscription = ConsoSubscriptionV2Entity.builder()
                .id(GeneratorUtils.generateSubscriptionId())
                .customer(CustomerV2Entity.builder()
                        .id(customerId)
                        .build())
                .startDate(now)
                .endDate(now.plusMonths(12))
                .createdAt(now)
                .build();
        subscription.setType(orderItem.getLabel());
        subscription.setAmount(orderItem.getPrice());
        return subscription;
    }
}
