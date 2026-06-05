# 📘 Conso‑Epargne – Project Overview  

*Version:* **2.0.1** (Spring Boot 2.7.8)  
*Java:* **21** (or higher)  
*Build tool:* **Maven**  

---

## 1️⃣ Purpose & Scope  

Conso‑Epargne is a **financial‑services platform** that lets customers:

| Feature | Description |
|---------|-------------|
| **Become a partner** | Upgrade a regular account to a “partner” status and receive a payment‑gateway link. |
| **Wallet management** | Query personal or LEA wallets, convert balances to coins/points, and withdraw cash. |
| **Collect handling** | Retrieve the current collection (campaign) and its metadata. |
| **Order processing** | Purchase LEA tokens, choose payment method (Wave or Dilaac), and receive transaction links. |
| **Account maintenance** | Update credentials, disable accounts, and fetch customer profile data. |

All operations are exposed through **REST endpoints** (via `CustomerFacade` and other facades) and secured by `AuthenticationService`.

---

## 2️⃣ Architecture Overview  

```
+-------------------+      +-------------------+      +-------------------+
|   API Layer       | ---> | Application Layer | ---> |   Domain Layer    |
| (Controllers /    |      | (Use‑cases)       |      | (Aggregates,      |
|  Facades)         |      |                   |      |  Entities, VO)    |
+-------------------+      +-------------------+      +-------------------+
        ^                         ^                         ^
        |                         |                         |
        |                         |                         |
        |                         |                         |
        |                +-------------------+               |
        +--------------> |   Ports (Interfaces) | <----------+
                         +-------------------+
                                   |
                                   v
                         +-------------------+
                         |   Adapters (Infra)|
                         | (Spring Data JPA, |
                         |  RestTemplate,    |
                         |  Config)          |
                         +-------------------+
```

### 2.1 Package Layout (modular monolith)

```
com.app.consoepargne
│
├─ api                # Controllers / Facades (HTTP adapters)
│   └─ customer
│
├─ application        # Use‑case services (transaction scripts)
│   └─ customer
│
├─ domain
│   ├─ customer       # Aggregates: Customer, Credential, etc.
│   ├─ wallet         # Aggregates: LeaWallet, PersonalWallet
│   ├─ order          # Aggregates: Order, OrderItem
│   ├─ collect        # Aggregates: Collect, CollectRecipient
│   └─ port           # Repository & external‑service interfaces
│
├─ infrastructure
│   ├─ repository      # Spring Data JPA implementations
│   ├─ gateway         # RestTemplate / payment‑gateway clients
│   └─ config          # Spring configuration classes
│
└─ shared
    ├─ constants
    ├─ exception
    └─ util
```

*All layers depend **only** on the layers below them. The **Domain** never imports Spring or infrastructure classes.*

---

## 3️⃣ Core Modules & Responsibilities  

| Module | Main Classes | Responsibility |
|--------|--------------|----------------|
| **api.customer** | `CustomerFacade`, DTOs (`CustomerDTO`, `ResponseDTO`, …) | Expose REST endpoints, map request/response objects, perform input validation. |
| **application.customer** | `CustomerApplicationService` | Orchestrate domain operations, manage transactions (`@Transactional`). |
| **domain.customer** | `Customer`, `Credential`, `CustomerService` (domain service) | Business rules: password change, partner status, profile retrieval. |
| **domain.wallet** | `LeaWallet`, `PersonalWallet`, `WalletService` (domain) | Balance queries, conversion logic, withdrawal fees. |
| **domain.order** | `Order`, `OrderItem`, `OrderService` | Validation, total calculation, payment‑method selection. |
| **domain.collect** | `Collect`, `CollectRecipient` | Current collection handling, recipient data. |
| **domain.port** | `CustomerRepository`, `WalletRepository`, `PaymentGatewayPort` | Pure Java interfaces that the domain uses to persist or call external services. |
| **infrastructure.repository** | `CustomerJpaRepository`, `WalletJpaRepository` | Spring Data JPA implementations of the ports. |
| **infrastructure.gateway** | `PaymentGatewayClient` | Calls the external payment‑gateway REST API. |
| **shared.exception** | `ApplicationException`, `ConsoEpargneException`, … | Unified exception hierarchy; API layer translates them to HTTP status codes. |
| **shared.constants** | `Router`, `ApiVersion` | Centralised URL prefixes, enum values, etc. |

---

## 4️⃣ Technology Stack  

