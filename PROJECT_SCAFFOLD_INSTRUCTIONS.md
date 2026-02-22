# Nexus Android Project Scaffold Instructions (Clean Architecture + MVVM)

Use this file as the single source of instructions for any coding agent when starting a new Android project from scratch.

## How To Use

1. Create a new empty Android Studio project (Compose Activity).
2. Give this file to the agent.
3. Tell the agent to follow this file exactly and replace placeholders:
   - `<APP_NAME>`
   - `<PACKAGE_NAME>` (example: `com.yourname.appname`)
4. Ask the agent to generate a dummy but runnable base project only (no real feature logic).

## Mandatory Stack

- Kotlin
- Jetpack Compose (Material 3)
- Hilt (DI)
- Coroutines + Flow
- Retrofit + OkHttp + Gson
- Clean Architecture + MVVM

## Architecture Rules

- `presentation` depends on `domain`
- `data` implements `domain` interfaces
- `domain` contains business contracts/use cases and has no Android framework dependency
- UI state is owned by `ViewModel` and exposed with `StateFlow`
- DTO -> Domain mapping must be done via mapper classes

## Required Package Structure

Create this structure under `app/src/main/java/<PACKAGE_NAME>/`:

```text
app/
  src/main/java/<PACKAGE_NAME>/
    app/
      App.kt
    core/
      coreNetwork/
        Result.kt
      coreDatabase/
        AppDatabase.kt
    data/
      dto/
        UserDto.kt
      mappers/
        UserMapper.kt
      dataSource/
        remoteDataSource/
          UsersApiService.kt
        localDataSource/
          UserDao.kt
      repository/
        UserRepositoryImpl.kt
    domain/
      models/
        User.kt
      repository/
        UserRepository.kt
      usecases/
        GetUsersUseCase.kt
    di/
      ApiModule.kt
      NetworkModule.kt
      RepositoryModule.kt
    presentation/
      ui/
        MainScreen.kt
      component/
        ListingScreen.kt
        ListIngItemScreen.kt
        ProgressScreen.kt
      state/
        UsersState.kt
      viewmodel/
        MainScreenViewModel.kt
      theme/
        Color.kt
        Theme.kt
        Type.kt
    MainActivity.kt
```

## Minimum Behavior To Implement

- App launches and shows a Compose screen.
- `MainScreenViewModel` calls `GetUsersUseCase`.
- `GetUsersUseCase` calls `UserRepository` interface.
- `UserRepositoryImpl` returns dummy data (hardcoded or fake remote response).
- UI renders `Loading`, `Success`, and `Error` states from `UsersState`.
- Include at least one mapper (`UserMapper`) and one DTO/domain conversion.

## Dependency Setup Requirements

- Configure Hilt plugin and KSP in Gradle.
- Add Hilt Android + compiler.
- Add Retrofit, Gson converter, OkHttp logging interceptor.
- Keep `minSdk`, `targetSdk`, `compileSdk` aligned with project defaults unless asked otherwise.

## DI Wiring Requirements

- `App.kt` must use `@HiltAndroidApp`.
- `MainActivity` must use `@AndroidEntryPoint`.
- Provide Retrofit/OkHttp/API in modules.
- Bind `UserRepositoryImpl` to `UserRepository` via Hilt module.

## File Quality Rules

- Keep each class focused on one responsibility.
- Use clear package names and consistent naming.
- Avoid putting networking logic in `ViewModel`.
- Avoid putting Android-specific code in `domain`.

## Acceptance Checklist

Agent must complete all:

1. Project builds without errors.
2. App runs and dummy list screen opens.
3. Folder structure matches this document.
4. Clean Architecture + MVVM flow is visible in code.
5. README includes architecture summary and structure overview.

## Reusable Prompt You Can Give Any Agent

```text
Create a new Android app named <APP_NAME> with package <PACKAGE_NAME> using Kotlin + Compose + Hilt + Retrofit + Coroutines/Flow.
Follow Clean Architecture + MVVM exactly as defined in PROJECT_SCAFFOLD_INSTRUCTIONS.md.
Generate the full folder structure and minimal runnable dummy implementation for User listing with Loading/Success/Error states.
Wire DI modules, repository contract/implementation, use case, ViewModel, UI state, and Compose screen.
After generation, verify build and provide a summary of created files.
```
