---
tags: [spring, kotlin, iut, td, td2]
date: 2026-09-26
cible: spring-boot-4.1
amphi: c2
---

← [[update]]

# TD2 — Validation, erreurs, configuration, JPA (amphi 2)

Modules : `exo21` → `td2/validation`, `exo33` → `td2/jpa`. Énoncé : `TD2.adoc` → `td2/README.adoc`.
Amphi de référence : `c2` (`validation` → `error` → `config` → `jpa` → `jpa2`).

> [!danger] Le TD le plus exposé à la migration 4.1
> La validation en 4.1 ne passe plus par `@Validated` sur le contrôleur (`C2-01`, `C2-02`,
> `C2-30`). Or le squelette (`@Validated` + `ConstraintViolationException`) **et** les tests
> fournis reposent sur l'ancien mécanisme. `T2-08` est à faire en premier : il conditionne les
> exos 1 à 6.

## Table des exos

Le TD2 correspond à c2, avec trois exceptions : l'ancien exo 40 a besoin d'`orphanRemoval` (vu en c3), les anciens exos 33-34
supposent une configuration de datasource (absente de c2), et plusieurs exos n'ont aucune slide.

| Nouveau | Ancien | Module | Sujet | Slide | Vérification | Socle |
|---|---|---|---|---|---|---|
| 1 | 21 | validation | annotations de validation | `c2/validation.md:88-137` | `PetControllerTest.Exo21` | socle |
| 2 | 22 | validation | méta-annotation sur `petId` | `c2/validation.md:255-320` | `Exo20` + `Exo22` | socle |
| 3 | 23 | validation | request params dans un objet | **aucune** (🔀 `T2-17`) | `Exo23` | socle |
| 4 | 24 | validation | `@ValidAgeRange` (niveau classe) | `c2/validation.md:327-485` (niveau paramètre seulement) | `Exo23` + `Exo24` | socle |
| 5 | 25 | validation | `@ExceptionHandler` → 418 | `c2/error.md:106-176` | `Exo25` | socle |
| 6 | 26 | validation | surcharge de `ResponseEntityExceptionHandler` | `c2/error.md:177-216` | `Exo26` (🔴 `T2-01`) | socle |
| 7 | 27 | validation | `banner.txt` | **aucune** | visuelle | socle |
| 8 | 28 | validation | `@Value` | `c2/config.md:334-376` | `InfoTest.Exo28` | socle |
| 9 | 29 | validation | `@ConfigurationProperties` | `c2/config.md:377-459` | `InfoTest.Exo28` | socle |
| 10 | 30 | validation | profils | `c2/config.md:175-252` | `InfoTest.Exo30` | socle |
| 11 | 31 | validation | paramètre `maxRange` | `c2/validation.md:404-470` | 🔵 `T2-13` | socle |
| 12 | 32 | validation | validateur injecté comme bean | **aucune** | 🔵 `T2-13` | ⭐ proposé |
| 13 | 33 | jpa | H2 en mémoire + console | **aucune** (🔀 `T2-19`) | manuelle | socle |
| 14 | 34 | jpa | H2 dans un fichier | **aucune** | manuelle | socle |
| 15 | 35 | jpa | Entity + `JpaRepository` | `c2/jpa.md:110-140`, `:435-512` | `exo 35` | socle |
| 16 | 36 | jpa | OneToOne unidirectionnel | `c2/jpa2.md:23-65`, `:245-440` | `exo 36` | socle |
| 17 | 37 | jpa | OneToOne bidirectionnel | idem | `exo 36` | socle |
| 18 | 38 | jpa | OneToMany unidirectionnel | `c2/jpa2.md:66-109`, `:441-573` | `exo 38` | socle |
| 19 | 39 | jpa | OneToMany bidirectionnel | idem | `exo 38` | socle |
| 20 | 40 | jpa | DELETE d'un pet | `c3/jpa3.md:583-639` (🔀 `T2-20`) | `exo 40` commenté | socle |
| 21 | Bonus | jpa | profil Postgres | `c2/config.md:175-252` | manuelle (Docker) | ⭐ |

## 🔴 Erreurs

