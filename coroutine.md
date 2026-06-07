# Kotlin Coroutines: The Ultimate Deep Dive (0 to 1)

Coroutines are "lightweight threads" that allow you to write asynchronous, non-blocking code in a sequential manner. They are built on the concept of **Suspending Functions**.

---

## 1. Core Concepts & Keywords

### `suspend`
The backbone of coroutines. A function marked with `suspend` can pause its execution without blocking the underlying thread.
*   **How it works:** It saves the current state (stack frame) and releases the thread for other tasks. When the result is ready, it resumes.
*   **Rule:** Can only be called from another `suspend` function or a Coroutine Scope.

### `CoroutineContext`
A set of elements that define the behavior of a coroutine. It includes:
*   **Job:** Controls the lifecycle.
*   **Dispatcher:** Determines the thread.
*   **Name:** For debugging.
*   **ExceptionHandler:** For catching errors.

### `CoroutineScope`
Defines the lifetime of the coroutine. When a scope is cancelled, all coroutines started in it are cancelled.
*   `viewModelScope`: Tied to ViewModel lifecycle.
*   `lifecycleScope`: Tied to Activity/Fragment lifecycle.
*   `GlobalScope`: (Avoid!) Tied to the whole app process.

---

## 2. Dispatchers (The Thread Controllers)

| Dispatcher | Thread Pool | Use Case |
| :--- | :--- | :--- |
| **`Dispatchers.Main`** | Main Thread | UI updates, small fast tasks. |
| **`Dispatchers.IO`** | Shared pool (64+ threads) | Disk/Network I/O. Optimized for waiting. |
| **`Dispatchers.Default`** | Shared pool (CPU cores) | Sorting, JSON parsing, heavy math. |
| **`Dispatchers.Unconfined`** | Current Thread | Starts on the caller thread, but resumes on whatever thread the suspend function used. **Use with caution!** |

---

## 3. Job vs. SupervisorJob

### `Job` (Standard)
If a child coroutine fails, the **parent and all other siblings are cancelled**. This is called "Structured Concurrency".

### `SupervisorJob`
If a child fails, **only that child is affected**. The parent and other siblings keep running.
*   **Usage:** Use this in a `ViewModel` or `supervisorScope` when you want independent tasks (e.g., uploading 5 images; if one fails, don't stop the others).

```kotlin
// supervisorScope example
supervisorScope {
    val task1 = launch { throw Exception("Failed!") }
    val task2 = launch { println("I still run!") } // This will execute
}
```

---

## 4. Sequential vs. Parallel Execution

### Sequential (Series)
Tasks run one after the other. Total time = **Sum of all tasks**.
This is the default behavior when calling `suspend` functions directly.
```kotlin
viewModelScope.launch {
    // 1. Fetch user (takes 2 seconds)
    // Execution STOPS here until fetchUser() returns
    val user = api.fetchUser() 
    
    // 2. Fetch posts (takes 1 second)
    // This ONLY starts after the line above is finished
    val posts = api.fetchPosts(user.id) 
    
    // Total time: 3 seconds
    println("User and posts loaded")
}
```

### Parallel
Tasks run at the same time. Total time = **Time of the longest task**.
Use `async` to start tasks concurrently.
```kotlin
viewModelScope.launch {
    // 1. Start fetching user (does NOT wait here)
    val deferredUser = async { api.fetchUser() } 
    
    // 2. Start fetching posts (runs at the same time as user fetch)
    val deferredPosts = async { api.fetchPosts() } 

    // 3. Wait for both to finish and get results
    // .await() suspends until the specific async block is done
    val user = deferredUser.await() 
    val posts = deferredPosts.await() 
    
    // Total time: 2 seconds (longest task)
    println("Both loaded in parallel")
}
```

---

## 5. Cancellation & Cooperative Coroutines
Coroutines are **cooperative**. If you run a heavy loop, it won't stop just because you called `job.cancel()`. You must check for cancellation.

### `isActive` / `ensureActive()`
Check if the coroutine is still alive. `ensureActive()` throws `CancellationException` immediately if cancelled.

### `yield()`
Yields the thread to other coroutines. It also checks for cancellation. Use it in heavy CPU loops.

```kotlin
launch {
    for (i in 1..1000) {
        yield() // Checks for cancellation and lets others run
        doHeavyCalculation()
        ensureActive() // Another way to check
    }
}
```

### `NonCancellable`
Used with `withContext` to perform cleanup that **must** happen even if the coroutine was cancelled.
```kotlin
finally {
    withContext(NonCancellable) {
        db.close() // This will run even if cancelled
    }
}
```

---

## 6. Coroutine Builders

### `launch`
"Fire and forget". Returns a `Job`. Used when you don't need a result back. It does not block the thread it's called from.

### `async`
"Perform and return". Returns a `Deferred<T>`. Use `.await()` to get the result. It also does not block the thread.

### `runBlocking`
**Blocks the current thread** until all coroutines inside it finish.
*   **Usage:** Mostly used in `main()` functions or Unit Tests.
*   **Warning:** **Never** use this in Android UI code (Activity/Fragment/ViewModel) as it will freeze the UI.
```kotlin
fun main() = runBlocking {
    // This thread is now blocked until this block finishes
    launch { 
        delay(1000)
        println("Done")
    }
}
```

### `withContext`
Changes the dispatcher of the current coroutine. It is **not** a builder that starts a new coroutine; it just switches threads and returns the result.
```kotlin
val result = withContext(Dispatchers.IO) {
    // Perform blocking IO here
    "Data"
}
```

---

## 7. Exception Handling

1.  **`try-catch`**: Standard way to catch errors inside `launch` or `async`.
2.  **`CoroutineExceptionHandler`**: A global handler for the scope. **Note:** It only works when set on the parent scope or a root `launch`.
3.  **`supervisorScope`**: Prevents failure propagation to siblings.

---

## 8. Summary Checklist
*   **Need parallelism?** Use `async`.
*   **Independent tasks?** Use `SupervisorJob` or `supervisorScope`.
*   **Long CPU loop?** Add `yield()` or `ensureActive()`.
*   **IO task?** Use `Dispatchers.IO`.
*   **Cleanup on cancel?** Use `withContext(NonCancellable)`.
