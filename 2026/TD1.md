---
tags: [spring, kotlin, iut, td, td1]
date: 2026-09-26
cible: spring-boot-4.1
amphi: c1
---

← [[update]]

# TD1 — DI, Spring Boot, MVC, tests (amphi 1)

Modules : `exo1` → `td1/di`, `exo10` → `td1/web`. Énoncé : `TD1.adoc` → `td1/README.adoc`.
Amphi de référence : `c1` (`jee` → `history` → `di` → `springboot` → `mvc` → `test`).

## Table des exos

Le TD1 correspond entièrement à c1. Seule différence d'ordre : l'énoncé fait des tests (anciens exos 6 et 9)
avant le MVC (ancien exo 10), alors que c1 enseigne mvc → test. Comme les deux chapitres sont dans le même amphi, ce n'est pas bloquant.

| Nouveau | Ancien | Module | Sujet | Slide | Vérification | Socle |
|---|---|---|---|---|---|---|
| 1 | 0 | di | `ListDatabase` | `c1/di.md:10-250` | 🔵 à créer (`T1-12`) | socle |
| 2 | 1 | di | `@Bean` + `AnnotationConfigApplicationContext` | `c1/di.md:255-360` | `exo1_1` (à compléter par l'étudiant) | socle |
| 3 | 2 | di | singleton partagé | `c1/di.md:539-610` | `exo1_2` | socle |
| 4 | 3 | di | scope prototype | `c1/di.md:593-646` | `exo1_3` | socle |
| 5 | 4 | di | injection automatique | `c1/di.md:748-810` | `exo1_3` | socle |
| 6 | 5 | di | `@Repository` + `@ComponentScan` | `c1/di.md:818-930`, `c1/springboot.md:137-153` | `exo1_3` | socle |
| 7 | 6 | di | tests unitaires de `ListDatabase` | `c1/test.md:28-111` | écrit par l'étudiant | socle |
| 8 | 6.5 | di | `HashDatabase` | — | tests de l'exo 7 | socle |
| 9 | 7 | di | bean singleton `HashDatabase` | `c1/di.md:1132-1170` | snippet de l'énoncé → 🔵 `T1-13` | socle |
| 10 | 8 | di | Spring Boot | `c1/springboot.md:6-110` | snippet de l'énoncé → 🔵 `T1-13` | socle |
| 11 | 9 | di | mock avec springmockk | `c1/test.md:177-390` | snippet de l'énoncé → 🔵 `T1-13` | socle |
| 12 | 10 | web | `HelloController` | `c1/mvc.md:6-115` | curl → 🔵 `T1-14` | socle |
| 13 | 11 | web | POST → 201 | `c1/mvc.md:157-226` | 🔵 `T1-14` | socle |
| 14 | 12 | web | POST → 409 | `c1/mvc.md:198-200` | 🔵 `T1-14` | socle |
| 15 | 13 | web | GET liste | `c1/mvc.md:139-150` | `demoGet` → 🔵 `T1-14` | socle |
| 16 | 14 | web | GET unitaire → 404 | `c1/mvc.md:188-226` | 🔵 `T1-14` | socle |
| 17 | 15 | web | PUT → 400/404 | `c1/mvc.md:218-222` | 🔵 `T1-14` | socle |
| 18 | 16 | web | DELETE → 204/404 | `c1/mvc.md:81` | 🔵 `T1-14` | socle |
| 19 | 17 | web | filtre `?rating` | `c1/mvc.md:125-137` (🔀 `T1-19`) | écrit par l'étudiant (exo 21) | socle |
| 20 | 18 | web | `Accept-Language` | `c1/mvc.md:170-186` | écrit par l'étudiant (exo 22) | socle |
| 21 | 19 | web | MockMvc + `@SpringBootTest` | `c1/test.md:396-560` | écrit par l'étudiant | socle |
| 22 | 20 | web | `@WebMvcTest` | `c1/test.md:576-648` | écrit par l'étudiant | socle |

## 🔴 Erreurs

- [x] **T1-01** · **`exo1/src/test/kotlin/iut/nantes/Exercies.kt:9`** — la classe s'appelle
  `Exercies`, alors que l'énoncé écrit `Exercice#exo1_1` (`TD1.adoc:31`, `:42`, `:51`) →
  renommer la classe selon la convention `T0-16` (`Exo2Test`… ou `@Nested`) et aligner l'énoncé.
  *Vérif :* `grep -rn 'Exercies\|Exercice#' td1/` → vide.
- [x] **T1-02** · **`exo10/src/main/kotlin/iut/nantes/Movie.kt:8-20`** — le film s'appelle
  `"My Little Pony: The Movie"`, alors que `FR_FR` et `FR_CA` ont pour clé `"My Little Pony"` : la
  traduction ne marche jamais sur les données de départ. De plus, les exemples de l'énoncé
  (`TD1.adoc:364-437`) mélangent les deux titres → choisir un titre unique, l'utiliser comme clé
  des deux maps et dans l'énoncé.
  *Vérif :* le test de l'exo 20 (`T1-14`) avec `Accept-Language: fr-FR` renvoie le titre traduit.
- [x] **T1-03** · ⚪ **`exo10/src/main/kotlin/iut/nantes/Movie.kt:19`** — `"Origine "` a une
  espace finale → la supprimer.
  *Vérif :* `grep -n '" *"' Movie.kt` → rien.
- [x] **T1-04** · **`TD1.adoc:158`** — « Lancer **Application.kt » (gras non fermé), alors
  que le fichier est `Exo10Application.kt` → corriger le nom et donner l'équivalent en CLI
  (`./gradlew :web:bootRun`).
  *Vérif :* la commande de l'énoncé démarre l'application.
- [x] **T1-05** · **correction `6de38b3` (Exo16)** — le DELETE d'un film absent renvoie 400
  au lieu de 404 → corriger dans le commit de correction de l'exo 18.
  *Vérif :* le test DELETE 404 de `T1-14` est vert sur `correction`.
- [x] **T1-06** · **`exo1/src/main/kotlin/iut/nantes/UserService.kt:15-17` et `:23-25`** —
  `update` et `findAll(name)` valent `TODO()` sans qu'aucun exo ne les utilise, alors que
  `SuperUserService.findAll()` est appelé → retirer ces méthodes du squelette ou les faire
  implémenter dans un exo.
  **DÉCISION :** implémentées par délégation à `Database` (comme `save`/`delete`), plus de `TODO()`.
  *Vérif :* aucun `TODO()` restant dans `td1/di/src/main` sur `correction`.

## 🟠 Migration 4.1

- [x] **T1-07** · **`exo1/build.gradle.kts`, `exo10/build.gradle.kts`** — appliquer `T0-12`
  et `T0-13` (`starter-web` → `starter-webmvc`, springmockk 5.x).
  *Vérif :* `./gradlew -p td1 compileTestKotlin`.
- [x] **T1-08** · **`exo10/src/test/kotlin/iut/nantes/MovieControllerTest.kt:5`** — import de
  `AutoConfigureMockMvc` à mettre à jour (`MIG-12`) ; ligne 8 : import inutilisé
  `RequestEntity.post` → le supprimer.
  *Vérif :* compilation sans warning d'import.
- [ ] **T1-09** · **`TD1.adoc:98-138`** (anciens exos 8-9) — les coordonnées demandées sont
  `mockk-jvm:1.14.6` et `springmockk:4.0.2`, alors que les modules utilisent `io.mockk:mockk` →
  donner les coordonnées et versions de `T0-01`/`T0-02`, et préciser `testImplementation`.
  *Vérif :* copier-coller les lignes de l'énoncé dans `td1/di/build.gradle.kts` → compile.
- [ ] **T1-10** · **`exo1/build.gradle.kts:4`** — le plugin Boot est appliqué alors que le
  module n'a que `spring-context` jusqu'à l'ancien exo 8 : vérifier que `bootJar` ne casse pas
  le build tant qu'il n'y a pas de `main` Spring Boot (sinon, n'appliquer le plugin qu'à l'exo 10,
  via l'énoncé).
  *Vérif :* `./gradlew -p td1 :di:build -x test` vert sur `main`.
- [ ] **T1-11** · **`exo10/build.gradle.kts:23`** — `spring-boot-starter` est redondant avec
  `starter-webmvc` → le supprimer.
  *Vérif :* compile.

## 🔵 Autonomie / tests

- [x] **T1-12** · **ancien exo 0** — aucun test ne vérifie `ListDatabase` seule avant l'exo 2
  → fournir `Exo1Test` (save / findOne / delete / findAll) qui instancie `ListDatabase()`.
  Le squelette ne contenant pas la classe, le test ne compile pas → fournir
  `class ListDatabase : Database` avec des `TODO()`.
  *Vérif :* `Exo1Test` rouge sur `main`, vert sur le commit de l'exo 1.
- [x] **T1-13** · **`TD1.adoc:79-138`** (anciens exos 7, 8, 9) — les tests sont donnés en
  snippet dans l'énoncé → les fournir dans le module (`Exo9Test`, `Exo10Test`, `Exo11Test`).
  Problème : `Exo9Test` référence `AppConfig` et `HashDatabase`, créés par l'étudiant, et
  `Exo10Test`/`Exo11Test` (`@SpringBootTest`, `@MockkBean`) exigent les dépendances de l'exo 10 :
  les trois cassent la compilation du module s'ils sont fournis → **DÉCISION :** les trois restent en
  snippet dans l'énoncé (« créez `Exo9Test.kt` avec ce contenu »).
  *Vérif :* `Exo9Test`, `Exo10Test` et `Exo11Test` verts sur `correction`.
- [x] **T1-14** · **`exo10/src/test/kotlin/iut/nantes/MovieControllerTest.kt`** — décision
  Q2.4 (a) : fournir les tests MockMvc des nouveaux exos 12 à 18 (hello, POST 201, POST 409,
  GET liste, GET 200/404, PUT 200/400/404, DELETE 204/404), une classe ou un `@Nested` par exo.
  Ils servent d'exemples pour les exos 21-22, où l'étudiant écrit lui-même les tests des
  exos 19-20. Supprimer le commentaire « FOR EXO 19 » (`:13-22`).
  *Vérif :* 7 tests rouges sur `main`, verts sur le commit de l'exo 18.
- [x] **T1-15** · **anciens exos 17-18** — en complément (si `T0-04` OK), une collection
  Bruno `td1/web/bruno/` qui appelle `?rating=99` et `Accept-Language: fr-FR` avec assertions,
  pour que l'étudiant vérifie son code **avant** d'écrire ses propres tests aux exos 21-22.
  **DÉCISION :** abandonné — les `curl` de l'énoncé et les tests de `T1-14` couvrent déjà ces vérifications ; Bruno ajouterait un outil à installer pour deux requêtes.
  *Vérif :* `bru run --env local` vert sur `correction`.

## 🔀 Cohérence avec les slides

- [x] **T1-16** · **ancien exo 4 (`TD1.adoc:55-59`)** — l'énoncé fait supprimer le constructeur et
  injecter avec `@Autowired` sur un champ, alors que c1 présente désormais l'injection par
  constructeur comme la norme (`C1-29`, `C1-41`). Proposition : l'exo devient « annoter
  `SuperUserService` en `@Service` et laisser Spring injecter `Database` par le constructeur » ;
  l'injection par champ n'est plus citée qu'en remarque (« existe, à éviter »).
  **DÉCISION :** l'exo est conservé tel quel : l'objectif est que l'étudiant expérimente l'injection par champ. Ajout d'une remarque dans l'énoncé (« on préfère le constructeur en pratique »).
  *Vérif :* la remarque est présente dans l'énoncé de l'exo 4 ; la correction de l'exo 4 garde l'injection par champ.
- [x] **T1-17** · **ancien exo 11 (`TD1.adoc:185-198`)** — l'énoncé demande un 201, mais c1 ne
  montre ni `ResponseEntity.created()` ni le header `Location` (`C1-21`). Proposition : ajouter la
  slide (`C1-21`) et exiger `Location` dans le test de `T1-14`.
  **DÉCISION :** `Location` exigé. Énoncé et test POST faits ; slide `C1-21` faite (`iut-spring-kotlin`) ; reste la correction de l'exo 13 (`345da84`, `T1-33`) à mettre à jour.
  *Vérif :* le test POST vérifie `header { string("Location", …) }`.
- [x] **T1-18** · **`/api/movies`** — c1 ne montre pas `@RequestMapping` au niveau de la
  classe (`C1-22`), que la correction utilise. Proposition : ajouter la slide `C1-22` et
  l'indiquer en indice dans l'énoncé.
  **DÉCISION :** slide `C1-22` ajoutée (`iut-spring-kotlin`) et indice (TIP) ajouté dans l'énoncé.
  *Vérif :* la slide `C1-22` est cochée.
- [ ] **T1-19** · **ancien exo 17 (`TD1.adoc:364`)** — le filtre `?rating` suppose un
  `@RequestParam` optionnel, absent de c1 (`C1-23`) → ajouter la slide `C1-23` (`required = false`
  / type nullable), ou donner l'indice dans l'énoncé.
  **DÉCISION :** ___
  *Vérif :* l'étudiant trouve l'information dans la slide ou l'énoncé.
- [ ] **T1-20** · **ancien exo 8** — `@SpringBootTest` ne fonctionne que si le test est dans le
  package de l'application ou un sous-package, ce que c1 signale trop tard (`C1-38`) →
  l'ajouter à l'énoncé (encadré « Attention ») et traiter `C1-38`.
  *Vérif :* encadré présent dans `td1/README.adoc`.
- [ ] **T1-21** · **ancien exo 20 (`TD1.adoc:447`)** — la slide `@WebMvcTest` mock désormais un
  Service (`C1-12`), mais le module `web` n'a pas de service (le contrôleur utilise `Database`
  directement). Proposition : ajouter un `MovieService` au squelette, ou faire mocker
  `Database` dans l'exo (le nom n'entre plus en collision avec l'interface Spring `Repository`).
  **DÉCISION :** ___
  *Vérif :* le test `@WebMvcTest` de la correction mocke le même type de bean que la slide.
- [ ] **T1-22** · **exos 21-22** — `MockMvcTester` / `RestTestClient` (`C1-19`, `MIG-16`) :
  garder le DSL Kotlin `MockMvc` (cohérent avec les slides et les tests fournis), citer
  `MockMvcTester` en remarque.
  **DÉCISION :** ___
  *Vérif :* énoncé et slides utilisent la même API.

## 🟡 Énoncé

- [ ] **T1-23** · **`TD1.adoc:1-19`** — le clone pointe vers `2025-iut-td` et le proxy se règle
  dans un `gradle.properties` à la racine → remplacer par le dépôt `td1` et
  `~/.gradle/gradle.properties` (`T0-06`, `T0-11`).
  *Vérif :* instructions suivies sur un poste vierge.
- [ ] **T1-24** · **Renumérotation** — appliquer la table ci-dessus aux titres `== Exo N`, aux
  renvois internes (« Le test exo1_3 doit toujours fonctionner », « Dans la classe Exo8 ») et
  aux noms de tests.
  *Vérif :* `grep -n 'Exo [0-9]' td1/README.adoc` ne cite que les nouveaux numéros.
- [ ] **T1-25** · **`TD1.adoc:69-77`** (anciens exos 6 et 6.5) — préciser où écrire les tests
  (`src/test/kotlin/…/ListDatabaseTest.kt`), comment mesurer la couverture en CLI (plugin
  JaCoCo ? IntelliJ seulement ?) et, pour l'exo 8, comment réutiliser les tests (classe
  abstraite paramétrée).
  **DÉCISION :** ___ (JaCoCo dans le build ?)
  *Vérif :* un étudiant VS Code peut mesurer la couverture.
