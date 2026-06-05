package org.marketplace_lea.prometheus.domain.customer.services.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.common.utils.SecurityUtils;
import org.marketplace_lea.common.dtos.ResponseDTO;
import org.marketplace_lea.common.entities.account.AccountSponsorshipEntity;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Type;
import org.marketplace_lea.common.forms.UpdateCredentialForm;
import org.marketplace_lea.common.forms.UpdateCustomerForm;
import org.marketplace_lea.prometheus.domain.onboarding.forms.CustomerV2Mapper;
import org.marketplace_lea.common.repositories.WalletV2JpaRepository;
import org.marketplace_lea.common.repositories.account.AccountSponsorshipJpaRepository;
import org.marketplace_lea.common.repositories.account.AccountV2JpaRepository;
import org.marketplace_lea.common.repositories.customer.CustomerV2JpaRepository;
import org.marketplace_lea.prometheus.domain.customer.dto.ChildV2DTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerChildrenDTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerCpmDTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerProfileDTO;
import org.marketplace_lea.prometheus.domain.customer.dto.CustomerStatisticsDTO;
import org.marketplace_lea.prometheus.domain.customer.services.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Implémentation V2 du service de profil client.
 *
 * <p>
 * Remplace les fonctionnalités de
 * {@code CustomerFacade} et {@code ConsoStatisticCalculator}
 * du module {@code application} en s'appuyant sur les entités V2
 * ({@link CustomerV2Entity}, {@link AccountV2Entity},
 * {@link AccountSponsorshipEntity}).
 * </p>
 *
 * <p>
 * Le réseau de parrainage est modélisé via la table {@code ce_sponsorship}
 * ({@link AccountSponsorshipEntity}) qui relie un parent à ses enfants,
 * contrairement au modèle V1 où {@code Account} avait une relation directe
 * {@code @OneToMany children}.
 * </p>
 *
 * @see CustomerProfileService
 * @see AccountSponsorshipEntity
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCustomerProfileService implements CustomerProfileService {
    private final CustomerV2JpaRepository customerRepository;
    private final AccountV2JpaRepository accountRepository;
    private final AccountSponsorshipJpaRepository sponsorshipRepository;
    private final WalletV2JpaRepository walletRepository;
    private final CustomerV2Mapper customerV2Mapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Profondeur maximale de parcours du réseau de parrainage.
     * Correspond à la logique V1 qui limitait le BFS à 1 niveau.
     */
    private static final int MAX_NETWORK_DEPTH = 1;

    /**
     * Pourcentage de commission appliqué sur les points générés.
     */
    private static final float COMMISSION_RATE = 0.20f;

    // ─────────────────────────────────────────────
    // Consultation du profil
    // ─────────────────────────────────────────────

    @Override
    public CustomerProfileDTO getCurrentCustomerProfile() {
        String login = SecurityUtils.getConnectedCustomerLogin();
        log.debug("[DefaultCustomerProfileService.getCurrentCustomerProfile] Récupération du profil pour: {}", login);

        CustomerV2Entity customer = getCustomerByLoginOrThrow(login);

        return CustomerProfileDTO.builder()
                .id(customer.getId())
                .account(customerV2Mapper.toDTO(customer).getAccount())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumbers(customer.getPhoneNumbers())
                .address(customer.getAddress())
                .longitude(customer.getLongitude())
                .latitude(customer.getLatitude())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    // ─────────────────────────────────────────────
    // Réseau de parrainage (enfants / filleuls)
    // ─────────────────────────────────────────────

    @Override
    public CustomerChildrenDTO getChildren() {
        AccountV2Entity account = getConnectedAccount();
        Map<String, List<ChildV2DTO>> childrenByLevel = new TreeMap<>();
        List<AccountV2Entity> currentLevel = getDirectChildren(account.getId());

        if (currentLevel.isEmpty()) {
            return CustomerChildrenDTO.builder()
                    .totalChildrenCount(0)
                    .childrenByLevel(new TreeMap<>())
                    .build();
        }

        int totalCount = 0;
        int level = 1;

        while (!currentLevel.isEmpty() && level <= MAX_NETWORK_DEPTH) {
            childrenByLevel.put(
                    String.format("level%d", level),
                    currentLevel.stream().map(this::convertToChildDTO).toList()
            );
            totalCount += currentLevel.size();

            if (level < MAX_NETWORK_DEPTH) {
                currentLevel = fetchChildrenBatch(currentLevel);
            } else {
                currentLevel = List.of();
            }
            level++;
        }

        return CustomerChildrenDTO.builder()
                .totalChildrenCount(totalCount)
                .childrenByLevel(childrenByLevel)
                .build();
    }

    @Override
    public Map<String, CustomerStatisticsDTO> getStatistics() {
        AccountV2Entity account = getConnectedAccount();
        Map<String, CustomerStatisticsDTO> stats = new TreeMap<>();

        List<AccountV2Entity> currentLevel = getDirectChildren(account.getId());
        if (currentLevel.isEmpty()) {
            return stats;
        }

        int level = 1;
        while (!currentLevel.isEmpty() && level <= MAX_NETWORK_DEPTH) {
            stats.put(String.format("level%d", level), computeStatisticsForLevel(currentLevel));

            if (level < MAX_NETWORK_DEPTH) {
                currentLevel = fetchChildrenBatch(currentLevel);
            } else {
                currentLevel = List.of();
            }
            level++;
        }

        return stats;
    }

    // ─────────────────────────────────────────────
    // CPM (Consommation Par Mois)
    // ─────────────────────────────────────────────

    @Override
    public CustomerCpmDTO getCpm() {
        AccountV2Entity account = getConnectedAccount();
        double cpm = computeCpm(account);
        return new CustomerCpmDTO(cpm);
    }

    // ─────────────────────────────────────────────
    // Mise à jour du profil
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public ResponseDTO updateCustomerInfo(UpdateCustomerForm form) {
        String login = SecurityUtils.getConnectedCustomerLogin();
        CustomerV2Entity customer = getCustomerByLoginOrThrow(login);

        if (form.firstName() != null && !form.firstName().isBlank()) {
            customer.setFirstName(form.firstName());
        }
        if (form.lastName() != null && !form.lastName().isBlank()) {
            customer.setLastName(form.lastName());
        }
        if (form.email() != null && !form.email().isBlank()) {
            customer.setEmail(form.email());
        }

        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);

        log.info("[DefaultCustomerProfileService.updateCustomerInfo] Profil mis à jour pour: {}", login);
        return new ResponseDTO("Modification effectuée avec succès !");
    }

    @Override
    @Transactional
    public ResponseDTO updateCredential(UpdateCredentialForm form) {
        AccountV2Entity account = getConnectedAccount();

        if (!passwordEncoder.matches(form.oldPassword(), account.getPassword())) {
            throw new ConsoEpargneException("L'ancien mot de passe est incorrect.", HttpStatus.BAD_REQUEST);
        }

        account.setPassword(passwordEncoder.encode(form.newPassword()));
        accountRepository.save(account);

        log.info("[DefaultCustomerProfileService.updateCredential] Mot de passe mis à jour pour: {}",
                account.getLogin());
        return new ResponseDTO("Mot de passe mis à jour avec succès !");
    }

    // ─────────────────────────────────────────────
    // Suppression du compte
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public ResponseDTO dismissAccount() {
        String login = SecurityUtils.getConnectedCustomerLogin();
        CustomerV2Entity customer = getCustomerByLoginOrThrow(login);

        customer.setDeletedAt(LocalDateTime.now());
        customer.getAccount().setBlacklisted(true);
        customerRepository.save(customer);

        log.info("[DefaultCustomerProfileService.dismissAccount] Compte supprimé (soft-delete) pour: {}", login);
        return new ResponseDTO("Compte supprimé avec succès !");
    }

    // ═════════════════════════════════════════════
    // Méthodes privées
    // ═════════════════════════════════════════════

    /**
     * Récupère le client V2 par login ou lève une exception.
     */
    private CustomerV2Entity getCustomerByLoginOrThrow(String login) {
        return customerRepository.getByLogin(login)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException(
                        "Aucun client trouvé pour le login '" + login + "'."));
    }

    /**
     * Récupère le compte V2 du client actuellement connecté.
     */
    private AccountV2Entity getConnectedAccount() {
        String login = SecurityUtils.getConnectedCustomerLogin();
        return accountRepository.getByActiveLogin(login)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException(
                        "Aucun compte actif trouvé pour le login '" + login + "'."));
    }

    /**
     * Récupère les enfants directs d'un compte via la table de parrainage.
     *
     * @param parentAccountId identifiant du compte parent
     * @return liste des comptes enfants
     */
    private List<AccountV2Entity> getDirectChildren(String parentAccountId) {
        return sponsorshipRepository.findActiveByParentId(parentAccountId)
                .stream()
                .map(AccountSponsorshipEntity::getChild)
                .toList();
    }

    /**
     * Récupère les enfants de plusieurs comptes parents en une seule requête SQL.
     *
     * <p>
     * Élimine le problème N+1 du BFS : au lieu de N appels à
     * {@link #getDirectChildren(String)}, un seul appel batch via
     * {@link AccountSponsorshipJpaRepository#findActiveByParentIds(List)}.
     * </p>
     *
     * @param parents comptes parents du niveau courant
     * @return comptes enfants du niveau suivant
     */
    private List<AccountV2Entity> fetchChildrenBatch(List<AccountV2Entity> parents) {
        List<String> parentIds = parents.stream()
                .map(AccountV2Entity::getId)
                .toList();
        return sponsorshipRepository.findActiveByParentIds(parentIds)
                .stream()
                .map(AccountSponsorshipEntity::getChild)
                .toList();
    }

    /**
     * Convertit un compte V2 en DTO de filleul.
     */
    private ChildV2DTO convertToChildDTO(AccountV2Entity childAccount) {
        Optional<CustomerV2Entity> customerOpt = customerRepository.getByLogin(childAccount.getLogin());

        return customerOpt.map(customer -> ChildV2DTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumbers(customer.getPhoneNumbers())
                .partner(childAccount.bePartner())
                .build()).orElse(ChildV2DTO.builder()
                        .id(childAccount.getId())
                        .firstName("Conso")
                        .lastName(childAccount.bePartner() ? "Partenaire" : "Consom'acteur")
                        .partner(childAccount.bePartner())
                        .build());
    }

    /**
     * Calcule les statistiques pour un niveau donné.
     *
     * <p>
     * Utilise une requête batch pour récupérer les wallets de tous
     * les comptes du niveau en une seule requête SQL.
     * </p>
     */
    private CustomerStatisticsDTO computeStatisticsForLevel(List<AccountV2Entity> accounts) {
        List<String> accountIds = accounts.stream()
                .map(AccountV2Entity::getId)
                .toList();

        Map<String, BigDecimal> savingsByAccountId = walletRepository
                .findByAccountIdInAndType(accountIds, WalletV2Type.PERSONAL)
                .stream()
                .collect(Collectors.toMap(
                        w -> w.getAccount().getId(),
                        WalletV2Entity::getNetworkSavingAmount,
                        BigDecimal::add
                ));

        float generatedPoints = savingsByAccountId.values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .floatValue();

        int partnerCount = (int) accounts.stream()
                .filter(AccountV2Entity::bePartner)
                .count();
        int customerCount = accounts.size() - partnerCount;

        return CustomerStatisticsDTO.builder()
                .generatedPoints(generatedPoints)
                .commissionRate(COMMISSION_RATE)
                .commissions(generatedPoints * COMMISSION_RATE)
                .totalUsersCount(accounts.size())
                .customerCount(customerCount)
                .partnerCount(partnerCount)
                .build();
    }

    /**
     * Calcule le CPM (Consommation Par Mois) pour un compte V2.
     *
     * <p>
     * Parcourt le réseau via la table de parrainage et somme les
     * épargnes personnelles en une seule requête batch.
     * </p>
     */
    private double computeCpm(AccountV2Entity account) {
        List<AccountV2Entity> allNetworkAccounts = new ArrayList<>();
        List<AccountV2Entity> currentLevel = getDirectChildren(account.getId());

        int level = 1;

        while (!currentLevel.isEmpty() && level <= MAX_NETWORK_DEPTH) {
            allNetworkAccounts.addAll(currentLevel);

            if (level < MAX_NETWORK_DEPTH) {
                currentLevel = fetchChildrenBatch(currentLevel);
            } else {
                currentLevel = List.of();
            }
            level++;
        }

        if (allNetworkAccounts.isEmpty()) {
            return 0.0;
        }

        List<String> ids = allNetworkAccounts.stream()
                .map(AccountV2Entity::getId)
                .toList();

        return walletRepository.findByAccountIdInAndType(ids, WalletV2Type.PERSONAL)
                .stream()
                .map(WalletV2Entity::getPersonalSavingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue() * COMMISSION_RATE;
    }
}
