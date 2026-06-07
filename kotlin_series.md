# Kotlin Series: Sealed Classes vs Sealed Interfaces

In Android development, specifically within ViewModels, we often need to represent UI states or results. Kotlin provides two powerful tools for this: `sealed class` and `sealed interface`.

## 1. Sealed Class Example
Sealed classes are best when variants are **mutually exclusive** and might need to share common state through a constructor.

```kotlin
sealed class UsersState {
    object Loading : UsersState()
    data class Success(val users: List<User>) : UsersState()
    data class Error(val message: String) : UsersState()
}
```

## 2. Sealed Interface Example
Sealed interfaces are the modern, more flexible alternative. Since Kotlin classes can implement multiple interfaces but only extend one class, interfaces offer better composition.

```kotlin
sealed interface UsersState {
    object Loading : UsersState
    data class Success(val users: List<User>) : UsersState
    data class Error(val message: String) : UsersState
}
```

---

## Comparison Table

| Feature | `sealed class` | `sealed interface` |
| :--- | :--- | :--- |
| **Constructor** | Can have constructor parameters (common data). | **Cannot** have a constructor. |
| **Inheritance** | Subclasses can only inherit from this one class. | Subclasses can implement multiple other interfaces. |
| **Logic** | Can contain shared methods and properties. | Can only contain abstract properties or default methods. |
| **Memory** | Slightly more overhead (is a class). | Most lightweight. |

---

## What to use when?

### Use **Sealed Class** when:
* **Shared State:** You want every variant to hold a common value.
  * *Example:* `sealed class State(val timestamp: Long)` ensures every state has a time.
* **Legacy Support:** Working with older codebases where class inheritance is expected.

### Use **Sealed Interface** when (Recommended by default):
* **No Shared State:** Most UI states don't actually need to share data in the base constructor.
* **Multiple Roles:** A state needs to be part of two different groups (e.g., a state that is both a `UIState` and a `LoggableEvent`).
* **Clean Architecture:** It's the modern standard for Compose UI events and states.

## Summary for ViewModels
1. **Result/State:** Use `sealed interface` for exhaustive `when` expressions (Loading, Success, Error).
2. **Form/UI State:** Use a `data class` if you need to maintain a persistent state (like text field inputs) while other flags change.
