package com.example.retrofit_da1.UI.Main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.retrofit_da1.R
import com.example.retrofit_da1.UI.Profile.AuthActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private val TimeOut: Long = 3000
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
        Handler().postDelayed({
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // El usuario está autenticado
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // El usuario no autenticado
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, TimeOut)
    }
}