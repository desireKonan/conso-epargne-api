# Optimisation de la recherche de produits (Pagination & Spécifications)

## Objectif
Optimiser la récupération des produits (actuellement des listes complètes via flux et filtres en mémoire) en utilisant la pagination (`Page`) et l'API Criteria/Specifications de JPA (`Specification`) pour filtrer directement en base de données.

## Étapes de l'implémentation

### 1. Critères de recherche (DTO)
- [x] Créer la classe `ProductV2Criteria` (ensignId, searchKeyword, isSoldOut, isDeleted).
- [x] Créer un utilitaire `ProductV2Specification` pour construire la `Specification<ProductV2Entity>` à partir du criteria.

### 2. Service Interface (`ProductV2Service`)
- [x] Remplacer les méthodes `findAll()`, `recycleBin()`, `findAllByEnsign()`, `getSoldOutProducts()` par une méthode unifiée : `Page<ProductV2Entity> searchProducts(ProductV2Criteria criteria, Pageable pageable)`.

### 3. Service Implémentation (`DefaultProductV2Service`)
- [x] Implémenter `searchProducts` en appelant `productRepository.findAll(Specification, Pageable)`.
- [x] Supprimer les anciens flux (`stream().filter()`) coûteux en mémoire.

### 4. Controller API (`ProductV2ApiController`)
- [x] Modifier la route `GET /api/v2/products` pour accepter les paramètres de filtrage et la pagination (Spring `@PageableDefault`).
- [x] Supprimer les routes redondantes ou les rediriger vers la recherche générique.

### 5. Vérification
- [x] Compiler le projet `order_service`.
- [x] Mettre à jour les lessons si nécessaire.
