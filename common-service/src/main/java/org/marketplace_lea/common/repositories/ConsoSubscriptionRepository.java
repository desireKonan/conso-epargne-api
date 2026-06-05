package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.subscription.ConsoSubscriptionV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsoSubscriptionRepository extends JpaRepository<ConsoSubscriptionV2Entity, String> {
    @Query("SELECT subs from Subscription subs WHERE subs.customer.id = :customerId ORDER BY createdAt DESC")
    List<ConsoSubscriptionV2Entity> getByCustomerId(@Param("customerId") String customerId);
}
