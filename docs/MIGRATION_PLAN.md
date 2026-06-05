# 🗺️ Plan de Migration – Monolithe Modulaire + DDD

> **528 fichiers Java** → **8 modules**
> Migration incrémentale, module par module. Chaque module compile avant de passer au suivant.

---

## Architecture cible – 8 Modules

```
com.app.consoepargne
│
├── shared/              # Module 1 – Shared Kernel
├── prometheus/          # Module 2 – Common (transversal)
├── identity/            # Module 3 – Customer & Account
├── wallet/              # Module 4 – Wallet & Transactions
├── order/               # Module 5 – Order, Cart & Catalog
├── collect/             # Module 6 – Collect
├── investment/          # Module 7 – Investment
└── consoweb/            # Module 8 – Thymeleaf Admin
```

### Relations entre modules

```
┌──────────────────────────────────────────────────────┐
│                    CONSO‑EPARGNE                     │
│                                                      │
│  ┌──────────────────────────────────────────────┐    │
│  │           Shared Kernel (Module 1)            │    │
│  └──────────────────────────────────────────────┘    │
│                        ▲                             │
│  ┌──────────────────────────────────────────────┐    │
│  │         Prometheus – Common (Module 2)        │    │
│  │  (Auth, Notification, Config, Storage, Events)│    │
│  └──────────────────────────────────────────────┘    │
│           ▲         ▲         ▲         ▲            │
│  ┌────────┐ ┌───────┐ ┌──────┐ ┌────────┐           │
│  │Identity│ │Wallet │ │Order │ │Collect │           │
│  │  (3)   │ │  (4)  │ │ (5)  │ │  (6)   │           │
│  └────────┘ └───────┘ └──────┘ └────────┘           │
│                                  ┌────────┐          │
│                                  │Invest. │          │
│                                  │  (7)   │          │
│                                  └────────┘          │
│  ┌──────────────────────────────────────────────┐    │
│  │         Conso Web – Admin (Module 8)          │    │
│  └──────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────┘
```

---

## Ordre de migration

| # | Module | Effort | Priorité |
|---|--------|--------|----------|
| 1 | **Shared Kernel** | 1 jour | 🔴 |
| 2 | **Prometheus** (Common) | 2‑3 jours | 🔴 |
| 3 | **Identity** (Customer & Account) | 2‑3 jours | 🔴 |
| 4 | **Wallet** (Wallet & Transactions) | 3‑4 jours | 🔴 |
| 5 | **Order** (Order, Cart & Catalog) | 2‑3 jours | 🟡 |
| 6 | **Collect** | 1 jour | 🟡 |
| 7 | **Investment** | 1 jour | 🟡 |
| 8 | **Conso Web** (Admin Thymeleaf) | 1‑2 jours | 🟢 |
| | **Total** | **≈ 3‑4 semaines** | |

---

## Module 1 – Shared Kernel

> **Effort : 1 jour** | Exceptions, constantes, utilitaires, validation, DTOs génériques.

### Structure cible

```
shared/
├── constants/          # Router, ApiVersion, … (5 fichiers)
├── exception/          # Toutes les 38 exceptions
├── util/               # CodeGenerator, DateUtils, SecurityUtils, … (10 fichiers)
├── validation/         # CustomFieldError, Error (2 fichiers)
├── dto/                # ResponseDTO, ErrorDTO (2 fichiers)
└── service/            # BaseService, BaseAdapterService (2 fichiers)
```

### Mapping source → destination

| Source | Destination |
|--------|-------------|
| `common/constants/*.java` (5) | `shared/constants/` |
| `common/exceptions/*.java` (38) | `shared/exception/` |
| `common/utils/*.java` (10) | `shared/util/` |
| `common/validation/*.java` (2) | `shared/validation/` |
| `common/service/BaseService.java` | `shared/service/BaseService.java` |
| `common/service/BaseAdapterService.java` | `shared/service/BaseAdapterService.java` |
| `dtos/ResponseDTO.java` | `shared/dto/ResponseDTO.java` |
| `dtos/ErrorDTO.java` | `shared/dto/ErrorDTO.java` |
| `entities/BaseDto.java` | `shared/model/BaseDto.java` |
| `converters/StringListToJsonConverter.java` | `shared/util/StringListToJsonConverter.java` |

