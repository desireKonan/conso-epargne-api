package org.marketplace_lea.prometheus.domain.onboarding.services.impl;

import org.marketplace_lea.common.common.service.notification.SmsService;
import org.marketplace_lea.common.common.utils.GeneratorUtils;
import org.marketplace_lea.common.entities.CountryV2Entity;
import org.marketplace_lea.common.entities.CurrencyValue;
import org.marketplace_lea.common.entities.account.AccountSponsorshipEntity;
import org.marketplace_lea.common.entities.account.AccountV2Entity;
import org.marketplace_lea.common.entities.customer.CustomerV2Entity;
import org.marketplace_lea.common.entities.wallet.WalletV2Type;
import org.marketplace_lea.common.repositories.DistrictJpaRepository;
import org.marketplace_lea.prometheus.common.dto.RegistrationV2Form;
import org.marketplace_lea.prometheus.domain.onboarding.forms.CustomerV2Mapper;
import org.marketplace_lea.common.repositories.CountryJpaRepository;
import org.marketplace_lea.common.repositories.account.AccountSponsorshipJpaRepository;
import org.marketplace_lea.common.repositories.account.AccountTypeV2JpaRepository;
import org.marketplace_lea.common.repositories.account.AccountV2JpaRepository;
import org.marketplace_lea.common.repositories.customer.CustomerV2JpaRepository;
import org.marketplace_lea.common.services.wallet.WalletV2Service;
import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;
import org.marketplace_lea.common.common.service.notification.MailService;
import org.marketplace_lea.common.common.service.notification.RegistrationEmailData;
import org.marketplace_lea.common.dtos.CustomerV2DTO;
import org.marketplace_lea.prometheus.domain.onboarding.services.CustomerOnboardingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation concrète du service d'inscription client.
 *
 * <p>Cette classe est responsable de l'inscription uniquement.
 * Elle respecte le principe de responsabilité unique (SRP) : ses méthodes
 * ne font que créer et persister les entités nécessaires à l'ouverture d'un compte.
 * Les notifications et la gestion des états (blocage, etc.) sont laissées à d'autres services.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCustomerOnboardingService implements CustomerOnboardingService {
    private final CustomerV2JpaRepository customerRepository;
    private final AccountV2JpaRepository accountV2JpaRepository;
    private final AccountTypeV2JpaRepository accountTypeV2JpaRepository;
    private final AccountSponsorshipJpaRepository accountSponsorshipJpaRepository;
    private final DistrictJpaRepository districtJpaRepository;
    private final CountryJpaRepository countryRepository;
    private final CustomerV2Mapper customerV2Mapper;
    private final WalletV2Service walletService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final SmsService smsService;

    @Value("${app.authentication.country-based:disabled}")
    private String countryBasedAuthentication;

    /**
     * {@inheritDoc}
     * Méthode qui permet à un utilisateur de s'onboarder (Création de ces accès, de son parrainage et de son wallet).
     *
     */
    @Override
    public CustomerV2DTO onboard(RegistrationV2Form form) {
        log.info("Début de l'inscription pour le login: {}", form.getLogin());

        // 1. Création d'un customer.
        var customerV2Saved = createCustomer(form);

        // 2. Envoie des informations sur la création par mail.
        mailService.sendRegistrationEmail(new RegistrationEmailData(customerV2Saved.getEmail(), form.getPassword(), customerV2Saved.fullname()));

        // 3. Envoies de SMS.
        smsService.sendSms("EDISPRO", String.format("Votre code est %s", form.getPassword()), String.format("+225%s", customerV2Saved.login()));

        log.info("Inscription terminée pour le login: {}", form.getLogin());
        return customerV2Mapper.toDTO(customerV2Saved);
    }


    // ==================== Méthodes privées de validation ====================
    @Transactional(rollbackFor = Exception.class) // Rollback pour toute exception
    private CustomerV2Entity createCustomer(RegistrationV2Form form) {
        // 1. Validation du pays et du téléphone
        validateCountryAndPhone(form);

        // 2. Vérification du code parrain (parent)
        Optional<AccountV2Entity> parentFound = findParentOrThrow(form.getParentCode());

        // 3. Construction des entités
        boolean isCountryBased = "enabled".equalsIgnoreCase(countryBasedAuthentication);

        CustomerV2Entity customer = buildCustomerFromForm(form, isCountryBased);

        // 4. Blockage automatique pour les comptes partenaires (à activer après validation admin)
        if (customer.getAccount().bePartner()) {
            customer.getAccount().setBlacklisted(true);
            log.info("Compte partenaire créé et bloqué en attente d'activation: {}", form.getLogin());
        }

        // 5. Persistance en cascade
        CustomerV2Entity customerV2Saved = customerRepository.save(customer);
        log.debug("Client persisté avec succès, id={}", customerV2Saved.getId());

        // 6. Création de la relation de parentalité si le parent existe.
        parentFound.ifPresent(parent -> createRelationship(parent, customerV2Saved.getAccount()));

        // 7. Création des portefeuilles
        initializeWallets(customerV2Saved.getAccount());

        return customerV2Saved;
    }


    private void validateCountryAndPhone(RegistrationV2Form form) {
        boolean isCountryBased = "enabled".equalsIgnoreCase(countryBasedAuthentication);
        if (!isCountryBased) {
            // Validation simple : 10 chiffres
            if (form.getLogin().length() != 10) {
                throw new ConsoEpargneException("Le numéro de téléphone doit comporter 10 chiffres.", HttpStatus.BAD_REQUEST);
            }

            if (accountV2JpaRepository.getByActiveLogin(form.getLogin()).isPresent()) {
                throw new ConsoEpargneException("Le login existe déjà !", HttpStatus.BAD_REQUEST);
            }
            return;
        }

        // Validation avancée basée sur le pays
        if (form.getCountryCode() == null || form.getCountryCode().isBlank()) {
            throw new ConsoEpargneException("Veuillez sélectionner le pays.", HttpStatus.BAD_REQUEST);

        }

        CountryV2Entity country = countryRepository.getByCode(form.getCountryCode())
                .orElseThrow(() -> new ConsoEpargneException(String.format("Aucun pays trouvé avec le code '%s'.", form.getCountryCode()), HttpStatus.BAD_REQUEST));

        if (form.getLogin().length() != country.getPhoneNumberLength()) {
            throw new ConsoEpargneException(String.format("Le numéro de téléphone doit comporter %d chiffres.", country.getPhoneNumberLength()), HttpStatus.BAD_REQUEST);
        }

        if (accountV2JpaRepository.existsByLoginAndCode(form.getLogin(), form.getCountryCode())) {
            throw new ConsoEpargneException("Le login existe déjà !", HttpStatus.BAD_REQUEST);
        }
    }


    private Optional<AccountV2Entity> findParentOrThrow(String parentCode) {
        if (parentCode == null || parentCode.isBlank()) {
            return Optional.empty(); // Pas de parrain obligatoire
        }

        return accountV2JpaRepository.getByAffiliationCode(parentCode);
    }


    // ==================== Méthodes privées de construction ====================
    private CustomerV2Entity buildCustomerFromForm(RegistrationV2Form form, boolean isCountryBased) {
        var entity = customerV2Mapper.toEntity(form);

        var accountType = accountTypeV2JpaRepository.findById(form.getAccountTypeId())
                .orElseThrow(() -> new ConsoEpargneException("Le type de compte n'existe pas !", HttpStatus.BAD_REQUEST));

        var account = entity.getAccount();
        account.setId(GeneratorUtils.generateAccountId());
        account.setAffiliationCode(GeneratorUtils.generateAffiliationCode());
        account.setPassword(passwordEncoder.encode(form.getPassword()));

        if(StringUtils.hasText(form.getToken())) {
            account.setNotificationToken(form.getToken());
        }

        account.setAccountType(accountType);
        account.setCreatedAt(LocalDateTime.now());

        entity.setAddress(form.getAddress());
        entity.setLatitude(form.latitude());
        entity.setLongitude(form.longitude());

        String phoneNumber = buildPhoneNumberLabel(form, isCountryBased);

        /// Ajout du district.
        addDistrict(entity, isCountryBased);

        entity.setPhoneNumbers(List.of(phoneNumber));
        return entity;
    }


    private String buildPhoneNumberLabel(RegistrationV2Form form, boolean isCountryBased) {
        //boolean isCountryBased = "enabled".equalsIgnoreCase(countryBasedAuthentication);
        if (!isCountryBased) {
            return form.getLogin();
        }

        String callingCode = countryRepository.getByCode(form.getCountryCode())
                .map(CountryV2Entity::getCallingCode)
                .orElse("");

        return callingCode + form.getLogin();
    }


    private void addDistrict(CustomerV2Entity customerV2Entity, boolean isCountryBased) {
        var districtFound = districtJpaRepository.findById(customerV2Entity.getDistrict().getId());
        if (isCountryBased && customerV2Entity.getDistrict() != null && "CIV".equals(customerV2Entity.getAccount().getCountryCode())) {
            districtFound.ifPresent(customerV2Entity::setDistrict);
        }
    }


    // ==================== Méthodes privées de persistance ====================
    private void createRelationship(AccountV2Entity parent, AccountV2Entity child) {
        Optional<AccountSponsorshipEntity> relationship = accountSponsorshipJpaRepository.getByRelationship(parent.getId(), child.getId());
        if (relationship.isPresent()) {
            throw new ConsoEpargneException("Ce code parrain a déjà été utilisé !", HttpStatus.BAD_REQUEST);
        }

        var sponsorship = AccountSponsorshipEntity.builder()
                .parent(parent)
                .child(child)
                .autorize(true)
                .createdAt(LocalDateTime.now())
                .build();
        var sponsorshipSaved = accountSponsorshipJpaRepository.save(sponsorship);
        log.info("Relation créée de partenariat, id={} entre parent={} et child={}", sponsorshipSaved.getId(), parent.getLogin(), child.getLogin());
    }


    private void initializeWallets(AccountV2Entity account) {
        walletService.createNewWallet(WalletV2Type.PERSONAL, CurrencyValue.POINT, account);
        walletService.createNewWallet(WalletV2Type.LEA, CurrencyValue.LEA, account);
        log.debug("Portefeuilles PERSONAL et LEA créés pour le compte {}", account.getLogin());
    }
}
