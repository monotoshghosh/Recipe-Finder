package com.monotoshghosh.recipefinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.monotoshghosh.recipefinder.data.model.Meal

@Composable
fun RecipesList(recipes: List<Meal>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ){
        items(recipes) {
            RecipeListItem(it)
        }
    }
}