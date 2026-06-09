package org.marketplace_lea.order.common.repository.order;

import org.marketplace_lea.order.common.entities.order.CartItemV2Entity;
import org.marketplace_lea.order.common.entities.order.CartType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemV2JpaRepository extends JpaRepository<CartItemV2Entity, Long>, JpaSpecificationExecutor<CartItemV2Entity> {
    @Query("SELECT c FROM CartItemV2Entity c WHERE c.customer.id = :customerId")
    List<CartItemV2Entity> getAllByCustomerId(@Param("customerId") String customerId);

    @Query("SELECT COUNT(c) > 0 FROM CartItemV2Entity c WHERE c.product.id = :productId AND c.customer.id = :customerId")
    boolean hasCart(@Param("productId") String productId, @Param("customerId") String customerId);

    @Query("SELECT COUNT(c) > 0 FROM CartItemV2Entity c WHERE c.product.id = :productId AND c.customer.id = :customerId AND c.product.soldOut = true")
    boolean hasCartProductSoldout(@Param("productId") String productId, @Param("customerId") String customerId);

    @Modifying
    @Query("DELETE FROM CartItemV2Entity c WHERE c.customer.id = :customerId")
    void deleteByCustomerId(@Param("customerId") String customerId);

    @Modifying
    @Query("DELETE FROM CartItemV2Entity c WHERE c.product.id = :id")
    void deleteByProductId(@Param("id") String id);

    @Modifying
    @Query("DELETE FROM CartItemV2Entity c WHERE c.product.id = :id AND c.cartType = :cart_type")
    void deleteByProductIdAndCartType(@Param("id") String id, @Param("cart_type") CartType cartType);
}
