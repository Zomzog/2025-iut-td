---
tags: [spring, kotlin, iut, td, td4]
date: 2026-09-26
cible: spring-boot-4.1
amphi: c4
---

← [[update]]

# TD4 — Flyway, Testcontainers, observabilité, réactif (amphi 4)

Modules : `exo60` → `td4/flyway`, `exo65` → `td4/observability`, `exo71` → `td4/reactive`.
Énoncé : `TD4.adoc` → `td4/README.adoc`.
Amphi de référence : `c4` (`auto-configuration` → `flyway` → `caching` → `messaging` →
`integration-testing` → `observability` (`logs`, `actuators`, `telemetry`) → `reactive`).

> [!warning] Socle léger une fois Docker retiré
> Testcontainers (anciens exos 62-63) passe en bonus, puisque Podman est optionnel sur les postes. Le réactif (ancien exo 71) est
> à revoir. Il reste alors 10 exos au socle, courts pour la plupart : les bonus de cette fiche
> (surtout le cache, `T4-29`) comptent donc davantage qu'ailleurs.

## Table des exos

Le TD4 correspond à c4, sans décalage. c4 contient en plus des sujets non pratiqués : auto-configuration, cache, messaging et télémétrie.

| Nouveau | Ancien | Module | Sujet | Slide | Vérification | Socle |
|---|---|---|---|---|---|---|
| 1 | 60 | flyway | migration V1 « init » | `c4/flyway.md:66-218` | `Exo60Test.exo60` (H2) | socle |
| 2 | 61 | flyway | V2 : `color` + `age` | `c4/flyway.md:168-197` | `Exo60Test.exo61` | socle |
| 3 | 62 | flyway | Testcontainers par propriété `jdbc:tc:` | `c4/integration-testing.md:156-175` | `Exo62Test` (Docker) | ⭐ |
| 4 | 63 | flyway | `@ServiceConnection` | `c4/integration-testing.md:35-146` | `Exo64Test` (Docker, 🔴 `T4-03`) | ⭐ |
| 5 | 65 (1ʳᵉ occurrence) | observability | kotlin-logging | `c4/logs.md:193-254` | 🔵 `T4-13` | socle |
| 6 | 65 (2ᵉ occurrence) | observability | `logback-spring.xml` | `c4/logs.md:47-146`, `:256-293` | 🔵 `T4-13` | socle |
| 7 | 66 (1ʳᵉ occurrence) | observability | DEBUG en profil `dev` | `c4/logs.md:278-323` | 🔵 `T4-13` | socle |
| 8 | 66 (2ᵉ occurrence) | observability | actuator `/info` | `c4/actuators.md:26-93` | `Exo66Test.exo66` | socle |
| 9 | 67 | observability | `app-name` dans `/actuator/info` | `c4/actuators.md:135-169` (🔀 `T4-19`) | `Exo66Test.exo67` | socle |
| 10 | 68 | observability | `/actuator/prometheus` | `c4/actuators.md:176-236` | manuelle → 🔵 `T4-14` | socle |
| 11 | 69 | observability | compteur `logCall` | `c4/actuators.md:238-286` | 🔵 `T4-14` | socle |
| 12 | 70 | observability | tag `result` | idem | 🔵 `T4-14` | socle |
| 13 | 71 | reactive | WebFlux + coroutines | `c4/reactive.md:1-88` (2 slides) | test cassé (🔴 `T4-01`) | à décider (`T4-21`) |

## 🔴 Erreurs

- [ ] **T4-01** · **`exo71/src/test/kotlin/iut/nantes/exo33/controller/PetControllerTest.kt:1-3`** —
  test dans le package `iut.nantes.exo33`, qui importe `iut.nantes.exo33.DatabaseProxy` (absent du
  module) : **le module ne compile pas** ; et même corrigé, `@SpringBootTest` ne trouverait pas
  `Exo71Application`. De plus, `application.yml:2` s'appelle `exo33` → à refaire selon la
  décision `T4-21`.
  *Vérif :* `./gradlew -p td4 :reactive:compileTestKotlin` vert.
- [ ] **T4-02** · **`exo65/src/test/kotlin/iut/nantes/exo65/PrometheusTestConfig.kt:3-4`** —
  importe `io.micrometer.prometheusmetrics`, dépendance que l'étudiant n'ajoute qu'à
  l'ancien exo 68 : **aucun test du module ne compile** avant l'exo 10, y compris ceux des exos 8-9
  → supprimer ce fichier du squelette (le test Prometheus est fourni en snippet à l'exo 10, ou
  vérifie `/actuator/prometheus` par MockMvc sans importer la classe).
  *Vérif :* `./gradlew -p td4 :observability:compileTestKotlin` vert sur `main`.
