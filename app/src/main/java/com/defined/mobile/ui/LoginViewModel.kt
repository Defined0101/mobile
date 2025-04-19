package com.defined.mobile.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.defined.mobile.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Google Sign-In Client
    fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    // Kullanıcı email doğrulaması yapmış mı kontrol eder
    fun isEmailVerified(): Boolean {
        return auth.currentUser?.isEmailVerified ?: false
    }

    // Email doğrulama linki gönderir
    fun sendEmailVerification(onResult: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }
        }
    }

    // Kullanıcı email & şifre ile kayıt olur ve email doğrulama linki gönderilir
    fun signUpWithEmail(email: String, password: String, onResult: (FirebaseUser?, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    sendEmailVerification { success, error ->
                        if (success) {
                            onResult(user, null)
                        } else {
                            onResult(null, error)
                        }
                    }
                } else {
                    onResult(null, task.exception?.message)
                }
            }
    }

    fun signInWithEmail(email: String, password: String, onResult: (FirebaseUser?, String?) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            onResult(null, "Email and password cannot be empty")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        onResult(user, null)
                    } else {
                        onResult(null, "Please verify your email before logging in.")
                    }
                } else {
                    onResult(null, task.exception?.message)
                }
            }
    }


    // Google Sign-In Authentication
    fun firebaseAuthWithGoogle(idToken: String, onResult: (FirebaseUser?, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(auth.currentUser, null)
                } else {
                    onResult(null, task.exception?.message)
                }
            }
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Logout
    fun logOut() {
        auth.signOut()
    }
}
