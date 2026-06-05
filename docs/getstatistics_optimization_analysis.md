# Analyse d'optimisation — `getStatistics()`

## 📍 Traçage des requêtes SQL (état actuel)

Voici le flux exact de requêtes exécutées pour un appel à `getStatistics()` :

| Étape | Méthode appelée | Requête SQL | Compte |
|-------|----------------|-------------|--------|
| 1 | `getConnectedAccount()` | `SELECT * FROM accounts WHERE login = ? AND blacklisted = false` | **1** |
| 2 | `getDirectChildren(account.getId())` | `SELECT s.*, c.* FROM ce_sponsorship s JOIN accounts c ON s.child_id = c.id WHERE s.parent_id = ? AND s.resigned_at IS NULL` | **1** |
| 3 | BFS loop — pour **chaque** enfant `child` | `getDirectChildren(child.getId())` → même requête que #2, mais par enfant | **N** |
| 4 | `computeStatisticsForLevel(levelAccounts)` | `SELECT w.* FROM wallets w WHERE w.wallet_type = 'PERSONAL' AND w.account_id IN (...)` | **1** |

### Total : **N + 3 requêtes** (où N = nombre d'enfants directs)

---

## 🔴 Problème critique : N requêtes gaspillées à 100%

> [!CAUTION]
> Les **N requêtes de l'étape 3 sont totalement inutiles**. Elles ne servent à rien.

Voici pourquoi — le code actuel ([L156-181](file:///home/hakon/Bureau/personal-projects/conso-epargne/prometheus-service/src/main/java/com/conso_epargne/prometheus_service/domain/customer/services/impl/DefaultCustomerProfileService.java#L156-L181)) :

```java
int level = 1;
while (!queue.isEmpty() && level <= MAX_NETWORK_DEPTH) {  // MAX_NETWORK_DEPTH = 1
    int levelSize = queue.size();
    List<AccountV2Entity> levelAccounts = new ArrayList<>(levelSize);

    for (int j = 0; j < levelSize; j++) {
        AccountV2Entity child = queue.poll();
        levelAccounts.add(child);
        queue.addAll(getDirectChildren(child.getId()));  // ⚠️ N requêtes ici
    }

    stats.put(String.format("level%d", level), computeStatisticsForLevel(levelAccounts));
    level++;  // level passe à 2 → la condition `level <= 1` échoue → fin de boucle
}
```

**Déroulé concret** avec 50 enfants directs :

1. `level = 1` → la boucle `for` itère 50 fois
2. À chaque itération, `getDirectChildren(child.getId())` est appelé → **50 requêtes SQL**
3. Les résultats sont ajoutés à `queue`
4. `level` passe à `2` → la condition `level <= MAX_NETWORK_DEPTH (1)` **échoue**
5. La boucle `while` se termine
6. **Les enfants de niveau 2 dans la queue ne sont JAMAIS traités**

→ **50 requêtes SQL pour rien.**

---

## 🟡 Même problème dans `getChildren()` et `computeCpm()`

Ce pattern identique se retrouve dans :
- [getChildren() L141](file:///home/hakon/Bureau/personal-projects/conso-epargne/prometheus-service/src/main/java/com/conso_epargne/prometheus_service/domain/customer/services/impl/DefaultCustomerProfileService.java#L141) : `queue.addAll(getDirectChildren(childAccount.getId()))`
- [computeCpm() L374](file:///home/hakon/Bureau/personal-projects/conso-epargne/prometheus-service/src/main/java/com/conso_epargne/prometheus_service/domain/customer/services/impl/DefaultCustomerProfileService.java#L374) : `queue.addAll(getDirectChildren(child.getId()))`

Les 3 méthodes souffrent du même gaspillage.

---

## ✅ Optimisations proposées

### Option A — Quick fix : guard clause sur le dernier niveau

Empêcher l'appel à `getDirectChildren()` quand on est au dernier niveau explorable :

```diff
 for (int j = 0; j < levelSize; j++) {
     AccountV2Entity child = queue.poll();
     levelAccounts.add(child);
-    queue.addAll(getDirectChildren(child.getId()));
+    if (level < MAX_NETWORK_DEPTH) {
+        queue.addAll(getDirectChildren(child.getId()));
+    }
 }
```

**Impact** : N+3 → **3 requêtes**. Changement minimal, compatible si `MAX_NETWORK_DEPTH` augmente un jour.

---

### Option B — Éliminer le BFS (recommandé pour `MAX_NETWORK_DEPTH = 1`)

Puisque la profondeur est de 1, le BFS (queue, boucle while, compteur de niveaux) est du code inutilement complexe. On peut simplifier radicalement :

```java
@Override
public Map<String, CustomerStatisticsDTO> getStatistics() {
    AccountV2Entity account = getConnectedAccount();
    List<AccountV2Entity> directChildren = getDirectChildren(account.getId());

    if (directChildren.isEmpty()) {
        return Map.of();
    }

    return Map.of("level1", computeStatisticsForLevel(directChildren));
}
```

**Impact** : Identique (3 requêtes), mais code beaucoup plus lisible et maintenable.

La même simplification s'applique à `getChildren()` et `computeCpm()`.

---

### Option C — Si `MAX_NETWORK_DEPTH` doit augmenter un jour : batch fetch par niveau

Ajouter une méthode batch au repository [AccountSponsorshipJpaRepository](file:///home/hakon/Bureau/personal-projects/conso-epargne/common-service/src/main/java/com/conso_epargne/common_service/repositories/v2/account/AccountSponsorshipJpaRepository.java) :

```java
@Query("SELECT s FROM AccountSponsorship s JOIN FETCH s.child " +
       "WHERE s.parent.id IN :parentIds AND s.resignedAt IS NULL")
List<AccountSponsorshipEntity> findActiveByParentIds(
        @Param("parentIds") List<String> parentIds);
```

Puis remplacer le fetch individuel par un fetch batch par niveau :

```java
while (!queue.isEmpty() && level <= MAX_NETWORK_DEPTH) {
    List<AccountV2Entity> levelAccounts = new ArrayList<>(queue);
    queue.clear();

    if (level < MAX_NETWORK_DEPTH) {
        List<String> parentIds = levelAccounts.stream()
                .map(AccountV2Entity::getId).toList();
        queue.addAll(sponsorshipRepository.findActiveByParentIds(parentIds)
                .stream().map(AccountSponsorshipEntity::getChild).toList());
    }

    stats.put(String.format("level%d", level),
              computeStatisticsForLevel(levelAccounts));
    level++;
}
```

**Impact** : Chaque niveau ne coûte qu'**1 requête SQL** au lieu de N, quelle que soit la profondeur.

---

## 📊 Comparaison des coûts

| Scénario (50 enfants, depth=1) | Requêtes SQL | Réduction |
|-------------------------------|-------------|-----------|
| **Actuel** | **53** | — |
| **Option A** (guard clause) | **3** | -94% |
| **Option B** (suppression BFS) | **3** | -94% |
| **Option C** (batch + multi-depth) | **3** (pour depth=1) | -94% |

> [!IMPORTANT]
> **Ma recommandation** : **Option B** si `MAX_NETWORK_DEPTH` reste à 1 (le plus propre). **Option C** si tu prévois d'augmenter la profondeur un jour (le plus évolutif). Applique le même fix aux 3 méthodes (`getStatistics`, `getChildren`, `computeCpm`).
