package com.example.proyecto_nativas.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.adapters.ProductoAdapter
import com.example.proyecto_nativas.models.Producto
import com.google.firebase.firestore.FirebaseFirestore
import com.example.proyecto_nativas.data.CarritoRepository


class ListaProductosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val listaProductos = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_productos)

        CarritoRepository.init(applicationContext)


        recyclerView = findViewById(R.id.recyclerFirebase)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductoAdapter(
            listaProductos,
            onAddToCartClick = { producto ->
                // Acción futura para agregar al carrito
                Toast.makeText(this, "${producto.nombre} agregado", Toast.LENGTH_SHORT).show()
            },
            onItemClick = { producto ->
                val intent = Intent(this, DetallesDeProductosActivity::class.java)
                intent.putExtra("producto_nombre", producto.nombre)
                intent.putExtra("producto_precio", producto.precio)
                intent.putExtra("producto_descripcion", producto.descripcion)
                intent.putExtra("producto_imagen", producto.imagen_url)
                startActivity(intent)
            }
        )

        recyclerView.adapter = adapter

        obtenerProductosDesdeFirestore()

        val userEmail = intent.getStringExtra("email") ?: "Usuario"

    }

    private fun obtenerProductosDesdeFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                listaProductos.clear()
                for (document in result) {
                    val producto = document.toObject(Producto::class.java)
                    listaProductos.add(producto)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                Log.e("Firestore", "Error al leer: ", error)
            }
    }
}