- [ ] **T4-03** · **`exo60/src/test/kotlin/iut/nantes/exo60/Exo64Test.kt:28-34`** — la classe teste
  l'ancien exo 63 mais s'appelle `Exo64Test`, et elle contient **déjà** `@ServiceConnection` : l'exo
  est résolu d'avance. En plus, `@Container` est posé sans `@JvmStatic` dans le `companion object`
  (`C4-17`), avec l'image `postgres:latest` (`C4-20`). → Le test ne doit pas contenir la solution
  (l'étudiant écrit la déclaration du conteneur, ou le test en dépend via une classe de config
  qu'il crée) ; `@JvmStatic` ; image figée.
  **DÉCISION :** ___ (comment tester l'exo 4 sans donner la solution)
  *Vérif :* sur `main`, le test échoue faute de conteneur ; sur `correction`, il est vert avec `-Pdocker`.
- [ ] **T4-04** · **`exo60/…/Exo64Test.kt:53-57`** — le test exige exactement 2 migrations et la
  description `"add age"` pour la V2, ce que l'énoncé ne dit pas (`TD4.adoc:17-21`) → donner le nom
  de fichier attendu dans l'énoncé (`V2__add_age.sql`) ou assouplir le test.
  *Vérif :* un étudiant qui suit l'énoncé passe le test du premier coup.
- [ ] **T4-05** · **`TD4.adoc:1-15`, `:23-29`** (anciens exos 60 et 62) — « ajouter la dépendance
  flyway » et « ajouter les dépendances testcontainers », alors qu'elles sont **déjà** dans
  `exo60/build.gradle.kts:26-34` → les retirer du squelette (`T0-14`) ou retirer la consigne.
  **DÉCISION :** ___
  *Vérif :* cohérence entre l'énoncé et le build.
- [ ] **T4-06** · **`exo60/src/test/resources/application-jdbc.yml:7`** — `url: something`, un
  placeholder à remplacer ; `application-SC.yaml` contient `spring.datasource.nothing: here` ;
  `src/main/resources/application.yml` contient un `custom.client` hérité de `exo45` → nettoyer ;
  décrire dans l'énoncé ce que l'étudiant doit mettre dans `application-jdbc.yml`.
  *Vérif :* `grep -rn 'something\|nothing\|custom.client' td4/flyway/src` → vide.
- [ ] **T4-07** · **`TD4.adoc:23-29`** (ancien exo 62) — « Les tests exo 60 et exo 61 doivent passer…
  et lancer un conteneur Postgres », alors qu'ils tournent avec le profil `h2` ; la vérification
  Postgres est `Exo62Test` → corriger l'énoncé.
  *Vérif :* énoncé cohérent avec les profils des tests.
- [ ] **T4-08** · **`exo65/src/main/resources/schema.sql`** et **`exo71/src/main/resources/schema.sql`** —
  tables de sécurité orphelines (bonus JDBC du TD3) → supprimer.
  *Vérif :* fichiers absents de `td4/`.
- [ ] **T4-09** · **`exo65/build.gradle.kts:22-40`** — `data-jpa`, `validation`, `flyway-core`,
  `h2`, `spring-security-test` ne servent pas aux exos d'observabilité ; `kotlin-logging` est
  déjà présent alors que l'exo 5 revient à l'utiliser (ce qui est cohérent, à garder) → nettoyer
  (`T0-14`).
  *Vérif :* le module démarre sans datasource.

## 🟠 Migration 4.1

- [ ] **T4-10** · **`exo60/build.gradle.kts:26-34`** — `flyway-core` + `flyway-database-postgresql`
  → `spring-boot-starter-flyway` (sans lui, **aucune migration ne tourne** et `Exo60Test`
  échoue) ; Testcontainers 2 (`testcontainers-junit-jupiter`, `testcontainers-postgresql`,
  `org.testcontainers.postgresql.PostgreSQLContainer`) (`C4-06`, `C4-36`).
  *Vérif :* `Exo60Test` vert sur `correction`.
- [ ] **T4-11** · **`exo65/src/main/resources/application.yml:14-18`** — renommages éventuels des
  propriétés `management.*` et du registre Prometheus (`MIG-29`) → vérifier au spike `T0-01`.
  *Vérif :* `/actuator/prometheus` répond sur `correction`.
- [ ] **T4-12** · **`exo65/…/Exo66Test.kt:5`** — import MockMvc déplacé (`MIG-12`).
  *Vérif :* compile.

## 🔵 Autonomie / tests

- [ ] **T4-13** · **exos 5-7 (logs)** — aucun test. Proposition : `OutputCaptureExtension` de
  Spring Boot (`@ExtendWith(OutputCaptureExtension::class)`) pour vérifier qu'un appel à `/log`
  écrit une ligne INFO ou ERROR (exo 5) au format `date - level - thread - message` (exo 6),
  et que `org.springframework` est en DEBUG avec `@ActiveProfiles("dev")` (exo 7).
  *Vérif :* 3 tests rouges sur `main`, verts sur `correction`.
- [ ] **T4-14** · **exos 10-12 (métriques)** — aucun test → `@SpringBootTest` + `MeterRegistry`
  injecté : après N appels à `/log`, `registry.counter("logCall", "result", "success").count()`
  plus `…"error"` = N. Pour l'exo 10, un test MockMvc sur `/actuator/prometheus`
  (`@AutoConfigureObservability` si nécessaire en 4.1).
  *Vérif :* tests rouges sur `main`, verts sur `correction`.
- [ ] **T4-15** · **`LogController`** — `randomService.randomThrow()` échoue une fois sur deux, ce
  qui rend les tests non déterministes → mocker `RandomService` (`@MockkBean`) dans les tests
  fournis.
  *Vérif :* 10 exécutions consécutives → même résultat.

## 🔀 Cohérence avec les slides

- [ ] **T4-16** · **exos 1-2** — la slide nomme l'exemple de trois façons (`C4-08`) et présente
  les undo migrations sans dire qu'elles sont payantes (`C4-07`) → aligner le nom de la slide sur
  l'exo (`V1__init.sql`, table `pony`).
  *Vérif :* `C4-07`, `C4-08` cochés.
- [ ] **T4-17** · **`ddl-auto: create` + Flyway** — les modules JPA des TD2-TD3 utilisent
  `ddl-auto: create` ; c4 doit dire qu'avec Flyway on passe à `validate` (`C4-34`) → l'exo 2 le
  fait faire (`spring.jpa.hibernate.ddl-auto: validate` dans `application-h2.yml`).
  **DÉCISION :** ___
  *Vérif :* la correction de l'exo 2 démarre avec `validate`.
- [ ] **T4-18** · **exos 5-7** — logs structurés JSON, MDC, placeholders `{}` (`C4-31`, `C4-32`,
  `C4-33`) : ajouter les placeholders en consigne de l'exo 5 (kotlin-logging :
  `logger.info { "…" }`) ; logs JSON en bonus (`T4-28`).
  **DÉCISION :** ___
  *Vérif :* énoncé et slide utilisent la même syntaxe de log.
- [ ] **T4-19** · **exo 9** — la slide montre `@EndpointWebExtension`, dont l'exemple est cassé
  (`C4-15`) ; le test attend `$.app-name`. Proposition : exo avec la propriété
  `info.app-name=${spring.application.name}` + `management.info.env.enabled=true`, ou avec un
  `InfoContributor` (`C4-29`) ; corriger ou remplacer la slide en conséquence.
  **DÉCISION :** ___
  *Vérif :* `Exo66Test.exo67` vert ; la slide montre la méthode de la correction.
- [ ] **T4-20** · **exos 8-12** — `/actuator` exposé sans sécurité (`C4-28`) : le signaler dans
  l'énoncé (« en production, ne pas exposer »).
  *Vérif :* remarque présente.
- [ ] **T4-21** · **ancien exo 71 (réactif)** — « Migrer l'application en réactive » tient en une
  ligne d'énoncé, sur 2 slides (`C4-21`, `C4-22`, `C4-39`, `C4-52`), alors que le module est en
  JPA/H2, incompatible avec WebFlux sans R2DBC. Options :
  (a) supprimer ;
  (b) bonus réduit : un `PonyController` en mémoire (pas de base) avec des fonctions `suspend`
  et un `Flow`, sous WebFlux, testé par `WebTestClient` ;
  (c) remplacer par les **threads virtuels** (`spring.threads.virtual.enabled=true`, `MIG-17`,
  `C4-26`) : un test vérifie que `/log` est servi par un thread virtuel.
  **DÉCISION :** ___
  *Vérif :* le module `td4/reactive` (ou son remplaçant) compile, et son test est rouge sur `main` et vert sur `correction`.
- [ ] **T4-22** · **exos 3-4 (Testcontainers)** — la slide doit montrer les dépendances
  (`C4-36`), `@JvmStatic` (`C4-17`) et une image figée (`C4-20`), et le test doit être identique.
  *Vérif :* `C4-17`, `C4-20`, `C4-36` cochés.

## 🟡 Énoncé

- [ ] **T4-23** · **`TD4.adoc`** — pas d'en-tête `== TD4`, pas de module indiqué, doublons
  `Exo 65` (`:35`, `:39`) et `Exo 66` (`:47`, `:51`), exo 63 testé par `Exo64Test` → appliquer la
  table (titres, noms de tests, modules).
  *Vérif :* `grep -c '^== Exo' td4/README.adoc` = nombre de lignes de la table.
- [ ] **T4-24** · **exos 3-4** — encadré « Bonus : nécessite Docker ou Podman », avec la
  configuration Podman pour Testcontainers (`DOCKER_HOST`, `TESTCONTAINERS_RYUK_DISABLED`) et
  la commande `./gradlew :flyway:test -Pdocker` (`T0-16`).
  *Vérif :* procédure testée avec Podman sur un poste.
- [ ] **T4-25** · **exo 5** — donner la dépendance kotlin-logging (version de `T0-01`), ou dire
  qu'elle est déjà là (`C3-48` : la slide ne la montre jamais).
  *Vérif :* énoncé et build cohérents.
- [ ] **T4-26** · **exo 10** — « Le endpoint doit répondre une fois l'application lancée » → donner
  la commande curl et un extrait attendu (`# TYPE logCall_total counter`).
  *Vérif :* sortie de la correction conforme.

## ⭐ Bonus proposés

- [ ] **T4-27** · ⭐ **Testcontainers** (anciens exos 62-63, cf. table) — déjà prévus en bonus.
  *Vérif :* `./gradlew -p td4 :flyway:test -Pdocker` vert sur `correction` (CI).
- [ ] **T4-28** · ⭐ **Logs structurés JSON** (`C4-31`) —
  `logging.structured.format.console=ecs` et un test `OutputCapture` qui parse la ligne en JSON.
  **DÉCISION :** ___
  *Vérif :* test vert sur `correction`.
- [ ] **T4-29** · ⭐ **Cache** — `CacheController` et `RandomService.random(i)` sont déjà dans
  `exo65`, mais aucun exo ne s'en sert, alors que c4 consacre un chapitre au cache
  (`caching.md`). Proposition : `@EnableCaching` + `@Cacheable` sur `random(i)` ; test fourni :
  deux appels avec le même `i` → même valeur, et `RandomService` appelé une seule fois. Piège à
  signaler : l'auto-invocation et les proxies Kotlin (`C3-39`, `C4-10`, `C4-11`).
  **DÉCISION :** ___ (socle ou bonus)
  *Vérif :* test fourni rouge sur `main`, vert sur `correction`.
- [ ] **T4-30** · ⭐ **Health indicator custom** (`C4-29`) — `/actuator/health` affiche l'état de
  `RandomService`.
  **DÉCISION :** ___
  *Vérif :* test MockMvc sur `/actuator/health` vert.

## 📽 Slides à modifier (amphi 4)

- [ ] **T4-31** · À faire **avant** le TD4 : `C4-06`, `C4-07`, `C4-08` (Flyway), `C4-15`,
  `C4-29` (info), `C4-17`, `C4-20`, `C4-36` (Testcontainers), et `C4-21`, `C4-39` selon la
  décision `T4-21`.
  *Vérif :* IDs cochés dans `Cours 4.md`.

## ✅ Correction (à écrire intégralement : aucune branche n'existe)

- [ ] **T4-32** · Exos 1-4 : un commit par exo ; les exos 3-4 ne sont validés que par le job CI
  avec Docker.
  *Vérif :* CI `correction` verte, `-Pdocker` compris.
- [ ] **T4-33** · Exos 5-12 : un commit par exo ; les exos 5 et 6 correspondent aux deux anciens « Exo 65 », les exos 7 et 8
  aux deux anciens « Exo 66 ».
  *Vérif :* chaque commit fait passer au vert le test de son exo, et seulement celui-là.
- [ ] **T4-34** · Exo 13 / bonus : selon `T4-21`, `T4-28`, `T4-29`, `T4-30`.
  *Vérif :* `./gradlew -p td4 test` vert en fin de branche.
