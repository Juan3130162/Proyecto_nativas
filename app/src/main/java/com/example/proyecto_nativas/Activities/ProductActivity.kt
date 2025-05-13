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
