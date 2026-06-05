package org.marketplace_lea.common.repositories.order;

import org.marketplace_lea.common.entities.order.OrderItemV2Entity;
import org.marketplace_lea.common.entities.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderItemV2Repository extends JpaRepository<OrderItemV2Entity, Long>, JpaSpecificationExecutor<OrderItemV2Entity> {
    @Query("SELECT COALESCE(SUM(p.savingAmount * oi.quantity), 0) FROM OrderV2 o JOIN o.orderItems oi JOIN oi.product p WHERE o.id = :orderId AND o.status = :status")
    BigDecimal sumTotalSavingAmountByOrderId(@Param("orderId") String orderId, @Param("status") OrderStatus status);

    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END " +
            "FROM OrderItemV2 oi " +
            "WHERE oi.order.id = :orderId " +
            "AND oi.product.shelf.parent.id = :shelfId " +
            "AND oi.order.status = :status")
    boolean hasOrderContainShelfProducts(@Param("orderId") String orderId, @Param("shelfId") String shelfId, @Param("status") OrderStatus status);

    @Query("SELECT oi " +
            "FROM OrderItemV2 oi " +
            "WHERE oi.order.id = :orderId " +
            "AND oi.product.shelf.id IN :shelfIds " +
            "AND oi.order.status = :status")
    List<OrderItemV2Entity> getOrderForAdherantKits(@Param("orderId") String orderId, @Param("shelfIds") List<String> shelfIds, @Param("status") OrderStatus status);

    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END " +
            "FROM OrderItemV2 oi " +
            "INNER JOIN ProductV2 p " +
            "WHERE oi.order.customer.id = :customerId " +
            "AND p.shelf.id = :shelfId " +
            "AND oi.order.status = :status")
    boolean hasAnyOrderContainShelfProducts(@Param("customerId") String customerId, @Param("shelfId") String shelfId, @Param("status") OrderStatus status);


    /**
     * Calcule la somme totale des points (comissionAccountAmount) de tous les produits
     * dans une commande spécifique
     *
     * @param orderId ID de la commande
     * @param status Statut de la commande (optionnel, peut être null pour tous les statuts)
     * @return Somme totale des points de tous les produits dans la commande
     */
    @Query("SELECT COALESCE(SUM(oi.product.comissionAccountAmount * oi.quantity), 0) " +
            "FROM OrderItemV2 oi " +
            "WHERE oi.order.id = :orderId " +
            "AND (:status IS NULL OR oi.order.status = :status)")
    BigDecimal sumTotalCommissionPointsByOrder(@Param("orderId") String orderId, @Param("status") OrderStatus status);
}