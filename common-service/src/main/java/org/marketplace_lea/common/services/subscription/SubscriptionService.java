package org.marketplace_lea.common.services.subscription;

import org.marketplace_lea.common.dtos.ConsoSubscriptionDTO;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    Optional<ConsoSubscriptionDTO> findById(String id);

    List<ConsoSubscriptionDTO> findByCustomerId(String customerId);

    Optional<ConsoSubscriptionDTO> findFirstByCustomerId(String customerId);

    List<ConsoSubscriptionDTO> findAll();

    ConsoSubscriptionDTO save(ConsoSubscriptionDTO subscriptionDTO);

    ConsoSubscriptionDTO update(String id, ConsoSubscriptionDTO subscriptionDTO);

    void delete(String id);

    boolean existsById(String id);

    List<ConsoSubscriptionDTO> getCurrentSubscription(String customerId);
}
