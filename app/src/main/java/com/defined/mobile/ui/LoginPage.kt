package com.defined.mobile.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defined.mobile.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser

@Composable
fun LoginPage(
    viewModel: LoginViewModel,
    onSignInClick: (FirebaseUser?) -> Unit
) {
    var isRegistering by remember { mutableStateOf(false) }

    if (isRegistering) {
        RegisterScreen(viewModel, onSignInClick) { isRegistering = false }
    } else {
        LoginScreen(viewModel, onSignInClick) { isRegistering = true }
    }
}

// **Login Ekranı**
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onSignInClick: (FirebaseUser?) -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var attemptedLogin by remember { mutableStateOf(false) }

    val googleSignInClient = remember { viewModel.getGoogleSignInClient() }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            viewModel.firebaseAuthWithGoogle(account?.idToken ?: "") { user, error ->
                if (user != null) {
                    onSignInClick(user)
                } else {
                    errorMessage = error
                }
            }
        } catch (e: Exception) {
            errorMessage = e.localizedMessage
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("LOGIN", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = attemptedLogin && email.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        if (attemptedLogin && email.isBlank()) {
            Text("Email cannot be empty", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            isError = attemptedLogin && password.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        if (attemptedLogin && password.isBlank()) {
            Text("Password cannot be empty", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Email and password cannot be empty"
                } else {
                    viewModel.signInWithEmail(email, password) { user, error ->
                        if (user != null) {
                            onSignInClick(user)
                        } else {
                            errorMessage = error
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Email")
        }

        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        GoogleSignInButton {
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        }

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = ButtonDefaults.outlinedButtonBorder,
        elevation = null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_logo), // Google logosu
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sign in with Google",
                color = Color.Black // Yazıyı siyah yap
            )
        }
    }
}

// **Register Ekranı**
@Composable
fun RegisterScreen(
    viewModel: LoginViewModel,
    onSignInClick: (FirebaseUser?) -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isEmailSent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("REGISTER", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.signUpWithEmail(email, password) { user, error ->
                    if (user != null) {
                        isEmailSent = true
                    } else {
                        errorMessage = error
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        if (isEmailSent) {
            Text(
                text = "A verification email has been sent. Please check your inbox!",
                color = MaterialTheme.colorScheme.primary
            )
        }

        Button(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login")
        }

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}

