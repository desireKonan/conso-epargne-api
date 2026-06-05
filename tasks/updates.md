# Updates and Refactoring

## OrderV2Service Implementation

### Overview
Implemented a new OrderV2Service in the order module, inspired by the existing OrderService in common-service, following the WORK_PLAN.MD rules for service implementation.

### Changes Made

#### 1. DTOs Created
- **OrderV2DTO** - DTO representing OrderV2Entity with flattened fields (customerId, customerName, voucherId, etc.)
- **CreateOrderV2Form** - Record for creating new orders with validation constraints
- **UpdateOrderV2Form** - Record for updating existing orders with optional fields
- **OrderV2SearchForm** - Record for filtering orders by customerId, status, delivered, hasOnlinePayment, provider

#### 2. Mapper Created
- **OrderV2Mapper** - MapStruct mapper for converting between OrderV2Entity and DTOs
  - Includes named method for mapping customer name from customer.account.login
  - Handles partial updates with NullValuePropertyMappingStrategy.IGNORE
  - Ignores system-managed fields during create/update operations

#### 3. Service Interface Created
- **OrderV2Service** - Following the standard interface format from WORK_PLAN.MD:
  - `Page<OrderV2DTO> getAll(OrderV2SearchForm filterCriteria, Pageable pageable)`
  - `OrderV2DTO getById(String id)`
  - `Optional<OrderV2DTO> findById(String id)`
  - `OrderV2DTO create(CreateOrderV2Form createDTO)`
  - `OrderV2DTO update(String id, UpdateOrderV2Form updateDTO)`
  - `void delete(String id)`
  - Additional methods: `changeOrderStatus`, `validateOrder`, `cancelOrder`

#### 4. Specification Created
- **OrderV2Specification** - JPA Specification for filtering orders by:
  - customerId
  - status
  - delivered
  - hasOnlinePayment
  - provider

#### 5. Service Implementation Created
- **DefaultOrderV2Service** - Implementation of OrderV2Service:
  - Uses OrderV2Repository for data access
  - Uses CustomerV2JpaRepository for customer lookup
  - Uses OrderV2Mapper for entity-DTO conversions
  - Implements status change logic (validate, cancel)
  - Follows SOLID principles with helper method `getEntityById`

#### 6. Controller Created
- **OrderV2ApiController** - REST endpoints:
  - `GET /api/v2/orders` - getAll with pagination and filtering
  - `GET /api/v2/orders/{id}` - getById
  - `POST /api/v2/orders` - create
  - `PUT /api/v2/orders/{id}` - update
  - `DELETE /api/v2/orders/{id}` - delete
  - `PATCH /api/v2/orders/{id}/status` - changeOrderStatus
  - `PATCH /api/v2/orders/{id}/validate` - validateOrder
  - `PATCH /api/v2/orders/{id}/cancel` - cancelOrder

### Architecture Followed
```
order/
|--- domain
|---- order
|----- dto (OrderV2DTO, CreateOrderV2Form, UpdateOrderV2Form, OrderV2SearchForm)
|----- mapper (OrderV2Mapper)
|----- services (OrderV2Service)
|----- services/impl (DefaultOrderV2Service)
|----- services/specifications (OrderV2Specification)
|----- controllers (OrderV2ApiController)
```

### Compliance with WORK_PLAN.MD Rules
- ✅ Analyzed common-service module (OrderV2Entity, OrderService, OrderV2Repository)
- ✅ Used v2 package entities and repositories
- ✅ Created DTOs for all service return types
- ✅ Created mapper for Entity-DTO binding
- ✅ Followed standard service interface format
- ✅ Applied SOLID principles
- ✅ Used Specifications for filtering
- ✅ Supported pagination

### Notes
- Voucher functionality marked as TODO in create method (requires Voucher repository)
- Used UUID for order ID generation
- Default status set to PENDING for new orders
- Timestamps (validatedAt, canceledAt) automatically set on status changes

## OrderCreationV2Service Extraction

### Overview
Extracted create and update methods from OrderV2Service into a separate OrderCreationV2Service following SOLID Single Responsibility Principle (SRP). This separates concerns between order creation/modification and order management (status changes, queries, deletion).

### Changes Made

#### 1. New Service Interface Created
- **OrderCreationV2Service** - Dedicated interface for order creation and update:
  - `OrderV2DTO create(CreateOrderV2Form createDTO)`
  - `OrderV2DTO update(String id, UpdateOrderV2Form updateDTO)`

