package com.monotoshghosh.recipefinder.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.google.firebase.auth.FirebaseAuth
import com.monotoshghosh.recipefinder.ui.components.ErrorComponent
import com.monotoshghosh.recipefinder.ui.components.LoadingComponent
import com.monotoshghosh.recipefinder.ui.components.SuccessComponent
import com.monotoshghosh.recipefinder.ui.viewmodel.RecipeViewIntent
import com.monotoshghosh.recipefinder.ui.viewmodel.RecipeViewModel
import com.monotoshghosh.recipefinder.ui.viewmodel.RecipeViewState

@Composable
fun HomeScreen(
    recipeViewModel: RecipeViewModel,

    onLogout: () -> Unit

) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val state by recipeViewModel.state

    when(state) {
        is RecipeViewState.Loading -> LoadingComponent()
        is RecipeViewState.Success -> {
            val recipes = (state as RecipeViewState.Success).recipes
            SuccessComponent(recipes = recipes, onSearchClicked = {query ->
                recipeViewModel.processIntent(RecipeViewIntent.SearchRecipes(query)

                )
            },

                // Logout logic
                onLogoutClicked = {

                    val googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(
                        context,
                        com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                            com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
                        )
                            .requestEmail()
                            .build()
                    )

                    // 🔥 Google logout
                    googleSignInClient.signOut()
                    googleSignInClient.revokeAccess()

                    // 🔥 Firebase logout
                    FirebaseAuth.getInstance().signOut()

                    onLogout()
                }
                )
        }
        is RecipeViewState.Error -> {
            val message = (state as RecipeViewState.Error).message
            ErrorComponent(message = message, onRefreshClicked = {
                recipeViewModel.processIntent(RecipeViewIntent.LoadRandomRecipe)
            })
        }
    }

    LaunchedEffect(Unit) {
        recipeViewModel.processIntent(RecipeViewIntent.LoadRandomRecipe)
    }
}