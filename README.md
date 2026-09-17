# HomeFit

A native Android home-workout app built around Jetpack Compose + Material 3, with an offline-first core, deterministic workout generation, an exercise library, a guided workout player, local progress tracking, and GitHub Actions CI/CD.

## Current stack

- Kotlin 2.4.20
- Android Gradle Plugin 9.4.0
- Gradle 9.6.0
- Jetpack Compose 1.12.1
- Material 3 1.4.0
- AndroidX Activity 1.13.0
- AndroidX Lifecycle 2.11.0
- Navigation Compose 2.10.1
- Preferences DataStore 1.2.1
- compileSdk / targetSdk 36
- minSdk 24

These versions were selected against the current Android/Kotlin release documentation available in September 2026. Material 3 is on the stable 1.4.0 line; Kotlin 2.4.20 is the current stable Kotlin release line; AGP 9.4.0 requires Gradle 9.6.0. See the official Android and Kotlin release notes before upgrading. 

## Architecture

The first implementation is intentionally lightweight for a developer with a weak local machine:

`Compose UI → ViewModel → Repository → DataStore`

Workout generation is pure Kotlin domain logic, which keeps it easy to unit test and independent of Android UI.

Core areas:

- `domain/` — exercises, workout models, deterministic workout engine
- `data/` — local progress persistence
- `ui/` — screens, navigation, ViewModel
- `ui/theme/` — Material 3 theme and design tokens

## Product scope in this build

Implemented:

- Material 3 light/dark/dynamic color theme
- adaptive NavigationBar / NavigationRail behavior
- Home dashboard
- recommended workout
- quick-start workouts
- workout library
- searchable exercise library with category filters
- guided workout player with timer, pause/resume and next controls
- completion flow
- local workout statistics using DataStore
- progress screen
- settings screen
- accessibility-conscious large controls and semantic Compose UI
- deterministic workout composition rules
- unit tests
- Compose instrumentation test
- GitHub Actions debug CI
- GitHub Actions instrumentation workflow
- GitHub Actions release workflow

## GitHub Actions — important for a weak PC

The canonical build environment is GitHub Actions.

The CI workflows use a GitHub-hosted Ubuntu runner and install the exact Gradle version expected by AGP. This means the local machine does not need to perform release builds or emulator tests.

### Pull requests / main branch

`.github/workflows/ci.yml` runs:

- JDK 21 setup
- Android SDK setup
- Gradle 9.6.0 setup
- unit tests
- Android Lint
- debug APK build
- report artifact upload
- debug APK artifact upload

### Instrumentation

`.github/workflows/instrumentation.yml` runs Compose UI tests on a headless Android emulator. It is also manually dispatchable.

### Release

`.github/workflows/release.yml` creates release APK + AAB artifacts.

For a signed release, configure these GitHub Actions secrets:

- `HOMEFIT_RELEASE_KEYSTORE_B64`
- `HOMEFIT_RELEASE_STORE_PASSWORD`
- `HOMEFIT_RELEASE_KEY_ALIAS`
- `HOMEFIT_RELEASE_KEY_PASSWORD`

The keystore is decoded into the ephemeral runner only; it is never committed to Git.

If signing secrets are not configured yet, the release workflow can still be used to validate the release build path, but the resulting outputs should not be treated as distributable signed releases.

## Local development

The repository is designed so the local machine can stay lightweight.

For normal editing, work in Android Studio or another Kotlin-aware IDE.

For CI-like builds on a machine with Gradle installed:

```bash
gradle testDebugUnitTest
gradle lintDebug
gradle assembleDebug
```

The current execution environment used to prepare this project does not have system Gradle or direct internet access, so I could not run the Android build locally here. The source tree and CI workflows are included, but the final compile should be validated by GitHub Actions after the repository is pushed.

## One-time Gradle Wrapper setup

The project includes `gradle/wrapper/gradle-wrapper.properties` pinned to Gradle 9.6.0. The binary wrapper JAR could not be downloaded in the isolated build environment used to create this package, so it is intentionally not fabricated or substituted.

On a normal machine with network access, generate the standard wrapper with:

```bash
gradle wrapper --gradle-version 9.6.0
```

Then commit the generated `gradlew`, `gradlew.bat`, and `gradle/wrapper/gradle-wrapper.jar`.

The GitHub Actions workflows intentionally install Gradle 9.6.0 directly, so CI does not depend on the missing wrapper JAR in this package.

## Safety and product principles

HomeFit is general fitness software, not a medical diagnostic tool. Exercise copy avoids body-shaming and appearance comparison. High-impact movements have easier alternatives, and equipment-based movements assume stable equipment.

## Next production steps

A full production release would still benefit from:

- richer licensed exercise media
- real program scheduling/recovery logic
- persistent preferences in DataStore
- a more complete workout history model
- signed Play App Bundle configuration
- broader API/device test coverage
- localization (including RTL)
- privacy policy and store metadata
- final UX review on physical devices
