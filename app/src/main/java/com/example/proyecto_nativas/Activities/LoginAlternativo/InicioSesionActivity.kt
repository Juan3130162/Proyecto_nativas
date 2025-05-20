package com.example.proyecto_nativas.Activities.LoginAlternativo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.Activities.ListaProductosActivity
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.data.CarritoRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class InicioSesionActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)
        val loadedClientId = getString(R.string.default_web_client_id)
        Log.d("SignInDebug", "default_web_client_id = $loadedClientId")

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // webClientId en strings.xml
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(this, gso)

        val edtEmail = findViewById<EditText>(R.id.edtEmailLogin)
        val edtPassword = findViewById<EditText>(R.id.edtPasswordLogin)
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            onLoginSuccess(email)
                        } else {
                            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btn_sign_in_google).setOnClickListener {
            startActivityForResult(googleClient.signInIntent, RC_SIGN_IN)
        }

        findViewById<Button>(R.id.btn_registrarse_Alt).setOnClickListener {
            startActivity(Intent(this, CrearCuentaActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { token ->
                    firebaseAuthWithGoogle(token)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in falló: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { authResult ->
                if (authResult.isSuccessful) {
                    val email = auth.currentUser?.email ?: ""
                    onLoginSuccess(email)
                } else {
                    Toast.makeText(this, "Autenticación Firebase falló", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun onLoginSuccess(email: String) {
        CarritoRepository.init(applicationContext) // <- ¡IMPORTANTE!
        Intent(this, ListaProductosActivity::class.java).also { intent ->
            intent.putExtra("email", email)
            startActivity(intent)
        }
        finish()
    }
}