### Checklist

- [ ] Créer `com.app.consoepargne.shared` + sous‑packages
- [ ] Déplacer les fichiers
- [ ] Mettre à jour tous les imports
- [ ] `mvn clean compile` → BUILD SUCCESS

---

## Module 2 – Prometheus (Common / Transversal)

> **Effort : 2‑3 jours** | Auth, Notification, Config, Storage, Events, Reference data.

### Structure cible

```
prometheus/
├── auth/               # JwtAuthorizationFilter, JwtAuthenticationTokenService,
│                       # JwtAuthenticationService, TokenService, OTPService
├── notification/
│   ├── fcm/            # FCMService, FCMInitializer, PushNotificationService
│   ├── sms/            # YellikaSmsClient + config (7 fichiers)
│   ├── mail/           # MailSenderService
│   └── model/          # Message, PushNotificationRequest/Response
├── config/             # SecurityConfig, CacheConfig, AsyncEventConfig,
│                       # WebMvcConfig, ConsoAppConfig, DataLoader, CallbackWaveUrlConfig
├── storage/            # FileSystemStorageService, StorageService, StorageProperties, FileService
├── event/              # ConsoEpargneEvent, EventPublisher, OrderPaidEvent, OrderValidationEvent,
│                       # UserCreatedEvent + tous les listeners
├── webhook/            # NoWalletSignatureValidator
├── reference/          # Services et entités de référence (Country, District, Locality,
│                       # Config, AppConfig, AppRelease, GeneralSetting, Devise)
├── scheduler/          # SchedulerService, jobs, PaymentSchedulerService
├── statistics/         # StatisticsService, ConsoStatisticCalculator
└── api/                # ConfigApiController, CountryApiController, LocalityApiController,
                        # FaqAndConditionApiController, NotificationApiController,
                        # DeliveryDateAndHourApiController, PushNotificationController
```

### Fichiers clés à déplacer

| Source | Destination |
|--------|-------------|
| `configuration/*.java` (8) | `prometheus/config/` |
| `common/service/auth/*.java` (1) | `prometheus/auth/` |
| `common/service/storage/*.java` (4) | `prometheus/storage/` |
| `common/service/notification/*.java` (2) | `prometheus/notification/` |
| `common/service/webhook/*.java` (1) | `prometheus/webhook/` |
| `notification/*.java` (8) | `prometheus/notification/fcm/` + `model/` |
| `client/yellika/sms/*.java` (7) | `prometheus/notification/sms/` |
| `services/events/*.java` (5) | `prometheus/event/` |
| `services/listeners/*.java` (3) | `prometheus/event/listener/` |
| `services/JwtAuthenticationService.java` | `prometheus/auth/` |
| `services/TokenService.java` | `prometheus/auth/` |
| `services/OTPService.java` | `prometheus/auth/` |
| `services/MailSenderService.java` | `prometheus/notification/mail/` |
| `services/MessageService.java` | `prometheus/notification/` |
| `services/ConfigService.java` | `prometheus/reference/` |
| `services/GeneralSettingService.java` | `prometheus/reference/` |
| `services/CountryService.java` | `prometheus/reference/` |
| `services/DistrictService.java` | `prometheus/reference/` |
| `services/LocalityService.java` | `prometheus/reference/` |
| `services/SchedulerService.java` | `prometheus/scheduler/` |
| `services/scheduler/*.java` (4) | `prometheus/scheduler/` |
| `services/StatisticsService.java` | `prometheus/statistics/` |
| `services/ConsoStatisticCalculator.java` | `prometheus/statistics/` |
| `services/DeviseService.java` | `prometheus/reference/` |
| Entités de référence (Token, Message, Config, Country, District, Locality, AppRelease, AppConfig, Devise, ParameterType) | `prometheus/model/` |
| Repos de référence correspondants | `prometheus/persistence/` |
| Mappers (CountryMapper, DistrictMapper, LocalityMapper) | `prometheus/mapper/` |
| DTOs (CountryDto, DistrictDTO, LocalityDTO, ConfigAmountDTO, FaqAndConditionsDTO, StatisticConsoItem) | `prometheus/dto/` |
| Facades (FaqAndConditionFacade, DeliveryDateFacade) | `prometheus/api/facade/` |

