package com.example.retrofit_da1.UI.Profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.retrofit_da1.Data.Products.ApiException
import com.example.retrofit_da1.R
import com.example.retrofit_da1.UI.Main.MainActivity
import com.example.retrofit_da1.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.btnGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent,100)

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100){
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val ac = accountTask.getResult(ApiException::class.java)
                firebaseGoogle(ac)
            }catch (e:Exception){

            }
        }
    }


    private fun firebaseGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val firebaseUser = auth.currentUser
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email

                if (authResult.additionalUserInfo!!.isNewUser) {
                    // Crear Account
                    Toast.makeText(this@AuthActivity, "Cuenta creada...", Toast.LENGTH_LONG).show()
                    goHome()
                }
                else {
                    Toast.makeText(this@AuthActivity, "Cuenta existente...", Toast.LENGTH_LONG).show()
                    goHome()
                }
                goHome()
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                Toast.makeText(this@AuthActivity, "Login fallido...", Toast.LENGTH_LONG).show()
            }
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
        val homeIntent = Intent(this, MainActivity::class.java).apply{
            putExtra("emial", binding.etEmail.text.toString())
        }
        startActivity(homeIntent)
    }
}
