---
tags: [spring, kotlin, iut, td, td3]
date: 2026-09-26
cible: spring-boot-4.1
amphi: c3
---

← [[update]]

# TD3 — Sécurité, clients HTTP (amphi 3)

Modules : état final de `td2/jpa` → `td3/security` (anciens exos 41-44, aujourd'hui dans
`exo33`), `exo45` → `td3/client`. Énoncé : `TD3.adoc` → `td3/README.adoc`.
Amphi de référence : `c3` (`di-reminder` → `jpa3` → `transactional` → `rest-client` →
`filters` → `security`).

> [!info] Anomalie de 2025
> Les exos de sécurité se faisaient dans `exo33`, le module du TD2 : c'est le TD4 (`167eb98`)
> qui modifiait `exo33/…/PetControllerTest.kt`, à l'origine de conflits. Avec un module
> `td3/security` dédié, ce problème disparaît.

## Table des exos

Le TD3 correspond à c3. L'ordre ci-dessous suppose que la décision `T3-17` (RestClient d'abord) est acceptée.
Si elle est refusée, on intervertit les blocs 6-9 et 10-13.

| Nouveau | Ancien | Module | Sujet | Slide | Vérification | Socle |
|---|---|---|---|---|---|---|
| 1 | 41 | security | `SecurityFilterChain` : GET libre, le reste authentifié | `c3/security.md:73-185`, `:261-295` | `exo 41` commenté (🔵 `T3-10`) | socle |
| 2 | 42 | security | DELETE réservé à ADMIN | `c3/security.md:261-318` | `exo 40` modifié par l'étudiant | socle |
| 3 | 43 | security | `InMemoryUserDetailsManager` | `c3/security.md:325-390` | manuelle (🔵 `T3-11`) | socle |
| 4 | 44 | security | `creatorLogin` = utilisateur connecté | `c3/security.md:451-486` | `exo 44` + `@WithMockUser` | socle |
| 5 | Bonus | security | `JdbcUserDetailsManager` | `c3/security.md:391-450` | manuelle | ⭐ |
| 6 | 50 | client | RestClient GET | `c3/rest-client.md:397-420` | `Exo50Test.exo50` | socle |
| 7 | 51 | client | RestClient query param | idem | `exo51` | socle |
| 8 | 52 | client | RestClient POST | idem | `exo52` | socle |
| 9 | 53 | client | RestClient 5xx → `MyInternalError` | `c3/rest-client.md:171-245` | `exo53` | socle |
| 10 | 45 | client | WebClient GET | `c3/rest-client.md:252-388` | `Exo45Test.exo45` | ⭐ proposé |
| 11 | 46 | client | WebClient query param | idem | `exo46` | ⭐ proposé |
| 12 | 47 | client | WebClient POST | idem | `exo47` | ⭐ proposé |
| 13 | 48 | client | WebClient 5xx | idem | `exo48` | ⭐ proposé |

## 🔴 Erreurs

- [ ] **T3-01** · **module de départ `td3/security`** — il doit partir de l'état final de
  `td2/jpa` **sans** sécurité. Aujourd'hui, le seul état final disponible (`exo71/src/main`)
  contient déjà la correction de l'ancien exo 44 (`Principal`, `creatorLogin`). → générer le
  squelette à partir du dernier commit de correction du TD2 (`T2-36`), puis ajouter les tests
  des exos 1-4.
  *Vérif :* `./gradlew -p td3 :security:compileTestKotlin` vert ; aucun `Principal` dans `src/main`.