- [ ] **T2-01** · **`exo21/src/test/kotlin/iut/nantes/exo21/controller/PetControllerTest.kt:238`** —
  `jsonPath("$.message") { startsWith("JSON parse error") }` construit un matcher Hamcrest
  sans jamais l'appliquer : l'assertion ne vérifie rien → `value(startsWith(...))`.
  *Vérif :* le test échoue si le message ne commence pas par « JSON parse error ».
- [ ] **T2-02** · **`exo21/src/main/resources/application.yml:1-7`** — l'application s'appelle
  `exo20`, et il reste des réglages `r2dbc` (URL `jdbc:` invalide pour R2DBC) et `flyway`, alors
  que ces dépendances n'existent pas dans le module → nom = nom du module, supprimer les résidus.
  *Vérif :* le démarrage n'affiche aucun warning sur des propriétés inconnues.
- [ ] **T2-03** · **`exo21/…/PetControllerTest.kt:31-32`** — `@Nested inner class Exo20`
  (vestige de l'ancienne numérotation, cf. `origin/tmp`) teste les GET/PUT de base → le
  renommer (`Base` ou `Exo2`, selon ce qu'il teste réellement) avec la convention `T0-16`.
  *Vérif :* aucun nom de classe de test ne cite un numéro d'exo inexistant.
- [ ] **T2-04** · **`exo33/src/main/kotlin/iut/nantes/exo33/controller/HumanController.kt:18-19`** —
  `@GetMapping("/api/v1/human/{human}")` avec `@PathVariable humanId` : le nom ne correspond pas
  (erreur 500 au premier appel), et le chemin est au singulier alors que les autres sont au pluriel
  (`/api/v1/humans`) → `/api/v1/humans/{humanId}`.
  *Vérif :* test GET `/api/v1/humans/1` → 200 ou 404, jamais 500.
- [ ] **T2-05** · **`TD2.adoc:115-133`** (ancien exo 28) — l'exemple JSON donne `"app-name"`,
  `"1.0"`, `"master"`, alors que `InfoTest.kt:30-33` attend `appName`, `1.0.1`, `main` →
  aligner l'énoncé sur le test (et le YAML de l'ancien exo 27).
  *Vérif :* le JSON de l'énoncé est exactement celui que le test attend.
- [ ] **T2-06** · **`TD2.adoc:213-227`** (ancien exo 35) — l'énoncé cite `findAllHumans`, alors
  que le squelette a `findAllHuman` (`exo33/…/DatabaseProxy.kt:29`) → harmoniser (au pluriel).
  *Vérif :* `grep -rn findAllHuman td2/` → une seule orthographe.
- [ ] **T2-07** · **`exo33/src/test/…/PetControllerTest.kt`** — la classe s'appelle
  `PetControllerTest` mais teste `/api/v1/humans` ; les tests partagent la même base (pas de
  nettoyage, lectures de `$[0]`), donc dépendent de l'ordre d'exécution ; import mort de
  `startsWith`, `WebMvcTest`, `EnableWebMvc` ; `@BeforeEach` vide (`:29-31`) → renommer
  (`HumanControllerTest`), nettoyer la base (`@Transactional` sur la classe de test, ou
  `deleteAll()` dans `@BeforeEach`), supprimer les imports morts.
  *Vérif :* `./gradlew :jpa:test` vert sur `correction` en exécutant les tests dans un ordre
  aléatoire (`junit.jupiter.testmethod.order.default=random`).

## 🟠 Migration 4.1

- [ ] **T2-08** · **`exo21/src/main/kotlin/iut/nantes/exo21/controller/PetController.kt:21`** et
  **`config/ErrorHandler.kt:12-13`** — `@Validated` sur le contrôleur, et un handler de
  `ConstraintViolationException` : c'est le mécanisme d'avant Spring 6.1 (`C2-01`, `C2-02`,
  `C2-30`). En 4.1, la validation des `@PathVariable`/`@RequestParam` est native et lève
  `HandlerMethodValidationException`, déjà gérée par `ResponseEntityExceptionHandler`
  → retirer `@Validated` et le handler ; vérifier que les tests `Exo21`, `Exo22`, `Exo24`
  attendent toujours le bon format d'erreur (sinon, adapter le **test** et l'énoncé
  « Un controller advice est déjà fourni » `TD2.adoc:3`).
  *Vérif :* correction des exos 1-4 verte **sans** `@Validated` dans `td2/validation/src/main`.
