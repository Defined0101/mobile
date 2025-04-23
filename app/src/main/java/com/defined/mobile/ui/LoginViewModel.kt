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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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

    fun handleGoogleUserIfNew(user: FirebaseUser, onResult: (Boolean, String?) -> Unit) {
        val userRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Kullanıcı zaten var, int_id eklemeye gerek yok
                    onResult(true, null)
                } else {
                    // Yeni kullanıcı → int_id üretip kaydet
                    generateNextUserID { intID, idError ->
                        if (intID != null) {
                            saveUserWithIntID(user, intID) { saveSuccess, saveError ->
                                if (saveSuccess) {
                                    onResult(true, null)
                                } else {
                                    onResult(false, "Kayıt başarısız: $saveError")
                                }
                            }
                        } else {
                            onResult(false, "ID üretilemedi: $idError")
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                onResult(false, "Kullanıcı kontrolü başarısız: ${e.message}")
            }
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
    fun signUpWithEmail(
        email: String,
        password: String,
        onResult: (FirebaseUser?, Long?, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // 1. Email doğrulama gönder
                        sendEmailVerification { emailSuccess, emailError ->
                            if (emailSuccess) {
                                // 2. Int ID üret
                                generateNextUserID { intID, idError ->
                                    if (intID != null) {
                                        // 3. Firestore'a kaydet (UID -> int_id eşlemesi)
                                        saveUserWithIntID(user, intID) { saveSuccess, saveError ->
                                            if (saveSuccess) {
                                                onResult(user, intID, null)
                                            } else {
                                                user.delete() // Firebase Authentication'dan da sil
                                                onResult(null, null, "Firestore kayıt hatası: $saveError")
                                            }
                                        }
                                    } else {
                                        onResult(null, null, "ID üretilemedi: $idError")
                                    }
                                }
                            } else {
                                onResult(null, null, "Email doğrulama hatası: $emailError")
                            }
                        }
                    } else {
                        onResult(null, null, "Kullanıcı oluşturuldu ama alınamadı.")
                    }
                } else {
                    onResult(null, null, task.exception?.message)
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
                    val user = auth.currentUser
                    if (user != null) {
                        // UID'yi kontrol et, users'a eklenmiş mi
                        handleGoogleUserIfNew(user) { handled, error ->
                            if (handled) {
                                onResult(user, null)
                            } else {
                                onResult(null, error)
                            }
                        }
                    } else {
                        onResult(null, "Giriş başarılı ama kullanıcı null.")
                    }
                } else {
                    onResult(null, task.exception?.message)
                }
            }
    }


    fun saveUserWithIntID(user: FirebaseUser, intID: Long, onResult: (Boolean, String?) -> Unit) {
        val userData = mapOf(
            "int_id" to intID.toString(),
        )

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    private fun generateNextUserID(onResult: (Long?, String?) -> Unit) {
        val counterRef = FirebaseFirestore.getInstance()
            .collection("metadata").document("userCounter")

        FirebaseFirestore.getInstance().runTransaction { transaction ->
            val snapshot = transaction.get(counterRef)
            val current = snapshot.getLong("count") ?: 1000L
            val next = current + 1
            transaction.update(counterRef, "count", next)
            return@runTransaction next
        }.addOnSuccessListener { nextID ->
            onResult(nextID, null)
        }.addOnFailureListener { e ->
            onResult(null, e.message)
        }
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getIntID(onResult: (String?) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            onResult(null)
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val intID = document.getLong("int_id")?.toString()
                onResult(intID)
            }
            .addOnFailureListener { e ->
                onResult(null)
            }
    }

    // Logout
    fun logOut() {
        auth.signOut()
    }
}
