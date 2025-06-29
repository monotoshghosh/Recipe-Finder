package com.monotoshghosh.recipefinder

import android.os.Bundle
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

class MainActivity : ComponentActivity() {
    private val recipeViewModel: RecipeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

