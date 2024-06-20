package com.example.retrofit_da1.UI

import android.content.Context
import android.provider.Settings
import android.provider.Settings.Global.getString
import androidx.lifecycle.ViewModel
import com.example.retrofit_da1.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerWithEmailAndPassword(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun signInWithEmailAndPassword(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun LogOut(){
        auth.signOut()
    }


}