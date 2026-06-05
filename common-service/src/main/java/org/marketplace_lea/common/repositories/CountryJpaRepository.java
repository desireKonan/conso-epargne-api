package org.marketplace_lea.common.repositories;

import org.marketplace_lea.common.entities.CountryV2Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryJpaRepository extends JpaRepository<CountryV2Entity, Long> {
    @Query("SELECT c FROM Country c where c.code =:code")
    Optional<CountryV2Entity> getByCode(@Param("code") String code);

    @Query("SELECT c FROM Country c where c.enabled =:enabled")
    List<CountryV2Entity> getCountries(@Param("enabled") boolean enabled);

    @Query("SELECT COUNT(c) FROM Country c")
    long countCountries();
}