- [ ] **T2-09** · **`exo21/build.gradle.kts`, `exo33/build.gradle.kts`** — `T0-12`/`T0-13` :
  `starter-webmvc`, `tools.jackson`, springmockk 5 ; supprimer `flyway-core` et
  `kotlin-logging` de `exo33` (TD4) et `actuator` s'il ne sert pas.
  *Vérif :* `./gradlew -p td2 compileTestKotlin`.
- [ ] **T2-10** · **tests MockMvc** (`exo21/…/InfoTest.kt:6-7`, `PetControllerTest.kt:7`,
  `exo33/…/PetControllerTest.kt:8-9`) — imports déplacés (`MIG-12`).
  *Vérif :* compile.
- [ ] **T2-11** · **Jackson 3** — `FAIL_ON_NULL_FOR_PRIMITIVES` est activé par défaut et l'ordre
  des propriétés devient alphabétique : vérifier l'ancien exo 26 (qui repose précisément sur une
  erreur de désérialisation, `C2-33`) et les `jsonPath` des tests.
  *Vérif :* tests `Exo26` et `InfoTest` verts sur `correction`.
- [ ] **T2-12** · **console H2** (`exo21/…/application.yml:8-10` et énoncé `TD2.adoc:176-203`) —
  la console passe dans un module séparé en Boot 4 → ajouter la dépendance dans le module
  `jpa` (ou la faire ajouter dans l'énoncé) et vérifier `/h2-console`.
  *Vérif :* `http://localhost:8080/h2-console` répond après l'exo 13.

## 🔵 Autonomie / tests

- [ ] **T2-13** · **anciens exos 31-32** (`TD2.adoc:156-174`) — aucun test → fournir `Exo11Test`
  (écart > max-range → 400) et, pour l'exo 12, un test `@SpringBootTest(properties = ["custom.api.pets.max-range=5"])`.
  Au passage, l'énoncé dit « valeur en dur à 100 », alors que la correction utilise un paramètre
  d'annotation qui vaut par défaut `Int.MAX_VALUE` → trancher et aligner.
  **DÉCISION :** ___
  *Vérif :* tests rouges sur `main`, verts sur `correction`.
- [ ] **T2-14** · **anciens exos 37 et 39** — ils réutilisent les tests `exo 36` et `exo 38`, qui
  passent aussi avec une relation unidirectionnelle → ajouter un test qui prouve la
  bidirectionnalité (par exemple, lire `pet.owner` depuis le repository).
  *Vérif :* le test de l'exo 17 est rouge avec la correction de l'exo 16 et vert avec celle de l'exo 17.
- [ ] **T2-15** · **`exo33/…/PetControllerTest.kt:94-110`** (ancien exo 40) — le test est
  commenté (« Uncomment the following line to before runing the test ») parce qu'il ne compile pas
  avant l'ajout de `pets` dans `HumanDto` → garder cette mécanique, mais corriger la phrase et
  indiquer le commentaire dans l'énoncé.
  *Vérif :* énoncé et commentaire concordent.
- [ ] **T2-16** · **anciens exos 33-34** — vérification manuelle seulement → écrire dans l'énoncé
  la vérification attendue (URL, requête SQL, résultat attendu, fichier `data/testdb.mv.db`
  créé).
  *Vérif :* un étudiant sait dire seul si l'exo est réussi.

## 🔀 Cohérence avec les slides

- [ ] **T2-17** · **ancien exo 23 (`TD2.adoc:20-27`)** — regrouper des request params dans un
  objet n'est dans aucune slide, et la phrase « Il ne faut pas lui ajouter d'annotation (même
  pas @RequestParam) sans lui fournir d'annotation » est illisible. Proposition : ajouter une slide
  courte dans `c2/validation.md` (ou `c1/mvc.md`) et réécrire la phrase.
  **DÉCISION :** ___
  *Vérif :* phrase relue ; la slide existe ou l'énoncé donne un indice.
- [ ] **T2-18** · **ancien exo 24** — la slide montre un validateur sur un `Int`
  (`VALUE_PARAMETER`), alors que l'exo demande une contrainte **de classe** (`TYPE`, validateur sur
  `AgeRange`). Proposition : ajouter l'exemple `@Target(CLASS)` à `c2/validation.md` (avec
  `C2-48`), ou donner un indice repliable dans l'énoncé.
  **DÉCISION :** ___
  *Vérif :* les deux formes sont présentes dans la slide ou l'énoncé.
