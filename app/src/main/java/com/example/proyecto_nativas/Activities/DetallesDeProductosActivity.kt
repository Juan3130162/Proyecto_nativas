package com.example.proyecto_nativas.Activities

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyecto_nativas.R
import java.io.File

class DetallesDeProductosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_de_productos)

        val imgProducto = findViewById<ImageView>(R.id.imgDetalleProducto)
        val txtNombre = findViewById<TextView>(R.id.txtNombreDetalle)
        val txtPrecio = findViewById<TextView>(R.id.txtPrecioDetalle)
        val txtDescripcion = findViewById<TextView>(R.id.txtDescripcionDetalle)

        val nombre = intent.getStringExtra("producto_nombre") ?: "Sin nombre"
        val precio = intent.getIntExtra("producto_precio", 0)
        val descripcion = intent.getStringExtra("producto_descripcion") ?: ""
        val imagenUrl = intent.getStringExtra("producto_imagen") ?: ""

        txtNombre.text = nombre
        txtPrecio.text = "$$precio"
        txtDescripcion.text = descripcion

//        val file = File(imagenUrl)
//        if (file.exists()) {
//            imgProducto.setImageURI(Uri.fromFile(file))
//        } else {
//            imgProducto.setImageResource(R.drawable.ic_placeholder)
//        }
        if (imagenUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imagenUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(imgProducto)
        } else {
            imgProducto.setImageResource(R.drawable.ic_placeholder)
        }
    }
}

