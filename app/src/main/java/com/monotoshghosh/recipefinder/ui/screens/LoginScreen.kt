package com.monotoshghosh.recipefinder.ui.screens

import android.app.Activity // 🔥 NEW
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult // 🔥 NEW
import androidx.activity.result.contract.ActivityResultContracts // 🔥 NEW

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.text.input.VisualTransformation
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.res.painterResource
import com.monotoshghosh.recipefinder.R

// 🔥 NEW IMPORTS
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as Activity // 🔥 NEW
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // 🔥 NEW: Google Sign-In setup
    val googleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("495564766549-cautihtg791k8cbst6hmdlkvjdvk2lem.apps.googleusercontent.com")
            .requestEmail()
            .build()
    )

    // 🔥 NEW: launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)

                val credential = GoogleAuthProvider.getCredential(
                    account.idToken,
                    null
                )

                isGoogleLoading = true

                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        isGoogleLoading = false

                        if (it.isSuccessful) {
                            Toast.makeText(context, "Google Login Success", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

            } catch (e: Exception) {
                Toast.makeText(context, "Google Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

            Text(
                "Welcome Back",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6FE7DD),
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(0.04f),
                    unfocusedContainerColor = Color.White.copy(0.04f)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, null) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6FE7DD),
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White.copy(0.04f),
                    unfocusedContainerColor = Color.White.copy(0.04f)
                )
            )

            // Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    if (email.isEmpty()) {
                        Toast.makeText(context, "Enter email first", Toast.LENGTH_SHORT).show()
                        return@TextButton
                    }
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    Toast.makeText(context, "Reset link sent", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Forgot Password?", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // LOGIN BUTTON
            Button(
                onClick = {

                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (password.length < 6) {
                        Toast.makeText(context, "Password must be 6+ chars", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            isLoading = false
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                                onLoginSuccess()
                            } else {
                                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6FE7DD))
            ) {
                if (isLoading)
                    CircularProgressIndicator(color = Color.Black)
                else
                    Text("Login", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(25.dp))

            // OR
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(0.2f))
                Text(" OR ", color = Color.White.copy(alpha = 0.7f))
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(0.2f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Continue Using", color = Color.White.copy(alpha = 0.7f))

            Spacer(modifier = Modifier.height(10.dp))

            // 🔥 UPDATED GOOGLE BUTTON
            IconButton(
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? Sign Up", color = Color.White.copy(alpha = 0.7f))
            }
        }

        // GOOGLE LOADING TOP INDICATOR
        if (isGoogleLoading) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(10.dp)
            ) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    color = Color(0xFF6FE7DD)
                )
            }
        }
    }
}