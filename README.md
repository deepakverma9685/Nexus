# Nexus

`Nexus` is an Android app built with **Kotlin** and **Jetpack Compose**, using **Hilt** for dependency injection and a **Clean Architecture + MVVM** approach for scalable, testable, and maintainable development.

## Tech Stack

- Kotlin
- Jetpack Compose (UI)
- Hilt (Dependency Injection)
- Retrofit + OkHttp + Gson (Networking)
- Coroutines + Flow
- Material 3

## Project Structure

The codebase is organized into clear layers with separation of concerns:

- `presentation/`
  - Compose screens, UI components, UI state, and `ViewModel`s
- `domain/`
  - Business models, repository contracts, and use cases
- `data/`
  - DTOs, mappers, repository implementations, and data sources
- `di/`
  - Hilt modules for dependency wiring
- `core/`
  - Shared/common utilities (e.g., result wrappers, base abstractions)

## Architecture: Clean Architecture + MVVM

This project combines **Clean Architecture** and **MVVM**:

- **Clean Architecture** defines strict layer boundaries:
  - `presentation` depends on `domain`
  - `data` implements `domain` contracts
  - `domain` stays framework-agnostic and contains business rules
- **MVVM** structures the UI layer:
  - `View` (Compose UI) observes state
  - `ViewModel` handles UI logic and state transformation
  - State is exposed using `StateFlow`/`Flow`

### Request/Data Flow

1. UI event is triggered from a Compose screen
2. `ViewModel` handles the event and calls a `UseCase`
3. `UseCase` talks to a repository interface from `domain`
4. `data` layer repository implementation fetches from remote/local sources
5. DTOs are mapped to domain models
6. `ViewModel` exposes `Loading`/`Success`/`Error` UI state back to Compose

This keeps UI logic decoupled from data sources and infrastructure details.

## Why This Architecture

- Clear separation between UI, business logic, and data access
- High testability (ViewModels, UseCases, and mappers can be tested independently)
- Better maintainability as features grow
- Easier refactoring and replacement of implementations (API, DB, cache)
- Predictable state-driven UI with Compose

## Repository Pattern

The Repository Pattern is used to abstract data access:

- Domain depends on repository interfaces only
- Data layer provides concrete repository implementations
- Remote/local logic is encapsulated in dedicated data sources
- Mapping logic is isolated in mapper classes

## Scalability

This structure scales well as the project grows:

- New features can be added by extending each layer consistently
- Networking, storage, and UI can evolve independently
- Hilt reduces manual dependency wiring
- Feature modules can be introduced later with minimal refactor

## Maintainability

Maintainability is improved by:

- Single responsibility per class/layer
- Clear boundaries between presentation, domain, and data
- Centralized dependency management via Hilt modules
- Predictable UI state handling in ViewModel

## Testability

The architecture is test-friendly:

- `UseCase` and `ViewModel` can be unit tested in isolation
- Repository interfaces allow mocking/faking
- Mapping and business logic can be tested without Android runtime
- Compose UI tests can validate rendering and interactions

## Getting Started

### Prerequisites

- Android Studio (latest stable)
- JDK 11+
- Android SDK (compile/target SDK configured in project)

### Run

1. Open the project in Android Studio
2. Sync Gradle
3. Run the `app` module on emulator or physical device

## Notes

- Internet permission is enabled for remote API calls.
- The current setup is a strong baseline for production-grade growth with Clean Architecture + MVVM.