- [ ] **T3-02** · **`exo33/build.gradle.kts:22-42`** — il n'y a ni starter security, ni
  `spring-security-test`, alors que les anciens exos 42 et 44 utilisent `@WithMockUser` : le
  squelette ne peut pas compiler les tests → ajouter `spring-security-test` au squelette (la
  dépendance « starter security » reste à ajouter par l'étudiant à l'exo 1, comme le dit
  l'énoncé). À vérifier : `@WithMockUser` sans le starter.
  *Vérif :* le squelette compile ; `grep starter-security td3/security/build.gradle.kts` → vide
  sur `main`.
- [ ] **T3-03** · **correction `be613b2`… `SecurityConfig.kt:3`** — import parasite
  `org.springframework.boot.webservices.client.WebServiceMessageSenderFactory.http` (ajouté
  automatiquement par l'IDE), qui ne compile plus en Boot 4 → le supprimer.
  *Vérif :* compile.
- [ ] **T3-04** · **correction `70ca916` (Exo44.5, bonus JDBC)** — elle remplace la règle finale
  par `authorize(anyRequest, permitAll) // for h2-console`, ce qui **annule** l'exo 1
  (« le reste nécessite une authentification ») → autoriser seulement `/h2-console/**`.
  *Vérif :* le test de l'exo 1 reste vert après le commit du bonus.
- [ ] **T3-05** · **correction `f786f72` (Exo46)** — le query param envoyé est `name=world`,
  alors que le test et l'énoncé attendent `World` → aligner (à reproduire sur l'exo 7).
  *Vérif :* test de l'exo 7 vert.
- [ ] **T3-06** · **`exo45/src/test/kotlin/iut/nantes/exo45/Exo50Test.kt:15`** — il importe à la
  fois `Assertions.assertThrows` (JUnit) et `assertThrows` (Kotlin) → n'en garder qu'un.
  *Vérif :* compile sans ambiguïté.
- [ ] **T3-07** · **`exo45/build.gradle.kts:27-46`** — `flyway-core`, `h2`, le plugin JPA et
  `kotlin-logging` ne servent à rien ; le starter security et `@WithMockUser` dans les tests du
  client ne servent pas l'exo ; MockServer est déclaré en `implementation` → nettoyer
  (`T0-14`), MockServer remplacé par l'outil de `T0-03` en `testImplementation`.
  *Vérif :* `./gradlew -p td3 :client:dependencies --configuration runtimeClasspath | grep -c mockserver` = 0.
- [ ] **T3-08** · **ports** — `application.yml` principal : `:8081`, tests : `:8888` → documenter
  dans l'énoncé (ou utiliser un port aléatoire fourni par l'outil de `T0-03` via
  `@DynamicPropertySource`, qui évite aussi les conflits de port).
  *Vérif :* deux exécutions de tests en parallèle ne se gênent pas.

## 🟠 Migration 4.1

- [ ] **T3-09** · **`exo45/…/HttpClientConfig.kt:16`, `:24`** — injection de
  `RestClient.Builder` et `WebClient.Builder` : en Boot 4, ces beans viennent des modules
  `restclient` et `webclient` → vérifier ce qu'apportent `starter-webmvc` et `starter-webflux`.
  Profiter de la migration pour évaluer `@ImportHttpServices` et `spring.http.clients.*`
  (`MIG-14`, `MIG-15`, `C3-33`, `C3-47`).
  *Vérif :* `@SpringBootTest` avec le profil `restclient` démarre.

## 🔵 Autonomie / tests

- [ ] **T3-10** · **`exo33/…/PetControllerTest.kt:94-128`** — les tests `exo 40` et `exo 41`
  sont commentés, et `@WithMockUser` de `exo 44` aussi (`:130`, sans import). Dans un module
  dédié, le starter security s'ajoute à l'exo 1 : ces tests peuvent être actifs dès le départ
  s'ils ne dépendent que de `spring-security-test` (`T3-02`) → les décommenter et écrire un test
  par exo (401 sans utilisateur, 403 avec USER, 204 avec ADMIN).
  *Vérif :* sur `main`, les tests compilent et sont rouges ; sur `correction`, ils sont verts exo après exo.
- [ ] **T3-11** · **ancien exo 43** — vérification seulement manuelle (« curl, bruno ») → une
  collection Bruno (`T0-17`) avec basic auth `user`/`admin` et les statuts attendus (401, 403, 204).
  *Vérif :* `bru run --env local` vert sur `correction`.