| Layer | Technology |
|-------|------------|
| **Framework** | Spring Boot 2.7.8 (auto‑configuration, embedded Tomcat) |
| **Persistence** | Spring Data JPA + Hibernate (PostgreSQL / H2 for tests) |
| **Build** | Maven 3.8+ (`mvn clean verify`) |
| **Testing** | JUnit 5, Mockito (unit); SpringBootTest (integration) |
| **Static# 📘 Conso‑Epargne – Project Overview  

*Version:* **1.2.0** (Spring Boot 2.7.8)  
*Java:* **17** (or higher)  
*Build tool:* **Maven**  

---

## 1️⃣ Purpose & Scope  

Conso‑Epargne is a **financial‑services platform** that lets customers:

| Feature | Description |
|---------|-------------|
| **Become a partner** | Upgrade a regular account to a “partner” status and receive a payment‑gateway link. |
| **Wallet management** | Query personal or LEA wallets, convert balances to coins/points, and withdraw cash. |
| **Collect handling** | Retrieve the current collection (campaign) and its metadata. |
| **Order processing** | Purchase LEA tokens, choose payment method (Wave or Dilaac), and receive transaction links. |
| **Account maintenance** | Update credentials, disable accounts, and fetch customer profile data. |

All operations are exposed through **REST endpoints** (via `CustomerFacade` and other facades) and secured by `AuthenticationService`.

---

## 2️⃣ Architecture Overview  

```
+-------------------+      +-------------------+      +-------------------+
|   API Layer       | ---> | Application Layer | ---> |   Domain Layer    |
| (Controllers /    |      | (Use‑cases)       |      | (Aggregates,      |
|  Facades)         |      |                   |      |  Entities, VO)    |
+-------------------+      +-------------------+      +-------------------+
        ^                         ^                         ^
        |                         |                         |
        |                         |                         |
        |                         |                         |
        |                +-------------------+               |
        +--------------> |   Ports (Interfaces) | <----------+
                         +-------------------+
                                   |
                                   v
                         +-------------------+
                         |   Adapters (Infra)|
                         | (Spring Data JPA, |
                         |  RestTemplate,    |
                         |  Config)          |
                         +-------------------+
```

### 2.1 Package Layout (modular monolith)

```
com.app.consoepargne
│
├─ api                # Controllers / Facades (HTTP adapters)
│   └─ customer
│
├─ application        # Use‑case services (transaction scripts)
│   └─ customer
│
├─ domain
│   ├─ customer       # Aggregates: Customer, Credential, etc.
│   ├─ wallet         # Aggregates: LeaWallet, PersonalWallet
│   ├─ order          # Aggregates: Order, OrderItem
│   ├─ collect        # Aggregates: Collect, CollectRecipient
│   └─ port           # Repository & external‑service interfaces
│
├─ infrastructure
│   ├─ repository      # Spring Data JPA implementations
│   ├─ gateway         # RestTemplate / payment‑gateway clients
│   └─ config          # Spring configuration classes
│
└─ shared
    ├─ constants
    ├─ exception
    └─ util
```

*All layers depend **only** on the layers below them. The **Domain** never imports Spring or infrastructure classes.*

---

## 3️⃣ Core Modules & Responsibilities  

| Module | Main Classes | Responsibility |
|--------|--------------|----------------|
| **api.customer** | `CustomerFacade`, DTOs (`CustomerDTO`, `ResponseDTO`, …) | Expose REST endpoints, map request/response objects, perform input validation. |
| **application.customer** | `CustomerApplicationService` | Orchestrate domain operations, manage transactions (`@Transactional`). |
| **domain.customer** | `Customer`, `Credential`, `CustomerService` (domain service) | Business rules: password change, partner status, profile retrieval. |
| **domain.wallet** | `LeaWallet`, `PersonalWallet`, `WalletService` (domain) | Balance queries, conversion logic, withdrawal fees. |
| **domain.order** | `Order`, `OrderItem`, `OrderService` | Validation, total calculation, payment‑method selection. |
| **domain.collect** | `Collect`, `CollectRecipient` | Current collection handling, recipient data. |
| **domain.port** | `CustomerRepository`, `WalletRepository`, `PaymentGatewayPort` | Pure Java interfaces that the domain uses to persist or call external services. |
| **infrastructure.repository** | `CustomerJpaRepository`, `WalletJpaRepository` | Spring Data JPA implementations of the ports. |
| **infrastructure.gateway** | `PaymentGatewayClient` | Calls the external payment‑gateway REST API. |
| **shared.exception** | `ApplicationException`, `ConsoEpargneException`, … | Unified exception hierarchy; API layer translates them to HTTP status codes. |
| **shared.constants** | `Router`, `ApiVersion` | Centralised URL prefixes, enum values, etc. |

