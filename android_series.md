# Android Series: Activity & Fragment Lifecycle

Understanding Lifecycles is fundamental to Android development. It ensures your app handles transitions smoothly, saves user data, and manages resources efficiently.

---

## Activity Lifecycle Methods
(Same as above...)

---

## The Core Lifecycle Methods

### 1. `onCreate()`
*   **Called when:** The activity is first created.
*   **Use for:** Static setup like `setContentView`, initializing ViewModels, or setting up one-time logic.
*   **Status:** The activity is created but not yet visible.

### 2. `onStart()`
*   **Called when:** The activity is about to become visible to the user.
*   **Use for:** Starting UI-related updates or animations.
*   **Status:** Activity is visible but not interactive.

### 3. `onResume()`
*   **Called when:** The activity enters the foreground.
*   **Use for:** Starting sensors, camera previews, or resume interactive tasks.
*   **Status:** Activity is running and in focus (user can interact).

### 4. `onPause()`
*   **Called when:** The activity is partially obscured (e.g., a transparent dialog appears) or the user is leaving it.
*   **Use for:** Pausing ongoing actions (like video) or saving light data.
*   **Status:** Activity is still visible but losing focus. **Keep this method fast.**

### 5. `onStop()`
*   **Called when:** The activity is no longer visible to the user.
*   **Use for:** Heavy cleanup, stopping background syncs, or saving data to a database.
*   **Status:** Activity is hidden but still in memory.

### 6. `onRestart()`
*   **Called when:** The activity was stopped and is being started again.
*   **Use for:** Logic specific to returning to the activity.
*   **Followed by:** `onStart()`.

### 7. `onDestroy()`
*   **Called when:** The activity is finishing (via `finish()`) or being destroyed by the system.
*   **Use for:** Releasing all remaining resources (like database connections).

---

## Common Scenarios (Cheat Sheet)