### Checklist

- [ ] Créer `com.app.consoepargne.prometheus` + sous‑packages
- [ ] Déplacer auth, notification, config, storage, events, reference data
- [ ] Mettre à jour tous les imports
- [ ] `mvn clean compile` → BUILD SUCCESS

---

## Module 3 – Identity (Customer & Account)

> **Effort : 2‑3 jours** | Customer, Account, User, Registration, Sponsorship, Subscription.

### Structure cible

```
identity/
├── domain/
│   ├── model/          # Customer, CustomerV2, Account, AccountV2, AccountType,
│   │                   # AccountSponsorship, PhoneNumber, User, UserGroup,
│   │                   # Privilege, Membership, ConsoSubscription, AccountCollect
│   ├── port/           # CustomerRepository, AccountRepository (interfaces)
│   └── service/        # Règles métier pures
├── application/        # CustomerService, AccountService, UserService,
│                       # AuthenticationService, PrivilegeService,
│                       # SubscriptionService, AccountCollectService
├── infrastructure/
│   ├── persistence/    # JPA repositories
│   └── mapper/         # CustomerMapper, UserMapper, ConsoSubscriptionMapper
└── api/
    ├── rest/           # CustomerApiController, AuthenticationApiController,
    │                   # RegistrationApiController, AccountTypeApiController,
    │                   # ConsomApiController
    ├── facade/         # CustomerFacade, AuthenticationFacade, RegistrationFacade,
    │                   # ConsoMFacade
    ├── dto/            # CustomerDTO, AccountDTO, AuthFoundDTO, OtpResponse, …
    └── form/           # UpdateCredentialForm, RegistrationForm, VerifyOtpForm, …
```

### Fichiers clés

| Source | Destination |
|--------|-------------|
| `entities/Customer.java`, `CustomerV2.java` | `identity/domain/model/` |
| `entities/account/*.java` (8) – sauf AccountWallet | `identity/domain/model/` |
| `entities/PhoneNumber.java`, `User.java`, `UserGroup.java`, `Privilege.java`, `Membership.java` | `identity/domain/model/` |
| `entities/subscription/*.java` (2) | `identity/domain/model/` |
| `repositories/Customer*.java`, `Account*.java`, `User*.java`, `PhoneNumber*.java`, `Privilege*.java`, `Membership*.java`, `ConsoSubscription*.java` | `identity/infrastructure/persistence/` |
| `services/CustomerService.java`, `CustomerV2Service.java`, `AccountService.java`, `AccountTypeService.java`, `UserService.java`, `UserGroupService.java`, `AuthenticationService.java`, `PrivilegeService.java`, `subscription/*.java` (2), `AccountCollectService.java` | `identity/application/` |
| `facades/api/CustomerFacade.java`, `AuthenticationFacade.java`, `RegistrationFacade.java`, `ConsoMFacade.java` | `identity/api/facade/` |
| `controllers/api/v1/CustomerApiController.java`, `AuthenticationApiController.java`, `RegistrationApiController.java`, `AccountTypeApiController.java`, `ConsomApiController.java` | `identity/api/rest/` |
| DTOs customer/account + Forms correspondants | `identity/api/dto/` et `identity/api/form/` |
| `mapper/CustomerMapper.java`, `UserMapper.java`, `ConsoSubscriptionMapper.java` | `identity/infrastructure/mapper/` |

### Enrichissement domaine

- [ ] `Account.changePassword(old, new)`
- [ ] `Account.assertNotAlreadyPartner()`
- [ ] `Account.canWithdraw()` → boolean

### Checklist

- [ ] Créer `com.app.consoepargne.identity` + sous‑packages
- [ ] Déplacer entités, repos, services, facades, controllers, DTOs, forms
- [ ] Créer les ports (interfaces) dans `domain/port/`
- [ ] `mvn clean compile` → BUILD SUCCESS

---

## Module 4 – Wallet (Wallet & Transactions)

