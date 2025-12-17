package com.fred.swyppy.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fred.swyppy.navigation.ROUTE_ADD_PROPERTY
import com.fred.swyppy.navigation.ROUTE_MAIN_SCREEN
import com.fred.swyppy.ui.theme.newpurple
import com.fred.swyppy.data.AuthViewModel // <-- Ensure this is imported

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    // AuthViewModel initialization is correct
    val authViewModel: AuthViewModel = remember {
        AuthViewModel(navController, context)
    }

    // State variables are defined using 'by' delegate
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Title
        Text(
            text = "Swyppy",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = newpurple
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome Back",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = "Email")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = newpurple,
                unfocusedLeadingIconColor = newpurple,
                focusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = newpurple,
                unfocusedLeadingIconColor = newpurple,
                focusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Remember Me Checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(checkedColor = newpurple)
            )
            Text(
                text = "Remember Me",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Login Button
        // Login Button
        Button(
            onClick = {
                // Set loading state on click
                isLoading = true
                errorMessage = ""

                // 🆕 PASS rememberMe TO signIn
                authViewModel.signIn(
                    email.trim(),
                    password.trim(),
                    rememberMe = rememberMe,  // 🆕 ADD THIS LINE
                    onSuccess = { role ->
                        isLoading = false
                        // Conditional navigation based on role from AuthViewModel
                        if (role == "admin") {
                            navController.navigate(ROUTE_ADD_PROPERTY) {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            navController.navigate(ROUTE_MAIN_SCREEN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    onFailure = { errorMsg ->
                        isLoading = false
                        errorMessage = errorMsg
                        Toast.makeText(context, "Login failed: $errorMsg", Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = newpurple),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen(rememberNavController())
}