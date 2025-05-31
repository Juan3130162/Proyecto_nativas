package com.example.proyecto_nativas.Activities.Galeria

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.adapters.GaleriaAdapter
import com.example.proyecto_nativas.models.Producto
import com.google.firebase.firestore.FirebaseFirestore

class GalleryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val listaImagenes = mutableListOf<String>()
    private lateinit var adapter: GaleriaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        recyclerView = findViewById(R.id.recyclerGaleria)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columnas

        adapter = GaleriaAdapter(listaImagenes, this)
        recyclerView.adapter = adapter

        cargarImagenesDesdeFirestore()
    }

    private fun cargarImagenesDesdeFirestore() {
        FirebaseFirestore.getInstance().collection("productos")
            .get()
            .addOnSuccessListener { result ->
                listaImagenes.clear()
                for (document in result) {
                    val producto = document.toObject(Producto::class.java)
                    producto.imagen_url?.let { url ->
                        listaImagenes.add(url)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar imágenes", Toast.LENGTH_SHORT).show()
                Log.e("Firestore", "Error al leer imágenes: ", it)
            }
    }
}
