package org.marketplace_lea.prometheus.domain.payment_method.repository;

import org.marketplace_lea.prometheus.domain.payment_method.entities.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethodEntity, Long>, JpaSpecificationExecutor<PaymentMethodEntity> {
    Optional<PaymentMethodEntity> findByProvider(String provider);

    List<PaymentMethodEntity> findAllByAvailableAndOnline(boolean available, boolean online);

    boolean existsByProvider(String provider);
}