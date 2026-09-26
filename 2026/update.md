---
tags: [spring, kotlin, iut, td, mise-a-jour]
date: 2026-09-26
cible: spring-boot-4.1
---

# Mise à jour 2026 des TD — Programmation avancée : Spring / Kotlin

Plan de mise à jour des 4 TD (après-midi 14h → 18h, IUT 3ᵉ année) pour l'année 2026-27.
Cible technique : **Spring Boot 4.1**, **Java 25**, **Kotlin 2.4**.

> [!info] Périmètre
> Ce fichier et les 4 fiches TD sont **descriptifs** : aucune ligne de code, d'énoncé ou de
> slide n'a été modifiée pour les écrire. Chaque étape est une case à cocher avec un ID stable,
> à traiter une par une puis à cocher (`- [x]`), ou à écarter (`**REPORTÉ :**`).
> Les chemins cités sont ceux **d'avant la restructuration** (`exo21/…`, `TD2.adoc:69`), sauf
> mention contraire.
>
> Les renvois vers la relecture des amphis (`C1-07`, `C2-32`, `MIG-04`, `AV-2`…) pointent vers
> `../iut-spring-kotlin/2026/*.md`, désormais numérotés.

## Navigation

| Note | Contenu | Amphi |
|---|---|---|
| [[TD1]] | DI « à la main », Spring Boot, MVC, tests (anciens exos 0-20) | c1 |
| [[TD2]] | Validation, erreurs, configuration, JPA (anciens exos 21-40 + bonus Postgres) | c2 |
| [[TD3]] | Sécurité, clients HTTP (anciens exos 41-53 + bonus JDBC) | c3 |
| [[TD4]] | Flyway, Testcontainers, logs, actuators, métriques, réactif (anciens exos 60-71) | c4 |

Correspondance modules actuels → cible (noms de modules à confirmer en `T0-08`) :

| Actuel | Cible | Contenu |
|---|---|---|
| `exo1` | `td1/di` | DI sans Spring Boot, puis Spring Boot (anciens exos 0-9) |
| `exo10` | `td1/web` | CRUD Movie, MockMvc (anciens exos 10-20) |
| `exo21` | `td2/validation` | Pets, validation, erreurs, config (anciens exos 21-32) |
| `exo33` | `td2/jpa` | Humans/Pets JPA (anciens exos 33-40) |
| `exo33` (état final de TD2) | `td3/security` | Sécurité (anciens exos 41-44) |
| `exo45` | `td3/client` | RestClient / WebClient (anciens exos 45-53) |
| `exo60` | `td4/flyway` | Flyway, Testcontainers (anciens exos 60-63) |
| `exo65` | `td4/observability` | Logs, actuators, métriques (anciens exos 65-70) |
| `exo71` | `td4/reactive` | Réactif (ancien exo 71) |

## Constat de départ

- **Cause des conflits de 2025** : ce n'est pas le monorepo en soi, ce sont des commits de TD
  qui modifiaient des fichiers **déjà modifiés par les étudiants** :
  `f5be5ba` (TD2) touchait `TD1.adoc`, `exo1/build.gradle.kts` et `exo10/build.gradle.kts`
  (que les exos 8-9 font modifier) ; `167eb98` (TD4) touchait `settings.gradle.kts` et
  `exo33/…/PetControllerTest.kt`.
- **Retard technique** : Boot 3.5.7 / 3.3.5 selon les modules, Kotlin 1.9.25, toolchain 21,
  Gradle 8.10.2. Boot 4 exige Kotlin 2.2+ (`MIG-13`), Java 25 exige Gradle ≥ 9.1.
- **Modules qui ne compilent pas en l'état** : `exo71` (test dans le package `exo33`) et `exo65`
  (`PrometheusTestConfig` dépend d'une dépendance ajoutée à l'exo 68).
- **Numérotation** : exos 49, 54-59, 64 absents ; exos 65 et 66 en double ; `Exo64Test` teste
  l'exo 63 ; module `exo21` dont l'app s'appelle `exo20`.
- **Autonomie** : ~25 exos sans aucun test automatisé (0, 6, 10-18, 27, 31-34, 43, 65-70).
- **Correction** : `td1-correction`, `td2-correction`, `td3-correction` (un commit par exo,
  les 19 premiers commits de `td3-correction` sont identiques à `td2-correction`) ; pas d'Exo 32,
  Exo 48 fusionné dans « Exo50-53 », **pas de correction TD4**.

## Principes

