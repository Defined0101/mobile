package com.defined.mobile.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Required imports for Google Sign-In and Firebase Authentication
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
// Import the custom ViewModel for Login
import com.google.firebase.auth.FirebaseUser

@Composable
fun LoginPage(
    viewModel: LoginViewModel,
    onSignInClick: (FirebaseUser?) -> Unit
) {
    val googleSignInClient = remember { viewModel.getGoogleSignInClient() }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        Log.d("FirebaseAuth", "User: ${user?.email}, UID: ${user?.uid}")
                        onSignInClick(user) // Notify about successful login
                    } else {
                        Log.e("FirebaseAuth", "Error: ${authResult.exception?.message}")
                        onSignInClick(null) // Notify about login failure
                    }
                }
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Login error: ${e.localizedMessage}")
            onSignInClick(null) // Notify about login failure
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "WELCOME",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = {
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }
        }) {
            Text(text = "Sign in with Google")
        }
    }
}

fun currentUser(): FirebaseUser? {
    return FirebaseAuth.getInstance().currentUser
}

fun logOut() {
    FirebaseAuth.getInstance().signOut()
}
