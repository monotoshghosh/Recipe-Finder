package com.monotoshghosh.recipefinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ✅ NEW IMPORTS (for keyboard actions)
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchComponent(onSearchClicked: (query: String) -> Unit) {

    var query by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // for hiding keyboard & removing focus
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .background(Color.Black),

            value = query,

            onValueChange = {
                if (it.isNotBlank()) errorMessage = ""
                query = it
            },

            label = { Text("Search", color = Color.White) },

            singleLine = true,
            isError = errorMessage.isNotBlank(),

            shape = RoundedCornerShape(17.dp),

            // Shows "Search" button in keyboard instead of "Enter"
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),

            // Handle keyboard search button click
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (query.isNotBlank()) {

                        // hide keyboard
                        keyboardController?.hide()

                        // remove focus
                        focusManager.clearFocus()

                        // Trigger search
                        onSearchClicked(query)

                    } else {
                        errorMessage = "Enter a query first"
                    }
                }
            ),

            trailingIcon = {
                IconButton(onClick = {
                    if (query.isNotBlank()) {

                        // same behavior for icon click
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onSearchClicked(query)

                    } else {
                        errorMessage = "Enter a query first"
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            },

            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.White,
                unfocusedLabelColor = Color.LightGray,
                focusedLabelColor = Color.White,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}