- [ ] **T3-12** · **exo 2 (ancien 42)** — l'étudiant doit modifier le test de l'exo 40 pour
  qu'il n'échoue plus avec un 403 : c'est un exercice d'écriture de test (qu'on garde) → écrire
  dans l'énoncé ce qui est attendu (« ajoutez `@WithMockUser(roles = ["ADMIN"])` »), en
  indice repliable.
  *Vérif :* énoncé relu.

## 🔀 Cohérence avec les slides

- [ ] **T3-13** · **exo 1** — la slide enseigne un ordre de règles faux (`C3-12`), et
  `formLogin` transforme les 401 en 302 (`AV-2`, `C3-21`). Le test attend 401. Proposition :
  corriger la slide, pas de `formLogin` dans la correction (API REST, `httpBasic` seul),
  CSRF désactivé en le justifiant (API stateless, `C3-45`, `C3-50`).
  **DÉCISION :** ___
  *Vérif :* `AV-2` tranché ; test 401 vert.
- [ ] **T3-14** · **exo 3** — la slide donne le rôle ADMIN aux deux utilisateurs
  (`C3-17`) → corriger la slide (USER / ADMIN) : c'est exactement ce que l'exo fait construire.
  *Vérif :* `C3-17` coché.
- [ ] **T3-15** · **exo 4** — la correction utilise `Principal`, alors que la relecture recommande
  `@AuthenticationPrincipal` (`C3-44`). Proposition : accepter les deux dans le test (il ne
  vérifie que `creatorLogin`) ; la correction montre `@AuthenticationPrincipal`.
  **DÉCISION :** ___
  *Vérif :* correction alignée sur la slide.
- [ ] **T3-16** · **`@EnableWebSecurity`** — la correction l'utilise, alors qu'il est inutile avec
  Boot (`C3-35`) → le retirer de la correction.
  *Vérif :* `grep EnableWebSecurity` → vide sur `correction`.
- [ ] **T3-17** · **ordre RestClient / WebClient** — le TD fait WebClient d'abord (anciens exos
  45-48) et RestClient en « refaire la même chose » (50-53). Or Boot 4 recommande RestClient, et la
  relecture propose d'inverser le chapitre (`C3-34`). Proposition : RestClient au socle
  (exos 6-9), WebClient en bonus (exos 10-13).
  **DÉCISION :** ___
  *Vérif :* ordre identique dans l'énoncé et dans `c3/rest-client.md`.
- [ ] **T3-18** · **exos 9 / 13 (5xx)** — la slide enseigne un ordre de `onStatus` qui avale le
  404 (`C3-24`) et lance `HttpClientErrorException` sur un 5xx (`C3-23`) → corriger les slides
  avant le TD ; l'exo ne teste que le 5xx : ajouter un cas 404 pour que l'erreur de la slide
  soit détectée.
  **DÉCISION :** ___
  *Vérif :* test 404 présent et vert sur `correction`.
- [ ] **T3-19** · **exos 10-13** — `block()` renvoie un type nullable (`C3-25`) :
  `WebClientService.hello(): HelloDto?` est cohérent, il faut le dire dans la slide.
  *Vérif :* `C3-25` coché.
- [ ] **T3-20** · **tests de client HTTP** — aucune slide ne montre comment tester un client
  (`C3-46`), alors que tout le TD s'appuie dessus → une slide avec l'outil retenu en `T0-03`.
  **DÉCISION :** ___
  *Vérif :* slide présente, même outil que dans les tests fournis.

## 🟡 Énoncé

- [ ] **T3-21** · **`TD3.adoc`** — pas d'en-tête `== TD3`, pas de module indiqué (sécurité dans
  `exo33`, clients dans `exo45`), pas de retour à la ligne final → en-tête, module par section,
  commandes Gradle.
  *Vérif :* rendu GitHub.
- [ ] **T3-22** · **`TD3.adoc:1-7`** (ancien exo 41) — « Les exos précédents sont KO » →
  ne s'applique plus dans un module dédié ; indiquer à la place quels tests passent au rouge et
  pourquoi (401 sur les écritures).
  *Vérif :* énoncé cohérent avec les tests.
- [ ] **T3-23** · **`TD3.adoc:69-71`** — « Exo 50 -> 53 : faire les exos 45 à 48 mais dans la
  classe RestClientService » → un exo par section, avec son test (convention `T0-16`).
  *Vérif :* 4 sections distinctes.
