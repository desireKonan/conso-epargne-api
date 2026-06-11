package org.marketplace_lea.order.domain.order.services.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.order.common.entities.order.OrderStatus;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;
import org.marketplace_lea.order.common.repository.order.OrderV2JpaRepository;
import org.marketplace_lea.order.domain.order.dto.OrderCreationDTO;
import org.marketplace_lea.order.domain.order.form.OrderV2SearchForm;
import org.marketplace_lea.order.domain.order.mapper.OrderV2Mapper;
import org.marketplace_lea.order.domain.order.services.OrderV2Service;
import org.marketplace_lea.order.domain.order.services.specifications.OrderV2Specification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultOrderV2Service implements OrderV2Service {
    private final OrderV2JpaRepository orderRepository;
    private final OrderV2Mapper orderV2Mapper;

    private OrderV2Entity getEntityById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Commande introuvable: " + id));
    }

    @Override
    public Page<OrderCreationDTO> getAll(OrderV2SearchForm filterCriteria, Pageable pageable) {
        var spec = OrderV2Specification.buildSpecification(filterCriteria);
        return orderRepository.findAll(spec, pageable)
                .map(orderV2Mapper::toDTO);
    }

    @Override
    public OrderCreationDTO getById(String id) {
        return findById(id)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Commande introuvable: " + id));
    }

    @Override
    public Optional<OrderCreationDTO> findById(String id) {
        return orderRepository.findById(id)
                .map(orderV2Mapper::toDTO);
    }

    @Override
    @Transactional
    public void delete(String id) {
        log.info("[DefaultOrderV2Service.delete] Suppression de la commande: {}", id);
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void changeOrderStatus(String id, String status) {
        log.info("[DefaultOrderV2Service.changeOrderStatus] Changement de statut de la commande: {} à {}", id, status);
        OrderV2Entity order = getEntityById(id);

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);

            if (newStatus == OrderStatus.VALIDATED) {
                order.setValidatedAt(LocalDateTime.now());
            } else if (newStatus == OrderStatus.CANCELED) {
                order.setCanceledAt(LocalDateTime.now());
            }

            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide: " + status);
        }
    }

    @Override
    @Transactional
    public void validateOrder(String id) {
        log.info("[DefaultOrderV2Service.validateOrder] Validation de la commande: {}", id);
        OrderV2Entity order = getEntityById(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Seule une commande en attente peut être validée");
        }

        order.setStatus(OrderStatus.VALIDATED);
        order.setValidatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(String id) {
        log.info("[DefaultOrderV2Service.cancelOrder] Annulation de la commande: {}", id);
        OrderV2Entity order = getEntityById(id);

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Seule une commande en attente peut être annulée");
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setCanceledAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
