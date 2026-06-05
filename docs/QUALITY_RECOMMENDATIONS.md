# 🔍 Recommandations Qualité – Conso‑Epargne

> Ce document regroupe **toutes les recommandations** issues de l'analyse qualité du projet.
> Chaque section contient le **problème identifié**, l'**impact**, la **solution** et un **exemple de code** quand c'est pertinent.

---

## Table des matières

1. [Compilation & Build](#1️⃣-compilation--build)
2. [Style statique (Checkstyle)](#2️⃣-style-statique-checkstyle)
3. [Tests & Couverture](#3️⃣-tests--couverture)
4. [Logging & Observabilité](#4️⃣-logging--observabilité)
5. [Gestion des erreurs & Messages](#5️⃣-gestion-des-erreurs--messages)
6. [Javadoc & Documentation du code](#6️⃣-javadoc--documentation-du-code)
7. [Maintenabilité & Responsabilités](#7️⃣-maintenabilité--responsabilités)
8. [Dépendances & Dépréciation](#8️⃣-dépendances--dépréciation)
9. [Intégration Continue (CI)](#9️⃣-intégration-continue-ci)
10. [Plan de migration vers DDD](#🔟-plan-de-migration-vers-ddd)

---

## 1️⃣ Compilation & Build

### Problème identifié

`CustomerFacade.java:171` appelait `form.getOldPassword()` et `form.getNewPassword()` sur un **record** Java.
Les records génèrent des accesseurs **sans préfixe `get`** (ex. `oldPassword()`, pas `getOldPassword()`).

| Sévérité | Impact |
|----------|--------|
| 🔴 **Critique** | Le projet ne compilait plus, aucun test ne pouvait s'exécuter, aucun JAR n'était produit. |

### ✅ Correction appliquée

```java
// AVANT (❌ ne compile pas)
accountService.updateCredential(connectedAccount, form.getOldPassword(), form.getNewPassword());

// APRÈS (✅ utilise les accesseurs de record)
accountService.updateCredential(connectedAccount, form.oldPassword(), form.newPassword());
```

### Recommandation générale

- **Toujours utiliser les accesseurs de record** (sans `get`) quand la classe est un `record`.
- Activer les **inspections IDE** (IntelliJ : *Settings → Inspections → Java → Record accessor style*) pour détecter ce type d'erreur à la volée.

### Vérification

```bash
mvn clean install -DskipTests
# Doit afficher BUILD SUCCESS
```

---

## 2️⃣ Style statique (Checkstyle)

### Problème identifié

`mvn checkstyle:check` rapporte **≈ 9 504 violations** réparties sur plusieurs catégories.

| Catégorie | Description | Exemple |
|-----------|-------------|---------|
| **Whitespace / OperatorWrap** | Les opérateurs `+` doivent être en début de nouvelle ligne. | `CollectRecipient.java:41` |
| **NewlineAtEndOfFile** | Retour chariot manquant en fin de fichier. | `application.properties:1` |
| **MissingJavadocMethod** | Commentaire Javadoc manquant sur les méthodes publiques. | `OrderItem.java:31` |
| **DesignForExtension** | Classe non `final` avec méthode non `final/static/abstract`. | `Order.java:190` |
| **JavadocVariable** | Commentaire Javadoc manquant sur les champs publics. | `CollectRecipient.java:25` |
| **FinalParameters** | Paramètre non `final` dans les méthodes. | `OrderItem.java:31` |
| **LineLength** | Lignes dépassant 80 caractères. | `OrderItem.java:65` |
| **Imports** | Imports inutilisés ou non ordonnés. | (divers fichiers) |

### ✅ Solutions

#### A. Intégrer un formateur automatique (Spotless)

Ajouter dans le `pom.xml` :

```xml
<plugin>
    <groupId>com.diffplug.spotless</groupId>
    <artifactId>spotless-maven-plugin</artifactId>
    <version>2.43.0</version>
    <configuration>
        <java>
            <googleJavaFormat>
                <version>1.19.2</version>
                <style>AOSP</style>
            </googleJavaFormat>
            <removeUnusedImports/>
            <trimTrailingWhitespace/>
            <endWithNewline/>
        </java>
    </configuration>
</plugin>
```

Utilisation :

```bash
# Vérifier le formatage
mvn spotless:check

# Appliquer le formatage automatiquement
mvn spotless:apply
```

#### B. Corriger les violations DesignForExtension

```java
// AVANT (❌ Checkstyle warning)
public class Order {
    public void validate() { /* ... */ }
}

// APRÈS (✅ Option 1 : rendre la classe final)
public final class Order {
    public void validate() { /* ... */ }
}

// APRÈS (✅ Option 2 : rendre la méthode final)
public class Order {
    public final void validate() { /* ... */ }
}
```

#### C. Corriger les OperatorWrap

```java
// AVANT (❌)
String result = "Hello " +
    "World";

// APRÈS (✅ opérateur en début de ligne)
String result = "Hello "
    + "World";
```

#### D. Ajouter les retours chariot en fin de fichier

Configurer l'IDE :
- **IntelliJ** : *Settings → Editor → General → Ensure every saved file ends with a line break* ✅
- **VS Code** : `"files.insertFinalNewline": true`

#### E. Utiliser `final` sur les paramètres de méthode

```java
// AVANT (❌)
public OrderItem(CartItem cartItem) { /* ... */ }

// APRÈS (✅)
public OrderItem(final CartItem cartItem) { /* ... */ }
```

### Objectif

| Métrique | Actuel | Cible court terme | Cible long terme |
|----------|--------|-------------------|------------------|
| Violations Checkstyle | 9 504 | < 500 | 0 |

---

## 3️⃣ Tests & Couverture

### Problème identifié

- Les tests sont **systématiquement ignorés** (`-DskipTests`).
- Le projet contient **1 seul fichier de test** (`src/test/`) et aucun répertoire `src/test/resources`.
- Aucune donnée de couverture n'est disponible.

### ✅ Solutions

#### A. Créer des tests unitaires pour le domaine

```java
// src/test/java/com/app/consoepargne/domain/order/OrderTest.java
class OrderTest {

    @Test
    void validate_shouldThrow_whenAmountIsNegative() {
        // Given
        Order order = new Order(/* ... */);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> order.validate());
    }

    @Test
    void getTotal_shouldReturnSumOfItems() {
        // Given
        OrderItem item1 = new OrderItem(/* price=100 */);
        OrderItem item2 = new OrderItem(/* price=200 */);
        Order order = new Order(List.of(item1, item2));

        // Then
        assertEquals(300, order.getTotal());
    }
}
```

#### B. Créer des tests d'intégration pour les facades

```java
// src/test/java/com/app/consoepargne/api/customer/CustomerFacadeTest.java
@ExtendWith(MockitoExtension.class)
class CustomerFacadeTest {

    @Mock private WalletService walletService;
    @Mock private AccountService accountService;
    @Mock private AuthenticationService authenticationService;
    // ... autres mocks

    @InjectMocks
    private CustomerFacade customerFacade;

    @Test
    void getCurrentCollect_shouldReturnCollectDTO() throws Exception {
        // Given
        Account account = mock(Account.class);
        Collect collect = new Collect();
        collect.setId("collect-1");
        collect.setLabel("Collecte Janvier");
        when(authenticationService.getConnectedCustomerAccount()).thenReturn(account);
        when(account.currentCollect()).thenReturn(collect);

        // When
        CollecteDTO result = customerFacade.getCurrentCollect();

        // Then
        assertEquals("collect-1", result.id());
        assertEquals("Collecte Janvier", result.label());
    }

    @Test
    void getCurrentCollect_shouldThrow_whenNoCollect() throws Exception {
        // Given
        Account account = mock(Account.class);
        when(authenticationService.getConnectedCustomerAccount()).thenReturn(account);
        when(account.currentCollect()).thenReturn(null);

        // When & Then
        assertThrows(ConsoEpargneException.class,
            () -> customerFacade.getCurrentCollect());
    }
}
```

#### C. Ajouter JaCoCo pour la couverture

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>verify</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
</plugin>
```

```bash
mvn verify
# Rapport HTML généré dans target/site/jacoco/index.html
```

### Objectif

| Métrique | Actuel | Cible |
|----------|--------|-------|
| Fichiers de test | 1 | ≥ 50 |
| Couverture de code | 0% | ≥ 60% (domaine ≥ 80%) |
| Tests exécutés par build | 0 | Tous (`mvn verify` sans `-DskipTests`) |

---

## 4️⃣ Logging & Observabilité

### Problème identifié

La classe `CustomerFacade` est annotée `@Slf4j` mais **aucun appel de log** n'est présent dans les méthodes.
Cela rend le **debugging en production très difficile**.

### ✅ Solution

Ajouter des logs à **l'entrée et la sortie** de chaque méthode publique, ainsi qu'aux **points d'erreur** :

```java
public TransactionLinkDTO becomePartner(String phoneNumber) throws Exception {
    log.info("Début becomePartner – phoneNumber={}", phoneNumber);

    Account account = authenticationService.getConnectedCustomerAccount();
    log.debug("Compte connecté : id={}, isPartner={}", account.getId(), account.isPartner());

    if (account.isPartner()) {
        log.warn("Tentative de devenir partenaire alors que déjà partenaire – accountId={}", account.getId());
        throw new ApplicationException("Vous êtes déjà l'un de nos formidables partenaires !");
    }

    Transaction transaction = transactionManager.initMembershipTransactionPayment(account);
    log.info("Transaction initialisée : transactionId={}", transaction.getId());

    // ... reste de la logique

    log.info("Fin becomePartner – transactionId={}", transaction.getId());
    return transactionLinkDto;
}
```

### Convention de logging recommandée

| Niveau | Utilisation |
|--------|-------------|
| `log.error(...)` | Exception inattendue, échec métier critique. |
| `log.warn(...)` | Situation anormale mais gérée (ex. tentative de double partenariat). |
| `log.info(...)` | Entrée/sortie de méthode, transitions d'état importantes. |
| `log.debug(...)` | Détails techniques (IDs, valeurs de paramètres). |
| `log.trace(...)` | Très verbeux, uniquement pour le debugging local. |

### Objectif

| Métrique | Actuel | Cible |
|----------|--------|-------|
| Méthodes publiques avec logging | 0 | 100% |
| Exceptions loguées avant le throw | 0% | 100% |

---

## 5️⃣ Gestion des erreurs & Messages

### Problème identifié

Les messages d'erreur sont **codés en dur** dans les méthodes :

```java
// ❌ Messages en dur, difficiles à maintenir et à traduire
throw new ApplicationException("Vous êtes déjà l'un de nos formidables partenaires !");
throw new ApplicationException("Vous devez disposer d'un compte partenaire...");
throw new ConsoEpargneException("La collecte actuelle n'existe pas !", HttpStatus.NOT_FOUND);
```

### ✅ Solutions

#### A. Créer un fichier `messages.properties`

```properties
# src/main/resources/messages.properties
error.already.partner=Vous êtes déjà l''un de nos formidables partenaires !
error.partner.required=Vous devez disposer d''un compte partenaire pour effectuer {0} !
error.collect.not.found=La collecte actuelle n''existe pas !
error.wallet.not.found=Aucun wallet associé à ce compte pour le type ''{0}''
error.self.sponsorship=Impossible de se parrainer soi-même !
success.password.updated=Mot de passe mis à jour avec succès !
success.withdrawal.requested=Votre demande de retrait a bien été prise en compte. Nous vous prions de patienter.
success.operation.done=Opération effectuée avec succès !
success.account.deleted=Compte supprimé avec succès !
success.profile.updated=Modification effectuée avec succès !
```

#### B. Injecter `MessageSource` dans les services

```java
@Service
@RequiredArgsConstructor
public class CustomerFacade {

    private final MessageSource messageSource;

    public TransactionLinkDTO becomePartner(String phoneNumber) throws Exception {
        Account account = authenticationService.getConnectedCustomerAccount();
        if (account.isPartner()) {
            throw new ApplicationException(
                messageSource.getMessage("error.already.partner", null, LocaleContextHolder.getLocale())
            );
        }
        // ...
    }
}
```

#### C. Créer une classe utilitaire pour simplifier

```java
@Component
@RequiredArgsConstructor
public class Messages {

    private final MessageSource messageSource;

    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}

// Utilisation :
throw new ApplicationException(messages.get("error.already.partner"));
throw new ApplicationException(messages.get("error.partner.required", "un retrait d'espèce"));
```

### Objectif

| Métrique | Actuel | Cible |
|----------|--------|-------|
| Messages en dur dans le code | ~15 | 0 |
| Messages externalisés dans `messages.properties` | 0 | 100% |

---

## 6️⃣ Javadoc & Documentation du code

### Problème identifié

- **Aucune Javadoc** sur les classes, méthodes et champs publics.
- Checkstyle rapporte des centaines de violations `MissingJavadocMethod` et `JavadocVariable`.

### ✅ Templates Javadoc à utiliser

#### Classe publique

```java
/**
 * Gère les opérations liées aux clients (API mobile).
 *
 * <p>Cette façade orchestre les appels aux services métier
 * pour les opérations de portefeuille, de parrainage
 * et de gestion de compte.</p>
 *
 * @author Équipe Conso-Epargne
 * @since 1.0.0
 */
@Service
public class CustomerFacade { /* ... */ }
```

#### Méthode publique

```java
/**
 * Transforme un compte consommateur en compte partenaire.
 *
 * <p>Initie une transaction de paiement d'adhésion via
 * la passerelle de paiement (Wave ou Dilaac).</p>
 *
 * @param phoneNumber numéro de téléphone pour le paiement Dilaac
 *                    (peut être {@code null} pour un paiement Wave)
 * @return les liens de la transaction (statut, annulation, passerelle)
 * @throws ApplicationException si le compte est déjà partenaire
 * @throws AccountNotFoundException si aucun compte connecté
 */
public TransactionLinkDTO becomePartner(String phoneNumber) throws Exception {
    // ...
}
```

#### Champ / Constante publique

```java
/**
 * Frais de retrait appliqués au compte (en pourcentage).
 *
 * <p>Valeur par défaut : {@code 0.02} (2%).</p>
 */
public static final double DEFAULT_WITHDRAWAL_FEES = 0.02;
```

### Configuration IDE

#### IntelliJ IDEA – Live Template `jdoc`

1. **Settings → Editor → Live Templates → Java**
2. Ajouter un template :

```
/**
 * $DESCRIPTION$
 *
 * @author $USER$
 * @since $VERSION$
 */
```

3. **Applicable in** : *Java → Declaration*

#### VS Code – Snippet `jdoc`

```json
{
    "Javadoc Template": {
        "prefix": "jdoc",
        "body": [
            "/**",
            " * ${1:Description brève}.",
            " *",
            " * <p>${2:Description détaillée.}</p>",
            " *",
            " * @author ${3:Équipe Conso-Epargne}",
            " * @since ${4:1.2.0}",
            " */"
        ]
    }
}
```

### Enforcement via Checkstyle

Vérifier que `checkstyle.xml` contient :

```xml
<module name="JavadocMethod">
    <property name="scope" value="public"/>
    <property name="allowMissingParamTags" value="false"/>
    <property name="allowMissingThrowsTags" value="false"/>
</module>

<module name="JavadocType">
    <property name="scope" value="public"/>
</module>

<module name="JavadocVariable">
    <property name="scope" value="public"/>
</module>
```

### Objectif

| Métrique | Actuel | Cible |
|----------|--------|-------|
| Classes publiques documentées | ~0% | 100% |
| Méthodes publiques documentées | ~0% | 100% |

---

## 7️⃣ Maintenabilité & Responsabilités

### Problèmes identifiés

| Problème | Fichier concerné | Impact |
|----------|------------------|--------|
| **Facade trop volumineuse** | `CustomerFacade` (~230 lignes, ~12 méthodes publiques) | Complexité cognitive élevée, difficile à tester unitairement. |
| **Logique métier dans la facade** | `becomePartner()`, `buyCoin()`, `withdraw()` | Les règles métier sont couplées à la couche HTTP. |
| **Pas de séparation lecture/écriture** | Toutes les opérations dans la même classe | Difficile d'optimiser les lectures indépendamment. |
| **Code dupliqué** | `addTransactionLinksToResponse()` construit des URLs de la même façon | Pourrait être extrait dans un utilitaire ou un mapper. |

### ✅ Solutions

#### A. Découper la facade en services applicatifs

```
CustomerFacade (actuel : 12 méthodes)
    │
    ├── PartnerApplicationService
    │   └── becomePartner(phoneNumber)
    │
    ├── WalletApplicationService
    │   ├── getCustomerWalletInfo(type)
    │   ├── convertToCoin(amount)
    │   ├── convertToPoint(amount)
    │   ├── buyCoin(form)
    │   └── withdraw(form)
    │
    ├── CollectApplicationService
    │   └── getCurrentCollect()
    │
    └── AccountApplicationService
        ├── updateCredential(form)
        ├── updateCustomerInfo(form)
        ├── getFees()
        ├── getCpm()
        ├── getCurrentCustomerInfo()
        └── dismiss()
```

#### B. Déplacer la logique métier dans le domaine

```java
// AVANT (❌ logique dans la facade)
public TransactionLinkDTO becomePartner(String phoneNumber) throws Exception {
    Account account = authenticationService.getConnectedCustomerAccount();
    if (account.isPartner()) {
        throw new ApplicationException("Vous êtes déjà partenaire !");
    }
    // ...
}

// APRÈS (✅ logique dans l'agrégat Account)
// Account.java (domaine)
public void assertNotAlreadyPartner() {
    if (this.isPartner()) {
        throw new AlreadyPartnerException();
    }
}

// PartnerApplicationService.java (application)
@Transactional
public TransactionLinkDTO becomePartner(String phoneNumber) {
    Account account = authenticationService.getConnectedCustomerAccount();
    account.assertNotAlreadyPartner(); // règle métier dans le domaine
    Transaction tx = transactionManager.initMembershipTransactionPayment(account);
    // ...
}
```

#### C. Extraire la construction d'URLs

```java
// AVANT (❌ construction d'URL en dur dans la facade)
private TransactionLinkDTO addTransactionLinksToResponse(Transaction transaction) {
    String gatewayUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(String.format("%s/", PAYMENT_GATEWAY_URI))
        .path(transaction.getId())
        .toUriString();
    // ...
}

// APRÈS (✅ builder dédié)
@Component
public class TransactionLinkBuilder {

    public TransactionLinkDTO buildLinks(Transaction transaction) {
        String base = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        return new TransactionLinkDTO(
            buildCheckStatusUrl(base, transaction.getId()),
            buildCancelUrl(base, transaction.getId()),
            buildGatewayUrl(base, transaction.getId())
        );
    }

    private String buildCheckStatusUrl(String base, String txId) {
        return String.format("%s/%s/%s/%s/status", base, API_ROOT_V1, TRANSACTIONS_URI, txId);
    }

    // ...
}
```

### Objectif

| Métrique | Actuel | Cible |
|----------|--------|-------|
| Lignes max par classe | ~230 | ≤ 150 |
| Méthodes max par classe | ~12 | ≤ 7 |
| Logique métier dans les facades | ~60% | 0% |

---

## 8️⃣ Dépendances & Dépréciation

### Problème identifié

```
[INFO] InvestmentService.java: Some input files use or override a deprecated API.
[INFO] Recompile with -Xlint:deprecation for details.
```

### ✅ Solution

```bash
# Identifier les dépréciations
mvn clean compile -Xlint:deprecation
```

Puis corriger chaque appel déprécié en utilisant l'API de remplacement recommandée.

### Spring Boot 2.7.8 → 3.x

| Changement | Action requise |
|------------|----------------|
| `jakarta.*` → `jakarta.*` | Remplacer tous les imports (ex. `jakarta.validation` → `jakarta.validation`). |
| Spring Security 6 | Adapter la configuration de sécurité (nouveau DSL `SecurityFilterChain`). |
| Java 17 minimum | Déjà en place ✅ |
| `WebSecurityConfigurerAdapter` supprimé | Migrer vers `@Bean SecurityFilterChain`. |

### Objectif

| Métrique | Actuel | Cible |
|----------|--------|-------|
| Warnings de dépréciation | ≥ 1 | 0 |
| Version Spring Boot | 2.7.8 | 3.x (planifié) |

---

## 9️⃣ Intégration Continue (CI)

### Problème identifié

Aucun pipeline CI/CD n'est en place. Les violations de style et les erreurs de compilation sont découvertes **manuellement**.

### ✅ Solution – GitHub Actions

Créer le fichier `.github/workflows/ci.yml` :

```yaml
name: CI – Build & Quality

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      - name: Build & Test
        run: mvn -B clean verify

      - name: Checkstyle
        run: mvn -B checkstyle:check
        continue-on-error: true  # Temporaire, à supprimer quand violations = 0

      - name: Spotless Check
        run: mvn -B spotless:check
        continue-on-error: true

      - name: Upload JaCoCo Report
        uses: actions/upload-artifact@v4
        with:
          name: jacoco-report
          path: target/site/jacoco/
```

### Objectif

| Métrique | Actuel | Cible |
|----------|--------|-------|
| Pipeline CI | ❌ Aucun | ✅ Actif sur chaque PR |
| Build automatique | ❌ | ✅ `mvn verify` |
| Checkstyle automatique | ❌ | ✅ Bloquant (quand violations = 0) |
| Couverture publiée | ❌ | ✅ JaCoCo dans les artefacts CI |

---

## 🔟 Plan de migration vers DDD

### Sprint 0 – Baseline (✅ fait)

- [x] Corriger l'erreur de compilation (`record` accessor).
- [x] Vérifier que `mvn clean install -DskipTests` passe.

### Sprint 1 – Restructuration des packages

- [ ] Créer les packages `api`, `application`, `domain`, `infrastructure`, `shared`.
- [ ] Déplacer les fichiers existants (sans changer le code).
- [ ] Mettre à jour tous les imports.
- [ ] Vérifier la compilation.

### Sprint 2 – Ports & Adapters

- [ ] Extraire les interfaces de repository dans `domain.port`.
- [ ] Faire implémenter ces interfaces par les repos Spring Data dans `infrastructure.repository`.

### Sprint 3 – Thin Facade → Controller + Application Service

- [ ] Créer `PartnerApplicationService`, `WalletApplicationService`, etc.
- [ ] Déplacer la logique orchestration des facades vers les services applicatifs.
- [ ] Les facades ne gardent que le mapping HTTP ↔ DTO.

### Sprint 4 – Enrichissement du domaine

- [ ] Ajouter des méthodes métier aux agrégats (`Account.assertNotAlreadyPartner()`, `Order.validate()`).
- [ ] Introduire des Value Objects (`Money`, `Password`, `PhoneNumber`).
- [ ] Écrire les tests unitaires du domaine (sans Spring).

### Sprint 5 – Qualité & CI

- [ ] Intégrer Spotless dans le `pom.xml` et appliquer le formatage.
- [ ] Ajouter JaCoCo pour la couverture de tests.
- [ ] Créer le pipeline GitHub Actions.
- [ ] Externaliser tous les messages dans `messages.properties`.
- [ ] Ajouter le logging dans toutes les méthodes publiques.
- [ ] Ajouter la Javadoc sur toutes les classes et méthodes publiques.

### Sprint 6 – Nettoyage final

- [ ] Réduire les violations Checkstyle à 0.
- [ ] Atteindre ≥ 60% de couverture de tests.
- [ ] Supprimer le flag `continue-on-error` du CI.
- [ ] Corriger les warnings de dépréciation.

---

## 📊 Tableau récapitulatif

| Aspect | Rating actuel | Cible | Priorité |
|--------|---------------|-------|----------|
| Compilation | ✅ 5/5 | 5/5 | – |
| Style statique (Checkstyle) | ❌ 2/5 | 4/5 | 🔴 Haute |
| Tests & Couverture | ❌ 1/5 | 4/5 | 🔴 Haute |
| Logging | ❌ 1/5 | 4/5 | 🟡 Moyenne |
| Gestion des messages | ❌ 2/5 | 4/5 | 🟡 Moyenne |
| Javadoc | ❌ 1/5 | 4/5 | 🟡 Moyenne |
| Maintenabilité | 🟡 3/5 | 4/5 | 🟡 Moyenne |
| Dépendances | 🟡 3/5 | 5/5 | 🟢 Basse |
| CI/CD | ❌ 1/5 | 5/5 | 🔴 Haute |
| **Score global** | **≈ 2.1 / 5** | **≥ 4 / 5** | |

---

> **💡 Conseil :** Commencez par les priorités **🔴 Haute** (CI, tests, style).
> Elles ont le meilleur ratio effort/impact et empêchent les régressions futures.
> Les priorités **🟡 Moyenne** et **🟢 Basse** peuvent être traitées progressivement
> au fil des sprints.

---

*Document généré le 2026‑04‑09. Dernière mise à jour : à maintenir au fil des sprints.*