> **Effort : 3‑4 jours** | ⚠️ Module le plus gros – inclut tous les clients de paiement.

### Structure cible

```
wallet/
├── domain/
│   ├── model/          # Wallet, LeaWallet, PersonalWallet, ConsomWallet,
│   │                   # CollectWallet, InvestmentWallet, WalletType,
│   │                   # AccountWallet, AccountWalletId, CurrencyValue,
│   │                   # Transaction, TransactionStatus, TransactionType,
│   │                   # PaymentMethod, PaymentDetail, Voucher, Bonus, BonusType
│   ├── port/           # WalletRepository, TransactionRepository, PaymentGatewayPort
│   ├── service/        # TransactionValidator
│   └── factory/        # TransactionCreator, CashinTransactionCreator, …
├── application/        # WalletService, AccountTransactionManager,
│                       # TransactionService, TransactionV2Service,
│                       # PaymentGatewayService, PaymentMethodService,
│                       # PaymentDetailsService, VoucherService, TransfertService,
│                       # TransactionStatsCalculator
├── infrastructure/
│   ├── persistence/    # Tous les JPA repos (Wallet, Transaction, PaymentMethod, …)
│   ├── gateway/
│   │   ├── wave/       # WaveClient + config (12 fichiers)
│   │   ├── dilaac/     # DilaacClient + config (18 fichiers)
│   │   ├── paystack/   # PaystackClient + config (15 fichiers)
│   │   ├── nowallet/   # NoWalletClient + config (18 fichiers)
│   │   └── sotels/     # SotelsClient + config (5 fichiers)
│   ├── handler/        # CallbackTransactionHandler, TransactionHandler
│   ├── mapper/         # LeaWalletMapper, PersonalWalletMapper
│   └── payment/        # WavePaymentService, NoWalletOrchestrationService, CallbackService
└── api/
    ├── rest/           # TransactionApiController, PaymentMethodApiController,
    │                   # VoucherApiController, WaveApiController, NoWalletApiController
    ├── webhook/        # WaveWebhookController, NoWalletWebhookController,
    │                   # PayStackWebhookApiController, WaveWebHookApiController
    ├── facade/         # TransactionFacade, VoucherFacade
    ├── dto/            # TransactionDTO, TransactionLinkDTO, WalletDTO, …
    └── form/           # AmountForm, WithdrawalForm, TransactionForm, …
```

### ⚠️ Points d'attention

- Les clients Feign doivent garder leurs annotations `@FeignClient` intactes.
- Les webhooks sont critiques – tester manuellement après migration.
- `AccountTransactionManager` est le cœur métier – vérifier toutes ses dépendances.

### Checklist

- [ ] Créer `com.app.consoepargne.wallet` + sous‑packages
- [ ] Déplacer wallet entities + transaction entities + payment entities
- [ ] Déplacer les 68 fichiers clients (Wave, Dilaac, Paystack, NoWallet, Sotels)
- [ ] Déplacer services, facades, controllers, DTOs, forms
- [ ] Créer les ports dans `domain/port/`
- [ ] `mvn clean compile` → BUILD SUCCESS

---

## Module 5 – Order (Order, Cart & Catalog)

> **Effort : 2‑3 jours** | Commandes, panier, produits, rayons, fournisseurs.

### Structure cible

```
order/
├── domain/
│   ├── model/          # Order, OrderItem, OrderStatus, OrderStatusHistory,
│   │                   # OrderV2, OrderItemV2, CartItem, Address,
│   │                   # Product, Shelf, Supplier, Inventory, Image,
│   │                   # WishItem, Item, ItemV2, Ad, Ensign, ProjectType
│   ├── port/
│   └── event/          # OrderPaidEvent, OrderValidationEvent
├── application/        # OrderService, CartItemService, ProductService,
│                       # ShelfService, SupplierService, AddressService,
│                       # ImageService, WishItemService, AdService,
│                       # EnsignService, ProjectTypeService, OrderHistoryService
├── infrastructure/
│   ├── persistence/    # Tous les JPA repos correspondants
│   ├── delivery/       # WaneDelivery client (14 fichiers)
│   └── mapper/         # OrderMapper, AddressMapper
└── api/
    ├── rest/           # OrderApiController, ProductApiController, CartApiController,
    │                   # ShelfApiController, AdApiController, WishListApiController,
    │                   # EnsignApiController, WaneDeliveryApiController
    ├── facade/         # OrderFacade, AddressFacade
    ├── dto/            # OrderDTO, CartItemDTO, etc.
    └── form/           # OrderForm, QuantityForm
```

