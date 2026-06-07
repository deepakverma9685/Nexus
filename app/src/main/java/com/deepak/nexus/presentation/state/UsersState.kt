package com.deepak.nexus.presentation.state

import com.deepak.nexus.domain.models.User

/*
 * SEALED CLASS:
 * 1. Can contain state (properties with backing fields).
 * 2. Can have constructors (useful if all subclasses need a common property).
 * 3. Follows single inheritance (a subclass can only extend one class).
 *
 * WHEN TO USE: Use when your subclasses share a common base implementation or need
 * to hold common state variables.
 * EXAMPLE: When all states need a `timestamp` or `networkType` property defined
 * in the base class.
 */
sealed class UsersState {
    // Used for representing the initial or loading state of the user list.
    object Loading : UsersState()

    // Used when users are successfully fetched, carrying the data payload.
    data class Success(val users: List<User>) : UsersState()

    // Used to capture and display an error message if the fetch fails.
    data class Error(val message: String) : UsersState()
}

/*
 * SEALED INTERFACE:
 * 1. Cannot contain state (no backing fields).
 * 2. Cannot have constructors.
 * 3. Supports multiple inheritance (a class can implement multiple interfaces).
 * 4. More lightweight and flexible for defining common behavior across different hierarchies.
 *
 * WHEN TO USE: Use when you want to define a type hierarchy without any base state,
 * or when a subclass needs to belong to multiple sealed hierarchies.
 * EXAMPLE: A `Success` state that needs to implement both `UsersState` and
 * a `Loggable` interface.
 */
sealed interface UsersState2 {
    // Used for representing the initial or loading state of the user list.
    object Loading : UsersState2

    // Used when users are successfully fetched, carrying the data payload.
    data class Success(val users: List<User>) : UsersState2

    // Used to capture and display an error message if the fetch fails.
    data class Error(val message: String) : UsersState2
}