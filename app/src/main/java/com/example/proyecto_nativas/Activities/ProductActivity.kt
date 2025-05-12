package com.example.proyecto_nativas.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.adapters.ProductoAdapter
import com.example.proyecto_nativas.models.Producto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

class ProductActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var productos: List<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        recyclerView = findViewById(R.id.recyclerProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        productos = leerProductosDesdeJson()

//        productoAdapter = ProductoAdapter(productos) {
//            // Aquí se manejaría el clic en "Agregar al carrito" (más adelante)
//        }
        productoAdapter = ProductoAdapter(
            productos,
            onAddToCartClick = {
                // Aquí se manejará más adelante
            },
            onItemClick = {
                // Aquí no es necesario hacer nada en esta activity
            }
        )


        recyclerView.adapter = productoAdapter

        ///////////////////////////////////////////////////// base de datos firebase inicio

        val btnGuardar = findViewById<Button>(R.id.btnGuardarProducto)

        btnGuardar.setOnClickListener {
            guardarProductoEnFirestore()
        }
    }

    private fun guardarProductoEnFirestore() {
        val db = FirebaseFirestore.getInstance()

//        val producto = Producto("Celular", "Samsung Galaxy A32", 750000)
        val producto = Producto(
            id = "", // Firestore generará un ID automáticamente si lo dejas vacío
            nombre = "Celular",
            imagen_url = "", // O una URL si la tienes
            descripcion = "Samsung Galaxy A32",
            precio = 750000
        )

        db.collection("productos")
            .add(producto)
            .addOnSuccessListener {
                Log.d("Firestore", "Producto guardado con éxito")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Error al guardar", it)
            }






    }

    /////////////////////////////////////////////////////base de datos firebase fin

    private fun leerProductosDesdeJson(): List<Producto> {
        val inputStream = assets.open("productos.json")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val jsonString = reader.readText()
        reader.close()

        val type = object : TypeToken<List<Producto>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
}
