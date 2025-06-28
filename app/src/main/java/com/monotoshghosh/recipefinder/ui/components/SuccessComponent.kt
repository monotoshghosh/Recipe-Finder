package com.monotoshghosh.recipefinder.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.monotoshghosh.recipefinder.data.model.Meal

@Composable
fun SuccessComponent(recipes: List<Meal>, onSearchClicked: (query: String) -> Unit) {
    Column {
        Text(
            text = "Recipe Finder",
            fontWeight = FontWeight(900),
            fontFamily = FontFamily.Serif,
            fontSize = 32.sp,
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
        )
        SearchComponent(onSearchClicked = onSearchClicked)
        RecipesList(recipes = recipes)
    }
}