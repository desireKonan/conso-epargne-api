package org.marketplace_lea.common.repositories.account;

import org.marketplace_lea.common.entities.account.AccountSponsorshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountSponsorshipJpaRepository extends JpaRepository<AccountSponsorshipEntity, String> {

    @Query(value = "SELECT sponsorship FROM AccountSponsorship sponsorship JOIN FETCH sponsorship.parent JOIN FETCH sponsorship.child WHERE sponsorship.parent.id = :parentId AND sponsorship.child.id = :childId")
    Optional<AccountSponsorshipEntity> getByRelationship(
            @Param("parentId") String parent,
            @Param("childId") String child
    );

    /**
     * Récupère tous les parrainages actifs (non résiliés) pour un parent donné.
     *
     * @param parentId identifiant du compte parent
     * @return liste des parrainages avec les comptes enfants chargés
     */
    @Query("SELECT s FROM AccountSponsorship s JOIN FETCH s.child WHERE s.parent.id = :parentId AND s.resignedAt IS NULL")
    List<AccountSponsorshipEntity> getActiveByParentId(@Param("parentId") String parentId);


    /**
     * Récupère tous les parrainages actifs (non résiliés) pour un parent donné.
     *
     * @param childId identifiant du compte parent
     * @return liste des parrainages avec les comptes enfants chargés
     */
    @Query("SELECT s FROM AccountSponsorship s JOIN FETCH s.parent WHERE s.child.id = :childId AND s.resignedAt IS NULL")
    Optional<AccountSponsorshipEntity> getActiveSponsorByChildId(@Param("childId") String childId);

    /**
     * Récupère tous les parrainages actifs pour une liste de parents (batch).
     *
     * <p>
     * Permet de récupérer les enfants de plusieurs comptes parents
     * en une seule requête SQL, éliminant le problème N+1
     * lors du parcours BFS du réseau.
     * </p>
     *
     * @param parentIds identifiants des comptes parents
     * @return liste des parrainages avec les comptes enfants chargés
     */
    @Query("SELECT s FROM AccountSponsorship s JOIN FETCH s.child WHERE s.parent.id IN :parentIds AND s.resignedAt IS NULL")
    List<AccountSponsorshipEntity> findActiveByParentIds(@Param("parentIds") List<String> parentIds);
}
