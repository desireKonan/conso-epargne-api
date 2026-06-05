package org.marketplace_lea.common.services.subscription.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.dtos.ConsoSubscriptionDTO;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.entities.subscription.ConsoSubscriptionV2Entity;
import org.marketplace_lea.common.mapper.ConsoSubscriptionMapper;
import org.marketplace_lea.common.repositories.ConsoSubscriptionRepository;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.repositories.customer.CustomerV2JpaRepository;
import org.marketplace_lea.common.services.subscription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final ConsoSubscriptionRepository subscriptionRepository;
    private final CustomerV2JpaRepository customerRepository;
    private final ConsoSubscriptionMapper subscriptionMapper;

    @Override
    public Optional<ConsoSubscriptionDTO> findById(String id) {
        log.debug("Recherche de la souscription avec l'ID: {}", id);
        return subscriptionRepository.findById(id)
                .map(subscriptionMapper::toDTO);
    }

    @Override
    public List<ConsoSubscriptionDTO> findByCustomerId(String customerId) {
        log.debug("Recherche des souscriptions pour le client ID: {}", customerId);
        return subscriptionRepository.getByCustomerId(customerId)
                .stream()
                .map(subscriptionMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<ConsoSubscriptionDTO> findFirstByCustomerId(String customerId) {
        return findByCustomerId(customerId)
                .stream()
                .findFirst();
    }

    @Override
    public List<ConsoSubscriptionDTO> findAll() {
        log.debug("Récupération de toutes les souscriptions");
        return subscriptionRepository.findAll()
                .stream()
                .map(subscriptionMapper::toDTO)
                .toList();
    }

    @Override
    public ConsoSubscriptionDTO save(ConsoSubscriptionDTO subscriptionDTO) {
        log.debug("Création d'une nouvelle souscription: {}", subscriptionDTO);
        ConsoSubscriptionV2Entity subscription = subscriptionMapper.toEntity(subscriptionDTO);
        subscription.setId(GeneratorUtils.generateSubscriptionId());
        subscription.setCreatedAt(LocalDateTime.now());


        // Associer le client si customerId est fourni
        if (subscriptionDTO.getCustomerId() != null) {
            CustomerV2Entity customer = customerRepository.findById(subscriptionDTO.getCustomerId())
                    .orElseThrow(() -> new ConsoEpargneException("Client non trouvé avec l'ID: " + subscriptionDTO.getCustomerId()));
            subscription.setCustomer(customer);
        }

        ConsoSubscriptionV2Entity savedSubscription = subscriptionRepository.save(subscription);
        log.info("Souscription créée avec l'ID: {}", savedSubscription.getId());
        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public ConsoSubscriptionDTO update(String id, ConsoSubscriptionDTO subscriptionDTO) {
        log.debug("Mise à jour de la souscription ID: {}", id);
        ConsoSubscriptionV2Entity existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Souscription non trouvée avec l'ID: " + id));

        // Mettre à jour les champs modifiables
        existingSubscription.setType(subscriptionDTO.getType());
        existingSubscription.setAmount(subscriptionDTO.getAmount());
        existingSubscription.setStartDate(subscriptionDTO.getStartDate());
        existingSubscription.setEndDate(subscriptionDTO.getEndDate());
        existingSubscription.setUpdatedAt(LocalDateTime.now());

        ConsoSubscriptionV2Entity updatedSubscription = subscriptionRepository.save(existingSubscription);
        log.info("Souscription ID: {} mise à jour", id);

        return subscriptionMapper.toDTO(updatedSubscription);
    }

    @Override
    public void delete(String id) {
        log.debug("Suppression (soft delete) de la souscription ID: {}", id);

        if (!subscriptionRepository.existsById(id)) {
            throw new ConsoEpargneNotFoundDataException("Souscription non trouvée avec l'ID: " + id);
        }

        subscriptionRepository.deleteById(id);
        log.info("Souscription ID: {} marquée comme supprimée", id);
    }

    @Override
    public boolean existsById(String id) {
        return subscriptionRepository.existsById(id);
    }


    public List<ConsoSubscriptionDTO> getCurrentSubscription(String customerId) {
        return subscriptionRepository.getByCustomerId(customerId)
                .stream()
                .map(subscriptionMapper::toDTO)
                .toList();
    }
}