#### 2. New Service Implementation Created
- **DefaultOrderCreationV2Service** - Implementation of OrderCreationV2Service:
  - Uses OrderV2Repository for data access
  - Uses CustomerV2JpaRepository for customer lookup
  - Uses OrderV2Mapper for entity-DTO conversions
  - Handles order creation with UUID generation and default status
  - Handles order updates with partial field mapping

#### 3. OrderV2Service Updated
- Removed `create` method
- Removed `update` method
- Retained methods: `getAll`, `getById`, `findById`, `delete`, `changeOrderStatus`, `validateOrder`, `cancelOrder`
- Removed unused imports (CreateOrderV2Form, UpdateOrderV2Form, CustomerV2JpaRepository, UUID)

#### 4. DefaultOrderV2Service Updated
- Removed `create` method implementation
- Removed `update` method implementation
- Removed CustomerV2JpaRepository dependency
- Removed unused imports (CreateOrderV2Form, UpdateOrderV2Form, CustomerV2Entity, UUID)

#### 5. OrderV2ApiController Updated
- Added OrderCreationV2Service dependency
- Updated `create` endpoint to use `orderCreationV2Service`
- Updated `update` endpoint to use `orderCreationV2Service`
- Other endpoints continue to use `orderV2Service`

### Benefits
- **Single Responsibility**: OrderCreationV2Service focuses solely on creation/update logic
- **Separation of Concerns**: OrderV2Service focuses on order management (status, queries, deletion)
- **Easier Testing**: Each service can be tested independently
- **Better Maintainability**: Changes to creation logic don't affect management logic
- **Follows SOLID Principles**: Adheres to Single Responsibility Principle

### Architecture Updated
```
order/
|--- domain
|---- order
|----- dto (OrderV2DTO, CreateOrderV2Form, UpdateOrderV2Form, OrderV2SearchForm)
|----- mapper (OrderV2Mapper)
|----- services (OrderV2Service, OrderCreationV2Service, OrderCreationHandler)
|----- services/impl (DefaultOrderV2Service, DefaultOrderCreationV2Service, DefaultOrderCreationHandler)
|----- services/specifications (OrderV2Specification)
|----- controllers (OrderV2ApiController)
```

## OrderCreationHandler Implementation

### Overview
Created OrderCreationHandler to orchestrate the complex business logic around order creation, based on the createOrder method from OrderService.java. This handler follows the Command Pattern to encapsulate all order creation actions.

### Changes Made

#### 1. Handler Interface Created
- **OrderCreationHandler** - Interface for order creation orchestration:
  - `OrderV2DTO handleOrderCreation(CreateOrderV2Form createDTO)`
  - Handles: cart validation, payment method handling, address management, payment detail creation, order history, event publishing, cart deletion

#### 2. Handler Implementation Created
- **DefaultOrderCreationHandler** - Implementation with 12-step orchestration:
  1. Get customer by ID
  2. Validate cart is not empty (TODO - requires CustomerV2Service)
  3. Validate no sold out products (TODO - requires CustomerV2Service)
  4. Get payment method (TODO - requires PaymentMethodService)
  5. Build order entity with mapper
  6. Handle address (TODO - requires AddressRepository)
  7. Create payment detail (TODO - requires PaymentDetailsService)
  8. Save order to repository
  9. Create order history (TODO - requires OrderHistoryService)
  10. Publish delivery event (TODO - requires EventPublisher)
  11. Delete customer cart (TODO - requires CustomerV2Service)
  12. Handle voucher (TODO - requires Voucher repository)

#### 3. Service Updated
- **DefaultOrderCreationV2Service** - Delegated create method to OrderCreationHandler:
  - Removed CustomerV2JpaRepository dependency
  - Removed direct order creation logic
  - Now delegates to `orderCreationHandler.handleOrderCreation()`
  - Simplified to orchestration role

### Architecture Pattern
The implementation follows the **Command Pattern** and **Single Responsibility Principle**:
- **OrderCreationV2Service**: Entry point for order creation (interface)
- **OrderCreationHandler**: Orchestrates complex business logic (command)
- **DefaultOrderCreationHandler**: Implements the orchestration steps (command implementation)

### TODO Items
The handler includes TODOs for dependencies that need to be implemented in v2:
- CustomerV2Service (cart operations)
- PaymentMethodService
- PaymentDetailsService
- OrderHistoryService
- AddressRepository
- EventPublisher
- Voucher repository

### Benefits
- **Separation of Concerns**: Complex business logic isolated in handler
- **Testability**: Handler can be tested independently
- **Maintainability**: Each step is clearly documented and can be modified independently
- **Extensibility**: New steps can be added to the handler without affecting the service
- **Progressive Implementation**: TODOs allow gradual implementation of missing dependencies
