package org.marketplace_lea.order.common.repository.order;

import org.marketplace_lea.order.common.entities.order.CartItemV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemV2JpaRepository extends JpaRepository<CartItemV2Entity, Long>, JpaSpecificationExecutor<CartItemV2Entity> {
    @Query("SELECT c FROM CartItemV2Entity c WHERE c.customerAccount.login = :login")
    List<CartItemV2Entity> getAllBYLogin(@Param("login") String login);

    @Query("SELECT COUNT(c) > 0 FROM CartItemV2Entity c WHERE c.product.id = :productId AND c.customerAccount.login = :login")
    boolean hasCart(@Param("productId") String productId, @Param("login") String login);

    @Modifying
    @Query("DELETE FROM CartItemV2Entity c WHERE c.customerAccount.login = :login")
    void deleteByLogin(@Param("login") String login);

    @Modifying
    @Query("DELETE FROM CartItemV2Entity c WHERE c.product.id = :id")
    void deleteByProductId(@Param("id") String id);
}
