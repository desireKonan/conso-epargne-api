package org.marketplace_lea.common.repositories.account;

import org.marketplace_lea.common.entities.account.AccountTypeV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTypeV2JpaRepository extends JpaRepository<AccountTypeV2Entity, String> {
    @Query(value = "SELECT accT FROM AccountTypeV2 accT WHERE accT.id = :reference")
    List<AccountTypeV2Entity> getAllById(@Param("reference") String reference);

    @Query(value = "SELECT accT FROM AccountTypeV2 accT WHERE accT.id != :reference")
    List<AccountTypeV2Entity> getAllByNotId(@Param("reference") String reference);
}