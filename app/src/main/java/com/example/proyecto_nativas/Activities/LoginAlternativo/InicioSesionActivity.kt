package com.example.proyecto_nativas.Activities.LoginAlternativo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.Activities.ListaProductosActivity
import com.example.proyecto_nativas.Activities.LoginActivity
import com.example.proyecto_nativas.Activities.ProductActivity
import com.example.proyecto_nativas.R
import com.google.firebase.auth.FirebaseAuth
import com.example.proyecto_nativas.data.CarritoRepository


class InicioSesionActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        val edtEmail = findViewById<EditText>(R.id.edtEmailLogin)
        val edtPassword = findViewById<EditText>(R.id.edtPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            CarritoRepository.init(applicationContext) // <- ¡IMPORTANTE!
                            val intent = Intent(this, ListaProductosActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        val btn_registrarse_Alt: Button = findViewById(R.id.btn_registrarse_Alt)

        btn_registrarse_Alt.setOnClickListener {
            val intent = Intent(this@InicioSesionActivity, CrearCuentaActivity::class.java)
            startActivity(intent)
        }


    }
}
