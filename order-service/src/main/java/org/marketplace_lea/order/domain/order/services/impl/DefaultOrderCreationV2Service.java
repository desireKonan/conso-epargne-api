package org.marketplace_lea.order.domain.order.services.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;
import org.marketplace_lea.order.common.repository.order.OrderV2JpaRepository;
import org.marketplace_lea.order.domain.order.dto.CreateOrderV2Form;
import org.marketplace_lea.order.domain.order.dto.OrderV2DTO;
import org.marketplace_lea.order.domain.order.dto.UpdateOrderV2Form;
import org.marketplace_lea.order.domain.order.events.OrderV2EventPublisher;
import org.marketplace_lea.order.domain.order.mapper.OrderV2Mapper;
import org.marketplace_lea.order.domain.order.services.OrderCreationHandler;
import org.marketplace_lea.order.domain.order.services.OrderCreationV2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultOrderCreationV2Service implements OrderCreationV2Service {
    private final OrderV2JpaRepository orderRepository;
    private final OrderV2Mapper orderV2Mapper;
    private final OrderCreationHandler orderCreationHandler;

    @Override
    public OrderV2DTO create(CreateOrderV2Form createDTO) {
        log.info("[DefaultOrderCreationV2Service.create] Delegating to OrderCreationHandler for customer: {}", createDTO.customerId());
        return orderCreationHandler.handleOrderCreation(createDTO);
    }

    @Override
    @Transactional
    public OrderV2DTO update(String id, UpdateOrderV2Form updateDTO) {
        log.info("[DefaultOrderCreationV2Service.update] Mise à jour de la commande: {}", id);
        OrderV2Entity order = orderRepository.findById(id)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Commande introuvable: " + id));

        orderV2Mapper.mapToEntity(updateDTO, order);

        var orderSaved = orderRepository.save(order);
        log.info("[DefaultOrderCreationV2Service.update] Mise à jour de la commande: {}", orderSaved.getId());
        return orderV2Mapper.toDTO(orderSaved);
    }
}
