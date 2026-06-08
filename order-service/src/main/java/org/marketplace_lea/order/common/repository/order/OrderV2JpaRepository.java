package org.marketplace_lea.order.common.repository.order;

import org.marketplace_lea.order.common.entities.order.OrderStatus;
import org.marketplace_lea.order.common.entities.order.OrderV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderV2JpaRepository extends JpaRepository<OrderV2Entity, String>, JpaSpecificationExecutor<OrderV2Entity> {
    @Query("SELECT o FROM OrderV2 o JOIN FETCH o.orderItems items JOIN o.customer c WHERE c.account.login = :login ORDER BY o.createdAt DESC")
    List<OrderV2Entity> getByCustomer(@Param("login") String login);

    @Query("SELECT o FROM OrderV2 o JOIN FETCH o.orderItems items ORDER BY o.createdAt DESC")
    List<OrderV2Entity> getAll();

    @Query("SELECT o FROM OrderV2 o JOIN FETCH o.orderItems items WHERE o.status = :status ORDER BY o.createdAt DESC")
    List<OrderV2Entity> getAll(@Param("status") OrderStatus status);

    @Query("SELECT COUNT(o.id) FROM OrderV2 o WHERE o.status = :status")
    long countByStatus(@Param("status") OrderStatus status);

    @Query("SELECT o FROM OrderV2 o JOIN o.customer c JOIN FETCH o.orderItems items WHERE o.id = :id AND c.id = :customerId")
    Optional<OrderV2Entity> getByIdAndCustomerId(@Param("id") String id, @Param("customerId") String customerId);
}