---

## 4️⃣ Technology Stack  

| Layer | Technology |
|-------|------------|
| **Framework** | Spring Boot 2.7.8 (auto‑configuration, embedded Tomcat) |
| **Persistence** | Spring Data JPA + Hibernate (PostgreSQL / H2 for tests) |
| **Build** | Maven 3.8+ (`mvn clean verify`) |
| **Testing** | JUnit 5, Mockito (unit); SpringBootTest (integration) |
| **Static Analysis** | Checkstyle 9.3 (enforced via Maven plugin) |
| **Logging** | SLF4J + Logback (configured in `application.yml`) |
| **Configuration** | `application.yml` + environment variables (Docker support) |
| **Containerisation** | Dockerfile & `docker-compose.yml` (PostgreSQL, MinIO) |

---

## 5️⃣ Running the Application  

```bash
# 1️⃣ Build (skip tests for speed)
mvn clean install -DskipTests

# 2️⃣ Run locally
java -jar target/application.jar

# 3️⃣ Or use Docker (requires Docker Compose)
docker compose up --build
```

The service starts on **`http://localhost:8080`**.  
API documentation (Swagger/OpenAPI) is available at **`/swagger-ui.html`** if the `springdoc-openapi` dependency is added.

---

## 6️⃣ Testing Strategy  

| Test type | Scope | Tools |
|-----------|-------|-------|
| **Unit tests** | Domain objects & application services (no Spring) | JUnit 5, AssertJ, Mockito |
| **Integration tests** | Spring context, repository layer, REST endpoints | `@SpringBootTest`, Testcontainers (PostgreSQL) |
| **Contract tests** | External payment‑gateway client | WireMock or MockServer |
| **Static analysis** | Code style, design rules | `mvn checkstyle:check` (fails on violations) |
| **CI pipeline** | Run all above on each PR | GitHub Actions / GitLab CI |

*All new code must have at least one unit test covering the main business rule.*

---

## 7️⃣ Guidelines for Contributors  

1. **Follow the package architecture** – place new classes in the correct layer.  
2. **Write Javadoc** for every public class, method, and field (use the template from the Javadoc guide).  
3. **Never add Spring annotations to domain classes** – keep them pure POJOs.  
4. **Use the shared exception hierarchy**; map them to HTTP responses in `GlobalExceptionHandler`.  
5. **Run Checkstyle locally** before committing: `mvn checkstyle:check`.  
6. **Prefer constructor injection** (`@RequiredArgsConstructor`) for all beans.  
7. **Keep methods short** (≤ 30 lines) and focused on a single responsibility.  
8. **Document any breaking change** in `CHANGELOG.md` with a version bump.  

---

## 8️⃣ Future Roadmap (high‑level)  

| Milestone | Goal | Approx. effort |
|-----------|------|----------------|
| **v1.3 – Refactor to full DDD** | Extract domain services, introduce domain events, make aggregates immutable where possible. | 2 sprints |
| **v1.4 – Modular Maven sub‑modules** | Split each bounded context into its own Maven module for clearer compile‑time boundaries. | 1 sprint |
| **v2.0 – Micro‑service extraction** | If traffic grows, extract `wallet` and `order` contexts into independent services (still share `shared` kernel). | 3‑4 sprints |
| **v2.1 – Observability** | Add Micrometer metrics, OpenTelemetry tracing, and centralized logging (ELK). | 1 sprint |
| **v2.2 – CI/CD pipeline** | Full GitHub Actions pipeline with static analysis, unit + integration tests, Docker image publishing. | 1 sprint |

---

## 9️⃣ References  

- **Domain‑Driven Design** – Eric Evans, *Domain‑Driven Design: Tackling Complexity in the Heart of Software*.  
- **Hexagonal Architecture** – Alistair Cockburn, *Ports and Adapters*.  
- **Spring Boot Documentation** – <https://docs.spring.io/spring-boot/docs/current/reference/html/>  
- **Checkstyle Rules** – `src/main/resources/checkstyle.xml` (included in the repo).  

---

*This README is the **single source of truth** for onboarding new developers, understanding the high‑level design, and guiding future extensions.*