- [ ] **T2-19** · **anciens exos 13-14 (33-34)** — la configuration `spring.datasource.*` et la
  console H2 n'apparaissent qu'en c4 (`c4/flyway.md:128-143`). Proposition : une slide en
  `c2/jpa.md` « configurer la datasource » (H2 mémoire / fichier / Postgres), qui sert aussi au
  bonus Postgres.
  **DÉCISION :** ___
  *Vérif :* slide présente en c2.
- [ ] **T2-20** · **ancien exo 40** — la suppression d'un pet repose sur `orphanRemoval`, enseigné
  seulement en c3 (`c3/jpa3.md:583-639`). Choix : (a) ajouter `orphanRemoval` à `c2/jpa2.md`,
  (b) déplacer l'exo au début du TD3, (c) passer l'exo en bonus.
  **DÉCISION :** ___
  *Vérif :* le TD2 n'utilise que des notions de c1 et c2.
- [ ] **T2-21** · **ancien exo 26** — le test impose `{status, message}` et vérifie que `$.title`
  **n'existe pas**, ce qui interdit `ProblemDetail`, que la relecture propose d'ajouter à c2 (`C2-32`,
  `MIG-18`). Proposition : si `C2-32` est retenu, l'exo devient « enrichir le `ProblemDetail`
  renvoyé pour `HttpMessageNotReadableException` » (`detail` personnalisé, propriété `field`), et
  on inverse le test ; sinon, garder l'exo tel quel.
  **DÉCISION :** ___
  *Vérif :* le test et la slide décrivent le même format de réponse.
