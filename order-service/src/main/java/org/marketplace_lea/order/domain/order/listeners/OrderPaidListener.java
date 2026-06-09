package org.marketplace_lea.order.domain.order.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.client.wane_delivery.form.DeliveryRequestForm;
import org.marketplace_lea.common.client.wane_delivery.service.WaneDeliveryService;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;
import org.marketplace_lea.order.domain.order.events.OrderPaidEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidListener {
    private final WaneDeliveryService waneDeliveryService;

    @EventListener
    @Async("eventTaskExecutor")
    public void handleDispatchingOrderOperation(OrderPaidEvent event) {
        try {
            log.info("[OrderPaidListener][handleDispatchingOrderOperation] Processing dispatch order operation for Id: {} at {}", event.getId(), event.getTimestamp());
            var order = event.getOrder();
            if (order.isPending() || order.isValidated()) {
                DeliveryRequestForm deliveryForm = buildDeliveryRequestForm(order, event.getCustomer());
                // Générer un correlation ID pour le tracking
                String correlationId = GeneratorUtils.getCurrentCorrelationId();
                var response = waneDeliveryService.createRequest(deliveryForm, correlationId);
                log.info("[OrderPaidListener][handleDispatchingOrderOperation] End processing dispatch order operation for Id: {} at {} - response:  {}", event.getId(), LocalDateTime.now(), response.message());
            }
        } catch (Exception ex) {
            log.error("[OrderPaidListener][handleDispatchingOrderOperation] Error while processing dispatch order operation for event Id: {} - message: {}", event.getId(), ex.getMessage(), ex);
        }
    }

    private DeliveryRequestForm buildDeliveryRequestForm(OrderV2Entity order, CustomerV2Entity customer) {
        DeliveryRequestForm form = new DeliveryRequestForm();

        // Utiliser les informations de livraison de la commande
        // Note: Vous devrez adapter ces valeurs selon votre modèle de données
        form.setPickupLat(0.0);
        form.setPickupLng(0.0);
        form.setPickupAddress("Addresse de départ");

        form.setDropLat(customer.getLatitude().doubleValue()); // À adapter
        form.setDropLng(customer.getLongitude().doubleValue()); // À adapter
        form.setDropAddress(customer.getAddress());

        // Informations client
        form.setCustomerName(customer._fullname());
        form.setCustomerPhone(customer.login());
        form.setCustomerEmail(customer.getEmail());

        // Informations sur les marchandises
        form.setGoodsTypeId(1); // À adapter selon votre catalogue
        form.setGoodsTypeQuantity("Commande ConsoEpargne;");

        // Mode de paiement (PARTNER_BILLING pour paiement déjà effectué)
        form.setPaymentMode("PARTNER_BILLING");
        form.setPartnerReference(order.getId()); // Référence de la commande

        // URL de callback pour les mises à jour de statut
        form.setCallbackUrl("https://votre-api.com/api/v1/delivery/callback/" + order.getId());

        // vehicleType sera défini par WaneDeliveryService
        return form;
    }
}