1. **Un TD = un dossier autonome** `tdN/` (wrapper Gradle, `settings.gradle.kts`, `README.adoc`
   = énoncé, modules). Aucun fichier partagé entre deux TD. Le `settings.gradle.kts` racine
   (`includeBuild`) sert uniquement au professeur et n'est pas publié.
2. **Éclatement avant la rentrée** : `git subtree split --prefix=tdN` sur `main` et sur
   `correction` → 4 dépôts publics. Ce dépôt reste le dépôt de préparation, figé après
   l'éclatement.
3. **Correction** : une branche `correction` par dépôt, **un commit par exo**
   (`tdN: Exo X — titre`), publiée **avant** le TD. Dans ce dépôt, un commit de correction ne
   touche **jamais** deux dossiers `tdN/` (sinon le split mélange les TD).
4. **Errata en cours d'année** : commit sur `main` du dépôt du TD, puis
   `git rebase main correction` + `push --force-with-lease` (les étudiants ne suivent pas
   `correction`). Report dans ce dépôt en fin d'année (`T0-27`).
5. **TDD, mécanique actuelle** : un test fourni par exo quand c'est possible, sinon une
   requête Bruno avec assertions, sinon une vérification manuelle explicite (commande +
   résultat attendu dans l'énoncé). Les étudiants **écrivent** aussi des tests (exos dédiés).
   Un module doit **toujours compiler** sur `main` (tests rouges acceptés, erreurs de
   compilation non).
6. **Numérotation par TD** : Exo 1..N dans chaque dépôt. Un exo = une classe de test
   `ExoNTest` (ou une `@Nested inner class ExoN`), lançable seule :
   `./gradlew :web:test --tests 'Exo13Test'`.
7. **CLI obligatoire** : IntelliJ **ou VS Code** → chaque consigne de l'énoncé donne la commande
   Gradle équivalente.
8. **Docker/Podman optionnel** : tout test qui démarre un conteneur est annoté
   `@Tag("docker")`, exclu par défaut, et l'exo correspondant est un **bonus**.
9. **Référence API = les slides 4.1**. Chaque divergence TD ↔ slide est listée dans la fiche
   du TD (section 🔀) avec une proposition et une ligne `**DÉCISION :** ___`, tranchée au cas
   par cas.
10. **Le TD N ne s'appuie que sur les amphis 1 à N** (alternance amphi 1 → TD1 → amphi 2 → TD2…).

## Légende

🔴 faux / cassé · 🟠 migration 4.1 · 🟡 énoncé (clarté, coquille, incohérence) ·
🔵 autonomie (test manquant) · 🔀 cohérence avec les slides · ⭐ bonus · ✅ correction ·
📽 slide à modifier · ⚪ cosmétique

Annotations, comme dans la relecture des amphis : `**DÉCISION :** …`, `**FAIT :** …`,
`**REPORTÉ :** …`.

Format d'une étape : `- [ ] **T1-07** · **\`fichier:ligne\`** — constat → correctif. *Vérif :* …`

## Versions retenues

À remplir par `T0-01` à `T0-03`, puis recopier telles quelles dans tous les `build.gradle.kts`.

| Composant | Version |
|---|---|
| Gradle (wrapper) | ______ (≥ 9.1) |
| Spring Boot / dependency-management | 4.1.___ / ______ |
| Kotlin (`jvm`, `plugin.spring`, `plugin.jpa`) | 2.4.___ |
| Java toolchain | 25 |
| mockk / springmockk | ______ / 5.___ |
| assertk | ______ |
| Serveur HTTP de test (TD3) | ______ |
| kotlin-logging | ______ |

---

## Étapes transverses

### A. Spikes — à faire avant tout le reste

- [ ] **T0-01** · 🟠 **Socle de versions** — créer hors dépôt un projet vide Boot 4.1.x +
  Kotlin 2.4.x + toolchain 25 + wrapper Gradle 9.x (`webmvc`, `data-jpa`, `validation`, `h2`,
  `actuator`, `security`, `flyway`) avec un `@SpringBootTest` et un `@WebMvcTest`.
  Vérifier la compatibilité Kotlin 2.4 / Boot 4.1 (Boot gère sa propre version de Kotlin : la
  surcharge `kotlin.version` est-elle nécessaire ?).
  *Vérif :* `./gradlew build` vert, tableau « Versions retenues » rempli.
- [ ] **T0-02** · 🟠 **springmockk 5.x** (`AV-4`, `MIG-09`, `MIG-27`) — dans le projet de
  `T0-01`, un `@WebMvcTest` avec `@MockkBean` et un `@SpringBootTest` avec `@MockkSpyBean`.
  Relever le package exact des annotations. Plan B si KO : `@MockitoBean` + `mockito-kotlin`
  (et adapter C1-16).
  *Vérif :* les deux tests verts ; conclusion reportée dans `AV-4`.
- [ ] **T0-03** · 🟠 **Serveur HTTP de test pour TD3** — MockServer 5.15 (Netty 4.1, Jackson 2)
  est à remplacer. Comparer OkHttp `mockwebserver3`, WireMock (`wiremock-spring-boot`) et
  `MockRestServiceServer` (RestClient seulement). Critères : fonctionne pour RestClient **et**
  WebClient, aucun conflit de dépendances avec Boot 4.1, lisible en Kotlin, cohérent avec la
  slide à écrire (`C3-46`).
  *Vérif :* le test `exo45()` actuel porté sur l'outil choisi est vert ; choix noté dans le tableau.
- [ ] **T0-04** · 🔵 **Bruno en CLI** — vérifier que `bru run` (npm `@usebruno/cli`) s'installe
  sur les postes IUT (proxy npm) et exécute une collection avec assertions (`res.status`,
  `res.body`). Plan B : fichier `.http` (IntelliJ + extension REST Client pour VS Code) avec
  vérification visuelle.
  *Vérif :* `bru run --env local` sur une collection de 2 requêtes → sortie « 2 passed ».
- [ ] **T0-05** · 🔵 **VS Code** — ouvrir un module Boot 4.1 / Kotlin 2.4 dans VS Code (extension
  Kotlin officielle + Gradle), lancer l'appli et un test depuis l'IDE et en CLI.
  *Vérif :* procédure de 5 lignes maximum rédigée pour les `README.adoc`.
- [ ] **T0-06** · 🔵 **Postes IUT** — JDK 25 détecté par Gradle sans téléchargement
  (`./gradlew -q javaToolchains`) ; proxy : valider que la configuration fonctionne dans
  **`~/.gradle/gradle.properties`** (et non plus dans un `gradle.properties` à la racine du
  projet : un seul réglage pour les 4 dépôts, et aucun fichier du dépôt modifié par les étudiants).
  *Vérif :* sur un poste IUT, `./gradlew build` du projet `T0-01` passe derrière le proxy.

### B. Restructuration du dépôt

- [ ] **T0-07** · ⚪ **Nettoyage** — supprimer `.mvn/`, `build/`, `.kotlin/` et `.gradle/`
  à la racine (non suivis, mais trompeurs) ; compléter `.gitignore` (`data/`, `*.mv.db`,
  `.vscode/` sauf `extensions.json`, `bruno/**/.env`).
  *Vérif :* `git status --ignored` ne montre plus rien d'autre que les dossiers ignorés prévus.
- [ ] **T0-08** · 🟡 **Noms de modules** — valider les noms thématiques (`di`, `web`,
  `validation`, `jpa`, `security`, `client`, `flyway`, `observability`, `reactive`) et le
  package racine (`iut.nantes.<module>`) ; les packages actuels `iut.nantes`,
  `iut.nantes.exo21`, … sont renommés en conséquence.
  **DÉCISION :** ___
  *Vérif :* tableau de correspondance ci-dessus mis à jour.
- [ ] **T0-09** · 🔴 **Déplacement** — `git mv` de chaque module vers `tdN/<module>` et de
  `TDn.adoc` vers `tdN/README.adoc` (conserve l'historique). Le cas de `td3/security`, qui
  part de l'état final de `td2/jpa`, est traité dans [[TD3]] (`T3-01`).
  *Vérif :* `git log --follow td2/README.adoc` remonte jusqu'à `TD2.adoc`.
- [ ] **T0-10** · 🔴 **Build autonome par TD** — dans chaque `tdN/` : `settings.gradle.kts`
  (`rootProject.name = "tdN"`, `include(...)` des modules du TD), wrapper Gradle de `T0-01`
  (`gradle wrapper --gradle-version …`), `gradlew` exécutable (`git update-index --chmod=+x`).
  À la racine : `settings.gradle.kts` avec `includeBuild("td1")`… uniquement.
  *Vérif :* `./gradlew -p tdN projects` liste les bons modules pour N = 1..4 ;
  `cd tdN && ./gradlew help` fonctionne sans le dossier parent.
- [ ] **T0-11** · 🟡 **`README.adoc` par TD** — en-tête `= TDn — <thème>`, lien de clone du
  dépôt éclaté, proxy (`~/.gradle/gradle.properties`, `T0-06`), import IntelliJ / VS Code
  (`T0-05`), « comment vérifier un exo » (commande Gradle, Bruno), rappel « ne modifiez pas
  les tests fournis sauf consigne » ; `README.adoc` racine → liens vers les 4 TD.
  *Vérif :* rendu GitHub de chaque `README.adoc` lisible, tous les liens valides.

### C. Montée de versions (tous les modules)

- [ ] **T0-12** · 🟠 **Bloc commun des `build.gradle.kts`** (`MIG-22`, `MIG-23`, `MIG-30`) —
  versions de `T0-01` ; toolchain 25 ; `kotlin { compilerOptions { freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property") } }` ;
  `plugin.jpa` + bloc `allOpen` (`MIG-32`) uniquement dans les modules JPA ; mockk unifié
  (aujourd'hui 1.13.12 dans `exo45`/`exo60` et 1.14.6 ailleurs).
  *Vérif :* `grep -h 'version "' td*/*/build.gradle.kts | sort | uniq -c` → une seule version
  par plugin.
- [ ] **T0-13** · 🟠 **Renommages Boot 4** (`MIG-01`, `MIG-04`, `MIG-12`, `MIG-24`, `MIG-25`) —
  `spring-boot-starter-web` → `spring-boot-starter-webmvc` ;
  `com.fasterxml.jackson.module:jackson-module-kotlin` → `tools.jackson.module:jackson-module-kotlin` ;
  imports de test MockMvc → `org.springframework.boot.webmvc.test.autoconfigure` +
  `spring-boot-starter-webmvc-test` ; `flyway-core` → `spring-boot-starter-flyway` ;
  console H2 → module dédié ; Testcontainers 2 (artefacts `testcontainers-*`).
  Fichiers concernés : voir 🟠 dans chaque fiche.
  *Vérif :* `grep -rn 'starter-web"\|com.fasterxml\|autoconfigure.web.servlet' td*/` → vide.
- [ ] **T0-14** · 🔴 **Dépendances au plus juste** — chaque module déclare exactement ce dont le
  **squelette** a besoin, et **rien de ce que l'étudiant doit ajouter lui-même** (sinon l'exo
  « ajouter la dépendance X » est déjà fait). Détail par module dans les fiches.
  *Vérif :* pour chaque exo « ajouter la dépendance X », `grep X tdN/<module>/build.gradle.kts`
  → vide sur `main`.
- [ ] **T0-15** · 🔴 **Tout compile sur `main`** — `./gradlew -p tdN compileTestKotlin` pour
  N = 1..4, après les correctifs 🔴 des fiches (`exo65`, `exo71` notamment).
  *Vérif :* commande verte pour les 4 TD.

### D. Tests, outillage et CI

- [ ] **T0-16** · 🔵 **Convention de test** — un exo = `ExoNTest` (ou `@Nested inner class ExoN`
  dans une classe par module) + `@DisplayName("Exo N — titre")` ; tests Docker annotés
  `@Tag("docker")` et exclus par défaut (`tasks.test { useJUnitPlatform { excludeTags("docker") } }`,
  réactivables par `-Pdocker`).
  *Vérif :* `./gradlew :web:test --tests 'Exo13Test'` n'exécute que l'exo 13.
- [ ] **T0-17** · 🔵 **Collections Bruno** (si `T0-04` OK) — une collection par module qui
  expose une API (`tdN/<module>/bruno/`), une requête par exo non couvert par un test
  JUnit, avec assertions ; environnement `local` (port 8080).
  *Vérif :* `bru run --env local` vert sur la branche `correction`.
- [ ] **T0-18** · 🔴 **CI GitHub Actions (dépôt de préparation)** —
  job `main` : matrice N = 1..4, `./gradlew -p tdN compileTestKotlin` ;
  job `correction` : pour chaque commit de `main..correction`, vérifier qu'un seul `tdN/`
  est touché (`git diff --name-only $c^ $c | cut -d/ -f1 | sort -u | wc -l` = 1) et que
  `./gradlew -p tdN test -Pdocker` est vert.
  *Vérif :* pipeline vert sur `main` et `correction` ; un commit de test touchant deux TD fait
  échouer le job.

### E. Branche de correction

- [ ] **T0-19** · ✅ **Création** — branche `correction` partant de `main` **après** B, C et D.
  Convention de message : `tdN: Exo X — titre` (+ `Ancien: ExoYY` dans le corps).
  *Vérif :* `git log --format=%s main..correction | grep -vcE '^td[1-4]: Exo [0-9]+'` = 0.
- [ ] **T0-20** · ✅ **Reprise des corrections 2025** — pour chaque exo, repartir du commit
  d'origine listé dans la section ✅ de la fiche (`git cherry-pick -n <hash>`), adapter les
  chemins et la numérotation, appliquer les correctifs de la fiche, committer. TD4 : à écrire.
  *Vérif :* chaque exo de chaque fiche a exactement un commit.
- [ ] **T0-21** · ✅ **Rebase continu** — tant qu'on modifie `main` (énoncés, tests fournis),
  `git rebase main correction` et relancer la CI.
  *Vérif :* `git merge-base --is-ancestor main correction` → vrai.

### F. Éclatement et publication (avant la rentrée)

- [ ] **T0-22** · 🔴 **Gel** — toutes les étapes `T1` à `T4` cochées ou `REPORTÉ` ; CI verte.
  *Vérif :* `grep -c '^- \[ \]' 2026/TD*.md` = 0 (hors `REPORTÉ`).
- [ ] **T0-23** · 🔴 **Création des 4 dépôts** — noms à valider
  (`Zomzog/2026-iut-td1` … `td4` ?), publics, vides.
  **DÉCISION :** ___
  *Vérif :* `gh repo view Zomzog/<nom>` pour les 4.
- [ ] **T0-24** · 🔴 **Split** — pour N = 1..4 :
  `git subtree split --prefix=tdN main -b split/tdN-main` puis
  `git subtree split --prefix=tdN correction -b split/tdN-correction` ;
  `git merge-base --is-ancestor split/tdN-main split/tdN-correction` doit être vrai ;
  push vers `main` et `correction` du dépôt N.
  *Vérif :* ancêtre commun vrai pour les 4.
- [ ] **T0-25** · 🔴 **Recette depuis un clone propre** — pour chaque dépôt : `git clone`,
  `./gradlew compileTestKotlin` sur `main`, `git switch correction && ./gradlew test` (sans
  Docker), puis import IntelliJ **et** VS Code.
  *Vérif :* 4 × vert ; si possible, sur un poste IUT.
- [ ] **T0-26** · ⚪ **Gel du dépôt de préparation** — `README.adoc` racine qui pointe vers les
  4 dépôts ; anciennes branches `td*-correction` supprimées du remote (l'historique reste
  dans `correction`).
  **DÉCISION :** ___ (supprimer ou garder les branches 2025)
  *Vérif :* `git ls-remote --heads origin` ne liste que les branches voulues.

### G. En cours et en fin d'année

- [ ] **T0-27** · 🟡 **Report des errata** — à la fin de l'année, pour N = 1..4 :
  `git subtree pull --prefix=tdN <dépôt N> main` (puis `correction`), pour que ce dépôt
  serve de base à l'édition 2027.
  *Vérif :* `git diff <dépôt N>/main HEAD:tdN` → vide.
- [ ] **T0-28** · 🟡 **Retour d'expérience** — après chaque TD, noter en tête de la fiche
  (`> [!note] Retour séance`) les exos bloquants, l'avancement médian et les coquilles
  remontées, pour calibrer socle et bonus l'an prochain.
  *Vérif :* 4 encadrés remplis en fin d'année.

## Cohérence entre les TD

- [ ] **T0-29** · 🔀 **Fil rouge du domaine** — Movie (TD1), Pet/Human (TD2-TD3),
  Hello (client TD3), Pony (Flyway TD4), `/log` aléatoire (observabilité TD4). Proposition :
  garder, mais faire partir `td3/security` et `td4/reactive` du **même** état final de
  `td2/jpa` (issu de la correction) pour que le code soit reconnu d'un TD à l'autre.
  **DÉCISION :** ___
  *Vérif :* `diff -r` entre le squelette `td3/security/src/main` et la correction TD2 à
  l'exo final ne montre que les suppressions voulues.
- [ ] **T0-30** · 🔀 **Style de code commun** — injection par constructeur partout (`C1-29`),
  `@RequestMapping` au niveau classe (`C1-22`), `ResponseEntity` homogène, pas de `data class`
  pour les entités JPA (`C2-35`), assertk dans tous les tests, `@Test` sur chaque test
  MockMvc (`C1-10`).
  *Vérif :* revue de la branche `correction` avec cette liste.
- [ ] **T0-31** · 🔀 **Point d'entrée de chaque TD** — un étudiant qui n'a pas fini le TD N
  doit pouvoir faire le TD N+1 : chaque module de départ est complet et indépendant du travail
  précédent (c'est déjà le cas avec des modules séparés, à garder).
  *Vérif :* le TD N+1 compile et ses tests de départ se comportent comme prévu sans avoir touché
  au TD N.
