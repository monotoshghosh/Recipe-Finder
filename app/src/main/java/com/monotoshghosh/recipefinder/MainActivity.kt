package com.monotoshghosh.recipefinder

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.monotoshghosh.recipefinder.ui.screens.HomeScreen
import com.monotoshghosh.recipefinder.ui.theme.RecipeFinderAppTheme
import com.monotoshghosh.recipefinder.ui.viewmodel.RecipeViewModel

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability


class MainActivity : ComponentActivity() {
    private val recipeViewModel: RecipeViewModel by viewModels()

    // In-App Update Manager
    private lateinit var appUpdateManager: AppUpdateManager

    // Request code for update
    private val updateRequestCode = 100

    // Listener for update state (download complete etc.)
    private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {

            // Update downloaded in background
            Toast.makeText(this, "Update downloaded. Restarting...", Toast.LENGTH_LONG).show()

            // Complete update (installs update & restarts app)
            appUpdateManager.completeUpdate()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize update manager
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Register listener (VERY IMPORTANT)
        appUpdateManager.registerListener(listener)

        // Check for update when app starts
        checkForUpdate()

        setContent {
            RecipeFinderAppTheme {
                // Setup system UI colors
                SetSystemBarsColor()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(recipeViewModel = recipeViewModel)
                }
            }
        }
    }

    // Function to check update availability
    private fun checkForUpdate() {

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

            // Check if update available AND flexible update allowed
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {

                // Start flexible update
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    updateRequestCode
                )
            }
        }
    }

    // Unregister listener (IMPORTANT to avoid memory leaks)
    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(listener)
    }

    @Composable
    fun SetSystemBarsColor() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = false // false = white icons

        SideEffect {
            // Explicitly set both
            systemUiController.setStatusBarColor(
                color = Color.Black,
                darkIcons = useDarkIcons
            )
            systemUiController.setNavigationBarColor(
                color = Color.Black,
                darkIcons = useDarkIcons
            )
        }
    }


}

