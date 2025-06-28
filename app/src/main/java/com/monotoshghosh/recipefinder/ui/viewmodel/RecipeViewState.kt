package com.monotoshghosh.recipefinder.ui.viewmodel

import com.monotoshghosh.recipefinder.data.model.Meal

sealed class RecipeViewState {
    object Loading: RecipeViewState()
    data class Success(val recipes: List<Meal>): RecipeViewState()
    data class Error(val message: String): RecipeViewState()
}
