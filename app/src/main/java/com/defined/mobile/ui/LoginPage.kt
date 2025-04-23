package com.defined.mobile.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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
    var isEmailSent by remember { mutableStateOf(false) }

    if (isRegistering) {
        RegisterScreen(viewModel, {
            isRegistering = false
            isEmailSent = true
        }) { isRegistering = false }
    } else {
        LoginScreen(viewModel, onSignInClick, { isRegistering = true }, isEmailSent)
    }
}

// **Login Ekranı**
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onSignInClick: (FirebaseUser?) -> Unit,
    onRegisterClick: () -> Unit,
    isEmailSent: Boolean = false
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
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        if (attemptedLogin && password.isBlank()) {
            Text("Password cannot be empty", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                attemptedLogin = true
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = ""
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
            Text("Sign in")
        }

        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
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

        if (isEmailSent) {
            Text(
                text = "A verification email has been sent. Please check your inbox!",
                color = MaterialTheme.colorScheme.primary
            )
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
        shape = MaterialTheme.shapes.extraSmall,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
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
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// **Register Ekranı**
@Composable
fun RegisterScreen(
    viewModel: LoginViewModel,
    onSignUpClick: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isEmailSent by remember { mutableStateOf(false) }
    var attemptedLogin by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SIGN UP", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        if (attemptedLogin && email.isBlank()) {
            Text("Email cannot be empty", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        if (attemptedLogin && password.isBlank()) {
            Text("Password cannot be empty", color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = ""
                } else {
                    viewModel.signUpWithEmail(email, password) { user, intID, error ->
                        if (user != null && intID != null) {
                            // Kayıt başarılı 🎉
                            println("User created: UID=${user.uid}, IntID=$intID")

                        } else {
                            // Hata
                            println("Kayıt başarısız: $error")
                        }
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