---

## Module 6 – Collect

> **Effort : 1 jour** | Collectes, bénéficiaires, statuts.

### Structure cible

```
collect/
├── domain/model/       # Collect, CollectStatus, CollectRecipient, AccountCollect, AccountCollectId
├── application/        # CollectService
├── infrastructure/
│   ├── persistence/    # CollectRepository, CollectRecipientRepository, AccountCollectRepository
│   └── mapper/         # CollectMapper
└── api/
    ├── rest/           # CollectApiController
    ├── facade/         # CollectFacade
    └── dto/            # CollectDTO, CollecteDTO, CollectsDTO, CollectMemberDTO, …
```

---

## Module 7 – Investment

> **Effort : 1 jour** | Investissements, profits, paiements.

### Structure cible

```
investment/
├── domain/model/       # Investment, InvestmentProfitPayment, InvestmentProfitPaymentStatus,
│                       # AccountInvestment, AccountInvestmentId
├── application/        # InvestmentService, AccountInvestmentService
├── infrastructure/
│   ├── persistence/    # InvestmentRepository, InvestmentProfitPaymentRepository,
│   │                   # AccountInvestmentRepository, InvestmentWalletRepository
│   └── mapper/         # InvestmentMapper
└── api/
    ├── rest/           # InvestmentApiController
    ├── facade/         # InvestmentFacade
    └── dto/            # InvestmentDTO, InvestorDTO, AccountInvestmentDTO, …
```

---

## Module 8 – Conso Web (Admin Thymeleaf)

> **Effort : 1‑2 jours** | Panel d'administration web.

### Structure cible

```
consoweb/
├── controller/         # 30 contrôleurs Thymeleaf (Dashboard, Customer, Order, Product, …)
├── base/               # BaseController, BaseApiController
└── error/              # CustomErrorController
```

### Les 30 contrôleurs web

AccessController, AccountTypeController, AdController, CollectController,
CountryController, CustomerController, DashboardController, DistrictController,
EnsignController, FileUploadController, GeneralSettingController,
InvestmentController, LocalityController, MainController, OrderController,
PaymentGatewayController, PaymentMethodController, PrivilegeController,
ProductController, ProjectTypeController, ShelfController, SupplierController,
TermsAndConditionController, TransactionController, UserController,
UserGroupController, VoucherController, WishlistController

---

## Checklist de validation (après chaque module)

```bash
# 1. Compilation
mvn clean compile

# 2. Build complet
mvn clean install -DskipTests

# 3. Commit
git add -A && git commit -m "refactor: migrate module X to DDD structure"
```

### Configuration Spring Boot à mettre à jour

```java
@SpringBootApplication
@EntityScan(basePackages = "com.app.consoepargne")
@EnableJpaRepositories(basePackages = "com.app.consoepargne")
@EnableFeignClients(basePackages = "com.app.consoepargne")
@ComponentScan(basePackages = "com.app.consoepargne")
public class ConsoEpargneApplication { }
```

### Règles strictes

1. **1 module à la fois** – ne jamais migrer 2 modules en parallèle.
2. **Commit après chaque module** – 1 commit = 1 module migré.
3. **Compiler après chaque déplacement** – ne pas accumuler les erreurs.
4. **Pas de changement de logique** – uniquement déplacements + imports.

---

## Calendrier

| Semaine | Modules | Résultat |
|---------|---------|----------|
| **S1** | Shared Kernel + Prometheus | Socle transversal en place |
| **S2** | Identity + Wallet | Cœur métier restructuré |
| **S3** | Order + Collect + Investment | Modules fonctionnels isolés |
| **S4** | Conso Web + Nettoyage | Migration terminée |

---

*Prêt à commencer ? Dis‑moi quel module tu veux attaquer en premier.*
