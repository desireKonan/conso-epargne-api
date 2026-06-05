package org.marketplace_lea.common.repositories.account;

import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountV2JpaRepository extends JpaRepository<AccountV2Entity, String>, JpaSpecificationExecutor<AccountV2Entity> {
    @Query("SELECT acc from AccountV2 acc WHERE acc.affiliationCode = :code")
    Optional<AccountV2Entity> getByAffiliationCode(@Param("code") String code);

    @Query("SELECT acc from AccountV2 acc WHERE acc.login = :login")
    Optional<AccountV2Entity> findAccountV2ByLogin(@Param("login") String login);

    @Query("SELECT a FROM AccountV2 a WHERE a.login = :login AND a.countryCode = :countryCode AND a.deletedAt is NOT NULL")
    Optional<AccountV2Entity> getByLoginAndCountryCode(String login, String countryCode);

    @Query("SELECT a FROM AccountV2 a WHERE a.login = :login AND a.deletedAt is NULL")
    Optional<AccountV2Entity> getByActiveLogin(@Param("login") String login);

    @Query("SELECT COUNT(a) > 0 FROM AccountV2 a WHERE a.login = :login AND a.countryCode = :countryCode")
    boolean existsByLoginAndCode(@Param("login") String login, @Param("countryCode") String countryCode);
}