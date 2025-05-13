package com.example.proyecto_nativas.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.adapters.UsuarioAdapter
import com.example.proyecto_nativas.models.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class VerUsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtSinUsuarios: TextView
    private lateinit var adapter: UsuarioAdapter
    private val listaUsuarios = mutableListOf<Usuario>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_usuarios)

        recyclerView = findViewById(R.id.recyclerUsuarios)
        txtSinUsuarios = findViewById(R.id.tvSinUsuarios)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UsuarioAdapter(listaUsuarios)
        recyclerView.adapter = adapter

        cargarUsuariosDesdeFirestore()
    }

    private fun cargarUsuariosDesdeFirestore() {
        FirebaseFirestore.getInstance().collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                listaUsuarios.clear()
                for (doc in result) {
                    val usuario = Usuario(
                        id = doc.id,
                        nombre = doc.getString("nombre") ?: "Desconocido",
                        email = doc.getString("email") ?: "Sin correo",
                        admin = doc.getBoolean("admin") ?: false
                    )
                    listaUsuarios.add(usuario)
                }
                adapter.actualizarLista(listaUsuarios)
                txtSinUsuarios.visibility = if (listaUsuarios.isEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener {
                txtSinUsuarios.text = "Error al cargar usuarios."
                txtSinUsuarios.visibility = View.VISIBLE
            }
    }
}
