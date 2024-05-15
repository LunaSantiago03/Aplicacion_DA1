package com.example.retrofit_da1.UI

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.retrofit_da1.R
import com.example.retrofit_da1.databinding.ActivityAuthBinding
import com.example.retrofit_da1.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private fun setUp() {

        binding.btnRegister.setOnClickListener {
            if (binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()) {
                viewModel.registerWithEmailAndPassword(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                ) { success ->
                    if (success) {
                        goHome()
                    } else {
                        showAlert()
                    }
                }
            }
        }

        binding.btnIniciar.setOnClickListener {
            if (binding.etEmail.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()) {
                viewModel.signInWithEmailAndPassword(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                ) { success ->
                    if (success) {
                        goHome()
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se produjo un error al autenticar")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goHome(){
        val homeIntent = Intent(this,MainActivity::class.java)
        startActivity(homeIntent)
    }
}
