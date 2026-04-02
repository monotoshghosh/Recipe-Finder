package com.monotoshghosh.recipefinder.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoginPressed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F1C))
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 25.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TITLE
            Text(
                "Create Account",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // NAME
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // PHONE
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },

                visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),

                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },

                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors()
            )

            Spacer(modifier = Modifier.height(35.dp))

            // REGISTER BUTTON
            Button(
                onClick = {

                    if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (password.length < 6) {
                        Toast.makeText(context, "Password must be 6+ chars", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    loading = true

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            loading = false

                            if (it.isSuccessful) {

                                val uid = auth.currentUser?.uid ?: ""

                                // 🔥 Save extra data in Firestore
                                db.collection("users").document(uid).set(
                                    mapOf(
                                        "name" to name,
                                        "phone" to phone,
                                        "email" to email
                                    )
                                )

                                Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                                onRegisterSuccess()

                            } else {
                                Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6FE7DD),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                if (loading)
                    CircularProgressIndicator(color = Color.Black)
                else
                    Text("Register")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // LOGIN TEXT
            TextButton(
                onClick = {
                    onNavigateToLogin()
                }
            ) {
                Text(
                    "Already have an account? Login",
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// 🎨 Reusable TextField Colors (same as your UI)
@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF6FE7DD),
    unfocusedBorderColor = Color.Transparent,
    focusedContainerColor = Color.White.copy(0.04f),
    unfocusedContainerColor = Color.White.copy(0.04f),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color.White
)