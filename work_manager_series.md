# Android WorkManager: The Complete Guide

WorkManager is the recommended solution for persistent work. It handles background tasks that need to run even if the app exits or the device restarts.

---

## 1. Defining a Worker
A `Worker` is where you put the actual code to be executed in the background.

```kotlin
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    // This method runs on a background thread automatically
    override fun doWork(): Result {
        // 1. Retrieve input data sent from the ViewModel/Activity
        val inputData = inputData.getString("key_name")

        return try {
            // 2. Perform the background task (e.g., uploading a file)
            println("Working on: $inputData")

            // 3. Create output data to send back results
            val outputData = workDataOf("result_key" to "Task Completed Successfully")

            // 4. Return Result.success() with data, or Result.retry() / Result.failure()
            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
```

---

## 2. Types of Work Requests

### A. One-Time Work Request
Runs exactly once as soon as constraints are met.

```kotlin
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when internet is available
    .setRequiresCharging(true)                    // Only run when device is charging
    .build()

val oneTimeRequest = OneTimeWorkRequestBuilder<MyWorker>()
    .setConstraints(constraints)                   // Apply the constraints defined above
    .setInputData(workDataOf("key_name" to "Upload Image")) // Pass data to the worker
    .setInitialDelay(15, TimeUnit.MINUTES)         // Wait 15 mins before starting
    .addTag("upload_tag")                          // Tag used to cancel or monitor work
    .build()

// Schedule it
WorkManager.getInstance(context).enqueue(oneTimeRequest)
```

### B. Periodic Work Request
Runs repeatedly on a cycle. The minimum interval is **15 minutes**.

```kotlin
val periodicRequest = PeriodicWorkRequestBuilder<MyWorker>(
    1, TimeUnit.HOURS,       // Repeat interval (every 1 hour)
    15, TimeUnit.MINUTES     // Flex interval (run in the last 15 mins of the hour)
).build()

WorkManager.getInstance(context).enqueue(periodicRequest)
```

### C. Unique Work
Ensures that only one instance of a specific task runs at a time. This prevents duplicate background tasks.

```kotlin
WorkManager.getInstance(context).enqueueUniqueWork(
    "sync_data_unique",                            // Unique name for this work
    ExistingWorkPolicy.KEEP,                      // KEEP existing work, or REPLACE with new one
    oneTimeRequest
)
```

---

## 3. Chaining Work (Sequential Execution)
You can run multiple tasks in a specific order.

```kotlin
WorkManager.getInstance(context)
    .beginWith(filterWorker)                       // Start with Worker A
    .then(compressWorker)                          // Then run Worker B
    .then(uploadWorker)                            // Then run Worker C
    .enqueue()
```

---

## 4. Monitoring Work Progress
You can observe the status of your background task in your UI/ViewModel using `LiveData` or `Flow`.

```kotlin
WorkManager.getInstance(context)
    .getWorkInfoByIdLiveData(oneTimeRequest.id)    // Get status by the unique ID
    .observe(owner) { workInfo ->
        if (workInfo != null && workInfo.state.isFinished) {
            val myResult = workInfo.outputData.getString("result_key")
            // Update UI with the result
        }
    }
```

---

## Summary Table

| Type | Best Used For | Key Feature |
| :--- | :--- | :--- |
| **One-Time** | Instant tasks like file uploads. | Runs once, supports chaining. |
| **Periodic** | Syncing data, clearing cache. | Repeats automatically (min 15 min). |
| **Unique** | Syncing DB to Cloud. | Prevents duplicate task execution. |
| **Chained** | Image processing pipeline. | Runs tasks in a specific sequence. |

---

## What Line Does What? (Cheat Sheet)
* `Worker`: The class that holds the background logic.
* `doWork()`: The entry point for the task; runs on a background thread.
* `Result.success()`: Tells WorkManager the task is done; cleanup can happen.
* `Constraints`: Conditions (Wifi, Battery) that must be met for the task to start.
* `enqueue()`: The command that actually schedules the task.
