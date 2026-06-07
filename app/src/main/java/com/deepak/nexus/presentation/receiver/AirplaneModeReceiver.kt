package com.deepak.nexus.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.deepak.nexus.domain.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * An example BroadcastReceiver that listens for Airplane Mode changes.
 * We use @AndroidEntryPoint to enable Hilt injection.
 */
@AndroidEntryPoint
class AirplaneModeReceiver : BroadcastReceiver() {

    // Inject your dependency here
    @Inject
    lateinit var userRepository: UserRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        // Hilt performs injection automatically before this method is called.
        // There is no need (and it's an error) to call super.onReceive().

        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = intent.getBooleanExtra("state", false)
            
            // Example usage of injected dependency
            println("User repository injected successfully: $userRepository")

            val message = if (isAirplaneModeOn) "Airplane Mode ON" else "Airplane Mode OFF"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
