package com.example.proyecto_nativas.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyecto_nativas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var txtNombre: TextView
    private lateinit var txtApellido: TextView
    private lateinit var txtEdad: TextView
    private lateinit var txtUsuario: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var imgPerfil: ImageView
    private lateinit var btnEditar: Button

    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val email = FirebaseAuth.getInstance().currentUser?.email ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        txtNombre = findViewById(R.id.txtNombrePerfil)
        txtApellido = findViewById(R.id.txtApellidoPerfil)
        txtEdad = findViewById(R.id.txtEdadPerfil)
        txtUsuario = findViewById(R.id.txtUsuarioPerfil)
        txtCorreo = findViewById(R.id.txtCorreoPerfil)
        imgPerfil = findViewById(R.id.imgFotoPerfil)
        btnEditar = findViewById(R.id.btnEditarPerfil)

        txtCorreo.text = email

        cargarDatosDeFirestore()

        btnEditar.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }
    }

    private fun cargarDatosDeFirestore() {
        if (uid == null) return

        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    txtNombre.text = document.getString("nombre") ?: "N/A"
                    txtApellido.text = document.getString("apellido") ?: "N/A"
                    txtUsuario.text = document.getString("usuario") ?: "N/A"
                    txtEdad.text = (document.getLong("edad")?.toString() ?: "N/A")
                    txtCorreo.text = document.getString("email") ?: "N/A"

                    val urlFoto = document.getString("foto_url")
                    if (!urlFoto.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(urlFoto)
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder)
                            .into(imgPerfil)
                    }
                } else {
                    txtNombre.text = "No registrado"
                    txtApellido.text = "-"
                    txtUsuario.text = "-"
                    txtEdad.text = "-"
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar perfil", Toast.LENGTH_SHORT).show()
            }
    }

}