- [ ] **T3-24** · **Renumérotation** — appliquer la table, y compris dans les noms de tests
  (`exo45()` … `exo53()`, classes `Exo45Test`, `Exo50Test`).
  *Vérif :* `grep -rn 'exo4[0-9]\|exo5[0-3]' td3/` → vide.
- [ ] **T3-25** · **ancien exo 44** — l'énoncé suppose que `HumanDto` gagne un champ
  `creatorLogin` avec une valeur par défaut, ce qui ne casse pas les tests existants : le
  préciser.
  *Vérif :* tests des exos 1-3 toujours verts après l'exo 4.

## ⭐ Bonus proposés

- [ ] **T3-26** · ⭐ **`JdbcUserDetailsManager`** (existant) — garder, en corrigeant `T3-04` ;
  le `schema.sql` est fourni dans l'énoncé (aujourd'hui il traîne dans `exo65` et `exo71`).
  **DÉCISION :** ___
  *Vérif :* commit de bonus vert.
- [ ] **T3-27** · ⭐ **`@Transactional` / `LazyInitializationException`** — c3 y consacre un
  chapitre entier (`transactional.md`) sans aucun exo. Proposition : dans `td3/security`
  (le module JPA), un test fourni qui provoque la `LazyInitializationException` sur
  `human.pets`, que l'étudiant corrige (`@Transactional(readOnly = true)`, `C3-41`, ou fetch
  join). Attention au piège des proxies Kotlin (`C3-39`).
  **DÉCISION :** ___
  *Vérif :* test rouge sur `main`, vert sur `correction`.
- [ ] **T3-28** · ⭐ **Filtre `OncePerRequestFilter`** (`C3-42`) — ajouter un header
  `X-Request-Id` à chaque réponse ; testé par MockMvc. Fait le lien filtres → sécurité (`C3-49`).
  **DÉCISION :** ___
  *Vérif :* test fourni vert sur `correction`.
- [ ] **T3-29** · ⭐ **HTTP interface / `@ImportHttpServices`** (`MIG-14`, `C3-33`) — refaire le
  client Hello en `@HttpExchange` ; mêmes tests.
  **DÉCISION :** ___
  *Vérif :* idem.

## 📽 Slides à modifier (amphi 3)

- [ ] **T3-30** · À faire **avant** le TD3 : `C3-12` (ordre des règles), `C3-17` (utilisateurs
  in-memory), `C3-23`, `C3-24` (gestion d'erreur des clients), `C3-34` (ordre RestClient d'abord),
  `C3-46` (tester un client), `C3-35` (`@EnableWebSecurity`).
  *Vérif :* IDs cochés dans `Cours 3.md`.

## ✅ Correction (`origin/td3-correction`, commits 20 à 28)

- [ ] **T3-31** · Exo 1 ← `be613b2` (`T3-03`, `T3-13`, `T3-16`) · Exo 2 ← `fe9c6e4` ·
  Exo 3 ← `6801771` · Exo 4 ← `c23eb44` (`T3-15`) · Exo 5 ← `70ca916` (`T3-04`).
  *Vérif :* CI `correction` verte.
- [ ] **T3-32** · Exos 6-9 (RestClient) ← `4c74c48` (« Exo50-53 », à **découper** en 4 commits) ·
  Exo 13 : la correction de l'ancien exo 48 (`WebClientService.error`) est cachée dans `4c74c48`
  → l'extraire.
  *Vérif :* 4 commits RestClient, chacun ne fait passer au vert que son test.
- [ ] **T3-33** · Exo 10 ← `891b087` · Exo 11 ← `f786f72` (`T3-05`) · Exo 12 ← `60ea9e7` ·
  Exo 13 ← partie de `4c74c48`.
  *Vérif :* `./gradlew -p td3 test` vert en fin de branche.
- [ ] **T3-34** · Bonus retenus (`T3-27`, `T3-28`, `T3-29`) : un commit chacun.
  *Vérif :* idem.
