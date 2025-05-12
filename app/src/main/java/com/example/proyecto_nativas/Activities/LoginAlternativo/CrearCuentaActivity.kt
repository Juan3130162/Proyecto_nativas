package com.example.proyecto_nativas.Activities.LoginAlternativo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.Activities.ProductActivity
import com.example.proyecto_nativas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val edtEmail = findViewById<EditText>(R.id.edtEmailRegistro)
        val edtPassword = findViewById<EditText>(R.id.edtPasswordRegistro)
        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtApellido = findViewById<EditText>(R.id.edtApellido)
        val edtEdad = findViewById<EditText>(R.id.edtEdad)
        val edtUsuario = findViewById<EditText>(R.id.edtUsuario)
        val btnRegistro = findViewById<Button>(R.id.btnRegistrarUsuario)

        btnRegistro.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val nombre = edtNombre.text.toString()
            val apellido = edtApellido.text.toString()
            val edad = edtEdad.text.toString().toIntOrNull() ?: 0
            val usuario = edtUsuario.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && nombre.isNotEmpty() && apellido.isNotEmpty() && usuario.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val uid = auth.currentUser?.uid ?: return@addOnSuccessListener

                        val datosUsuario = hashMapOf(
                            "nombre" to nombre,
                            "apellido" to apellido,
                            "edad" to edad,
                            "usuario" to usuario,
                            "email" to email
                        )

                        db.collection("usuarios").document(uid).set(datosUsuario)
                            .addOnSuccessListener {
                                val intent = Intent(this, ProductActivity::class.java)
                                intent.putExtra("email", email)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