| Scenario | Lifecycle Sequence |
| :--- | :--- |
| **App Launched** | `onCreate` → `onStart` → `onResume` |
| **Home Button Pressed** | `onPause` → `onStop` |
| **Returning to App** | `onRestart` → `onStart` → `onResume` |
| **Back Button Pressed** | `onPause` → `onStop` → `onDestroy` |
| **Activity A to Activity B** | **A:** `onPause` → **B:** `onCreate` → `onStart` → `onResume` → **A:** `onStop` |
| **Back from B to A** | **B:** `onPause` → **A:** `onRestart` → `onStart` → `onResume` → **B:** `onStop` → `onDestroy` |
| **Dialog Appears (Partial Focus Loss)** | `onPause` (Only if it's a transparent activity/dialog) |
| **Screen Rotation** | `onPause` → `onStop` → `onDestroy` → `onCreate` → `onStart` → `onResume` |

---

## Fragment Lifecycle Methods

Fragments have a slightly more complex lifecycle because they are hosted within an Activity and have their own View lifecycle.

### 1. `onAttach()`
*   **Called when:** The fragment is first attached to its host activity.
*   **Use for:** Getting a reference to the activity context.

### 2. `onCreate()`
*   **Called when:** The fragment is being created.
*   **Use for:** Initializing non-UI components.

### 3. `onCreateView()`
*   **Called when:** The fragment is about to draw its UI for the first time.
*   **Use for:** Inflating the layout XML.

### 4. `onViewCreated()`
*   **Called when:** The view has been created.
*   **Use for:** View setup (finding views by ID, setting up adapters). **Recommended place for UI logic.**

### 5. `onStart()` / `onResume()`
*   **Similar to Activity:** Becomes visible and then interactive.

### 6. `onPause()` / `onStop()`
*   **Similar to Activity:** Losing focus and then becoming hidden.

### 7. `onDestroyView()`
*   **Called when:** The fragment's view is being removed.
*   **Use for:** Cleaning up references to views (to avoid memory leaks).

### 8. `onDestroy()`
*   **Called when:** Final cleanup for the fragment.

### 9. `onDetach()`
*   **Called when:** The fragment is no longer attached to the activity.

---

## Activity vs Fragment Lifecycle (Combined Flow)

When an Activity contains a Fragment, their lifecycles are intertwined. Here is the exact sequence from creation to destruction:

| Step | Activity Method | Fragment Method | Description |
| :--- | :--- | :--- | :--- |
| 1 | `onCreate()` | | Activity is initialized. |
| 2 | | `onAttach()` | Fragment is linked to the Activity. |
| 3 | | `onCreate()` | Fragment is initialized. |
| 4 | | `onCreateView()` | Fragment's UI layout is inflated. |
| 5 | | `onViewCreated()` | Fragment's view is ready for setup. |
| 6 | | `onActivityCreated()` | (Deprecated) Activity's `onCreate` is complete. |
| 7 | `onStart()` | | Activity becomes visible. |
| 8 | | `onStart()` | Fragment becomes visible. |
| 9 | `onResume()` | | Activity enters foreground. |
| 10 | | `onResume()` | Fragment enters foreground. |
| **--** | **Active State** | **Active State** | **User interacts with both.** |
| 11 | `onPause()` | | Activity is losing focus. |
| 12 | | `onPause()` | Fragment is losing focus. |
| 13 | `onStop()` | | Activity is no longer visible. |
| 14 | | `onStop()` | Fragment is no longer visible. |
| 15 | | `onDestroyView()` | Fragment's view is being removed. |
| 16 | | `onDestroy()` | Fragment is being destroyed. |
| 17 | | `onDetach()` | Fragment is unlinked from Activity. |
| 18 | `onDestroy()` | | Activity is being destroyed. |

---

## Fragment Navigation (A to B)
When using `fragmentTransaction.replace()` with `addToBackStack()`:

| Direction | Fragment A (Source) | Fragment B (Destination) |
| :--- | :--- | :--- |
| **A → B** | `onPause()` | |
| | `onStop()` | |
| | `onDestroyView()` | |
| | | `onAttach()` |
| | | `onCreate()` |
| | | `onCreateView()` |
| | | `onViewCreated()` |
| | | `onStart()` |
| | | `onResume()` |
| **B → A (Back)** | | `onPause()` |
| | | `onStop()` |
| | | `onDestroyView()` |
| | | `onDestroy()` |
| | | `onDetach()` |
| | `onCreateView()` | |
| | `onViewCreated()` | |
| | `onStart()` | |
| | `onResume()` | |

---

## Best Practices
1.  **Don't block the UI thread:** Keep lifecycle methods fast to avoid "Application Not Responding" (ANR) errors.
2.  **Symmetry:** If you start something in `onStart`, stop it in `onStop`. If you start it in `onResume`, stop it in `onPause`.
3.  **Save State:** Use `onSaveInstanceState()` to save transient UI state (like scroll position) before the activity is killed by the system.

---

## 🧠 Memory Hacks: How to remember this easily?

### 1. The "Symmetry" Rule (The Mirror)
Think of the lifecycle as a mirror. For every "start" method, there is a corresponding "stop" method.

*   **Whole Life:** `onCreate()` ↔ `onDestroy()` (Memory allocated/released)
*   **Visible Life:** `onStart()` ↔ `onStop()` (User can see/cannot see)
*   **Foreground Life:** `onResume()` ↔ `onPause()` (User can interact/cannot interact)

### 2. The "Guest at a House" Metaphor
Imagine an Activity is a guest visiting your house:
1.  **`onCreate`**: Guest arrives and puts their bags in the room.
2.  **`onStart`**: Guest walks into the living room (visible).
3.  **`onResume`**: Guest starts talking to you (interactive).
4.  **`onPause`**: Guest's phone rings; they stop talking but are still in the room.
5.  **`onStop`**: Guest goes back to their room and closes the door (not visible).
6.  **`onDestroy`**: Guest packs bags and leaves the house.

### 3. The "Fragment View Sandwich"
Fragments are just Activities with a "View Sandwich" in the middle of creation:
*   `onCreate()` (The bread)
*   `onCreateView()` (The filling - layout)
*   `onViewCreated()` (The seasoning - setup logic)
*   `onDestroyView()` (The bread)

### 4. Key Acronym: **C-S-R**
Just remember **C-S-R** to go UP:
*   **C**reate
*   **S**tart
*   **R**esume

And the reverse to go DOWN (P-S-D):
*   **P**ause
*   **S**top
*   **D**estroy

---

## Broadcast Receiver

A Broadcast Receiver is an Android component that allows you to register for system or app events. All registered receivers for an event are notified by the Android runtime when the event happens.

### 1. Types of Registration
*   **Static (Manifest):** Declared in `AndroidManifest.xml`. Works even if the app is not running (limited in newer Android versions).
*   **Dynamic (Context-based):** Registered in code (e.g., in an Activity). Only works while the context is alive.

### 2. Implementation Example
```kotlin
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Logic goes here
    }
}
```

### 3. Usage in Activity (Dynamic Registration)
```kotlin
class MyActivity : Activity() {
    private val receiver = MyReceiver()

    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter("ACTION_NAME"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver) // Crucial to prevent memory leaks
    }
}
```

---

## Hilt in Broadcast Receiver

Since the Android system instantiates `BroadcastReceiver`, you cannot use constructor injection. Instead, you must use **Field Injection**.

### 1. Requirements
*   Annotate the class with `@AndroidEntryPoint`.
*   The `Application` class must be annotated with `@HiltAndroidApp`.
*   Use `@Inject lateinit var` for your dependencies.

### 2. Implementation Example
```kotlin
@AndroidEntryPoint
class MyReceiver : BroadcastReceiver() {

    @Inject
    lateinit var myRepository: MyRepository

    override fun onReceive(context: Context, intent: Intent) {
        // Hilt handles the injection automatically before onReceive is called.
        // You can use myRepository here immediately.
    }
}
```

> **Note:** Do NOT call `super.onReceive()` in a `BroadcastReceiver`, as `onReceive` is an abstract method in the base class and calling it will cause a compilation error.
