package org.marketplace_lea.common.repositories.customer;

import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerV2JpaRepository extends JpaRepository<CustomerV2Entity, String>, JpaSpecificationExecutor<CustomerV2Entity> {
    @Query("SELECT c FROM CustomerV2 c JOIN c.account a WHERE a.login = :login")
    Optional<CustomerV2Entity> getByLogin(@Param("login") String login);

    @Query("SELECT c FROM CustomerV2 c JOIN c.account a WHERE a.accountType.id = :id")
    List<CustomerV2Entity> getAllByAccountTypeId(@Param("id") String id);

    @Query("SELECT COUNT(c) FROM CustomerV2 c JOIN c.account a JOIN a.accountType at WHERE at.id = :id")
    long countByAccountTypeId(@Param("id") String id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CustomerV2 c JOIN c.account a WHERE a.login = :login")
    boolean existsForLogin(@Param("login") String login);

    @Query("SELECT COUNT(c) > 0 FROM CustomerV2 c JOIN c.account a WHERE a.affiliationCode = :code")
    boolean existsForAffiliationCode(@Param("code") String code);
}