- [ ] **T2-22** · **anciens exos 36-40** — les entités de la correction sont des `data class`
  avec les deux côtés non-nullables (`exo71/…/HumanRepository.kt:17`, `C2-23`, `C2-35`,
  `C3-29`, `C3-30`) → dans la correction : `class` + `id` nullable, collections
  `var MutableList` (`C2-28`), et côté inverse nullable.
  **DÉCISION :** ___ (appliquer ou non l'anti-`data class` dès le TD2)
  *Vérif :* correction sans `data class` annotée `@Entity`, ou justification écrite.
- [ ] **T2-23** · **ancien exo 29** — `@ConfigurationProperties` : la correction utilise-t-elle
  `@EnableConfigurationProperties` ou `@ConfigurationPropertiesScan` (`C2-40`) ? Aligner avec ce
  que la slide montrera.
  **DÉCISION :** ___
  *Vérif :* même annotation dans la slide et la correction.
- [ ] **T2-24** · **anciens exos 27 et 32** (bannière, validateur comme bean) — sans slide.
  Proposition : garder l'exo 7 (bannière) comme exo express avec un indice dans l'énoncé ; passer
  l'exo 12 en ⭐ bonus.
  **DÉCISION :** ___
  *Vérif :* table ci-dessus mise à jour.

## 🟡 Énoncé

- [ ] **T2-25** · **`TD2.adoc:1-3`** — en-tête sans rappel du module à ouvrir ni des commandes ;
  il faut le module (`validation` puis `jpa`) à chaque changement (comme au TD1), et les commandes
  `./gradlew :validation:test --tests 'Exo1Test'`.
  *Vérif :* chaque section d'exo cite son module.
- [ ] **T2-26** · **Renumérotation** — appliquer la table (titres, « Les classes de test Exo20 et
  Exo22 de PetControllerTest… » `TD2.adoc:18`, etc.).
  *Vérif :* `grep -n 'Exo2[0-9]\|Exo3[0-9]\|exo 3[0-9]\|exo 40' td2/README.adoc` → vide.
- [ ] **T2-27** · **`TD2.adoc:69-92`** (ancien exo 26) — la consigne « mettre un point d'arrêt dans
  `handleException` » suppose un débogueur (IntelliJ **et** VS Code) : vérifier que la procédure
  fonctionne dans VS Code, sinon ajouter l'alternative « lire la trace avec
  `logging.level.org.springframework.web=DEBUG` ».
  *Vérif :* procédure testée sous VS Code.
- [ ] **T2-28** · **`TD2.adoc:166-174`** (ancien exo 32) — « on ne peut pas utiliser lateinit.
  Il faut fournir une valeur par défaut » : avec l'injection par constructeur (`T0-30`), la
  remarque ne s'applique plus → réécrire selon la correction retenue.
  *Vérif :* énoncé cohérent avec la correction.

## ⭐ Bonus proposés

- [ ] **T2-29** · ⭐ **Profil Postgres** (existant, `TD2.adoc:280-285`) — le garder en bonus
  (Podman optionnel) ; donner la commande `podman run` **et** l'alternative
  `docker compose`, et la version d'image figée (`postgres:18`).
  **DÉCISION :** ___
  *Vérif :* procédure testée.
- [ ] **T2-30** · ⭐ **`@Transactional` et `LazyInitializationException`** — sujet de c3, donc
  plutôt au TD3 ; noté ici pour mémoire (voir `T3-27`).
  **DÉCISION :** ___
  *Vérif :* —
- [ ] **T2-31** · ⭐ **Requêtes dérivées** (`c2/jpa.md:513-766`, jamais pratiquées) —
  `findByNameContainingIgnoreCase`, `findByPetsKind`, avec un test fourni.
  **DÉCISION :** ___
  *Vérif :* test fourni vert sur `correction`.

## 📽 Slides à modifier (amphi 2)

- [ ] **T2-32** · `C2-01`, `C2-02`, `C2-30` (validation sans `@Validated`) : à traiter **avant**
  le TD2, en même temps que `T2-08`.
  *Vérif :* IDs cochés dans `Cours 2.md`.
- [ ] **T2-33** · Nouvelles slides issues des décisions `T2-17` (request params dans un objet),
  `T2-18` (contrainte de classe), `T2-19` (datasource), `T2-20` (`orphanRemoval`),
  `T2-21` (`ProblemDetail`, `C2-32`).
  *Vérif :* chaque décision « slide » a sa slide.

## ✅ Correction (`origin/td3-correction` : 19 premiers commits identiques à `td2-correction`)

- [ ] **T2-34** · Exo 1 ← `3ac584b` · Exo 2 ← `976f240` · Exo 3 ← `7c0ccd1` · Exo 4 ← `f7b3ed6` ·
  Exo 5 ← `dea15c9` · Exo 6 ← `aecd8ae` — à réécrire sans `@Validated` (`T2-08`) et selon `T2-21`.
  *Vérif :* CI `correction` verte.
- [ ] **T2-35** · Exo 7 ← `59d7e28` · Exo 8 ← `efc4e03` · Exo 9 ← `e68ac31` · Exo 10 ← `85c2ea3` ·
  Exo 11 ← `fe3bdfd` · **Exo 12 : aucun commit, à écrire**.
  *Vérif :* idem.
- [ ] **T2-36** · Exo 13 ← `da19138` · Exo 14 ← `cb80fbe` · Exo 15 ← `a4d3322` · Exo 16 ← `d83f350` ·
  Exo 17 ← `bb16f95` · Exo 18 ← `34c6c14` · Exo 19 ← `78d2e75` · Exo 20 ← `96a8d1d`
  (selon `T2-20`, `T2-22`).
  *Vérif :* idem, plus `./gradlew -p td2 test` vert en fin de branche.
- [ ] **T2-37** · Bonus retenus : Postgres (à écrire, profil `application-postgres.yml`),
  `T2-31`.
  *Vérif :* idem.
