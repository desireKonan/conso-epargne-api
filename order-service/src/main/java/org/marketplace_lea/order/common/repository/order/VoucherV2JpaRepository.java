package org.marketplace_lea.order.common.repository.order;

import org.marketplace_lea.common.entities.VoucherV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoucherV2JpaRepository extends JpaRepository<VoucherV2Entity, String> {
    @Query("SELECT v FROM Voucher v WHERE v.account.login = :login AND v.valid = :is_valid")
    List<VoucherV2Entity> getAllByLogin(@Param("login") String login, @Param("is_valid") boolean validated);

    @Query("SELECT v FROM Voucher v WHERE v.valid = true AND v.account.id = :customer_id")
    Optional<VoucherV2Entity> getValidVoucherByCustomerId(@Param("customer_id") String customerId);
}