- [ ] **T1-26** · **`TD1.adoc:139-153`** (ancien exo 9) — le snippet n'a pas de langage
  (` ``` ` nu) et le `// GIVEN TODO` n'indique pas ce qu'il faut mocker (`every { database.delete(...) } throws …`)
  → donner un indice repliable (`[%collapsible]`) comme au TD2.
  *Vérif :* rendu GitHub.

## ⭐ Bonus proposés

- [ ] **T1-27** · ⭐ **Injection d'une `List<Database>`** (`C1-25`) — bean qui reçoit toutes
  les implémentations et les interroge toutes ; suivi de `@Primary` / `@Qualifier` pour
  choisir l'une d'elles. Couvre `c1/di.md:993-1030`, jamais pratiqué.
  **DÉCISION :** ___
  *Vérif :* test fourni vert sur `correction`.
- [ ] **T1-28** · ⭐ **Détection d'une dépendance circulaire** (`C1-26`) — deux services qui se
  référencent ; l'étudiant lit l'erreur au démarrage et la corrige.
  **DÉCISION :** ___
  *Vérif :* test `assertThrows<BeanCurrentlyInCreationException>` (ou équivalent 4.1).

## 📽 Slides à modifier (amphi 1)

- [ ] **T1-29** · `C1-21` (201 + `Location`), `C1-22` (`@RequestMapping` au niveau de la classe),
  `C1-23` (`@RequestParam` optionnel) et `C1-38` (sous-package) conditionnent directement les
  exos 13, 15-16, 19 et 10 → à traiter **avant** le TD1.
  *Vérif :* les 4 IDs sont cochés dans `Cours 1.md`.
- [ ] **T1-30** · `MIG-10` — la dépendance springmockk n'est citée dans aucune slide, alors que
  l'exo 11 la fait ajouter → la montrer dans `c1/test.md` (bloc `build.gradle.kts`).
  *Vérif :* slide présente.

## ✅ Correction (`origin/td1-correction`, base `f5be5ba`)

Un commit par exo. Hash d'origine → nouveau commit :

- [ ] **T1-31** · Exo 1 ← `31edc0e` (Exo0) · Exo 2 ← `88662b8` · Exo 3 ← `2024f3c` ·
  Exo 4 ← `63ed3d6` · Exo 5 ← `d5dc730` · Exo 6 ← `03b9f7a`.
  *Vérif :* CI `correction` verte sur ces 6 commits.
- [ ] **T1-32** · Exo 7 ← `23bb822` · Exo 8 ← `7d17880` · Exo 9 ← `53061e3` ·
  Exo 10 ← `036215f` · Exo 11 ← `e2e2caa` (mettre à jour pour springmockk 5).
  *Vérif :* idem.
- [ ] **T1-33** · Exo 12 ← `3dcd901` · Exo 13 ← `345da84` · Exo 14 ← `aa4c873` ·
  Exo 15 ← `0dc2ca6` · Exo 16 ← `d47475a` · Exo 17 ← `e0a4bfb` · Exo 18 ← `6de38b3` (404, `T1-05`) ·
  Exo 19 ← `9f99a5f` · Exo 20 ← `003ea84`.
  *Vérif :* les tests de `T1-14` passent au vert commit après commit.
- [ ] **T1-34** · Exo 21 ← `2851ebf` (renomme `MovieControllerTest` en `MovieControllerTestIT` :
  à adapter, les tests fournis gardent leur nom) · Exo 22 ← `8e0d917`
  (`@WebMvcTest` + `@MockkBean`, selon `T1-21`).
  *Vérif :* idem, plus `./gradlew -p td1 test` vert en fin de branche.
- [ ] **T1-35** · Bonus retenus (`T1-27`, `T1-28`) : un commit chacun, **après** l'exo 22.
  *Vérif :* idem.
