# Lessons Learned

<!-- Fichier mis à jour après chaque correction utilisateur (cf. docs/WORK_PLAN.MD §3) -->

## Règles actives

1. **Optimisation des flux et des listes (JPA)**
   - Ne jamais charger l'intégralité d'une table avec `.findAll()` puis faire un `stream().filter(...)` en mémoire.
   - Toujours privilégier `JpaSpecificationExecutor<T>` avec `Specification` (API Criteria) couplée à un `Pageable` de Spring Data pour filtrer la donnée directement côté base et éviter de saturer la RAM, notamment pour les entités volumineuses (ex: `ProductV2Entity`).
   - Fusionner les multiples routes "GET" (`/recycle-bin`, `/ensign/{id}`, `/sold-out`, etc.) en une seule méthode `search` acceptant un objet Criteria de filtre (ex: `ProductV2Criteria`) et des paramètres dynamiques (`@ModelAttribute`).
