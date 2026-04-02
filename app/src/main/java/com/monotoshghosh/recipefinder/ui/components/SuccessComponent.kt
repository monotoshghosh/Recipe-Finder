package com.monotoshghosh.recipefinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout

import com.monotoshghosh.recipefinder.data.model.Meal

@Composable
fun SuccessComponent(
    recipes: List<Meal>,
    onSearchClicked: (query: String) -> Unit,
    onLogoutClicked: () -> Unit
) {

    // state to control dialog visibility
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .systemBarsPadding()
            .padding(8.dp)
    ) {

        // Top Row (Title + Logout)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Recipe Finder😋",
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 11.dp)
            )

            // Logout button
            IconButton(
                onClick = {
                    showDialog = true   // SHOW DIALOG instead of direct logout
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color.Gray,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text("Logout")
                },
                text = {
                    Text("Are you sure you want to logout?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            onLogoutClicked()
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Existing UI
        SearchComponent(onSearchClicked = onSearchClicked)
        RecipesList(recipes = recipes)
    }
}