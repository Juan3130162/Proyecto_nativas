package com.example.proyecto_nativas.Activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyecto_nativas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetallesDeProductosActivity : AppCompatActivity() {

    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button

    private var productoId: String? = null
    private var nombre = ""
    private var descripcion = ""
    private var precio = 0
    private var imagenUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_de_productos)

        val imgProducto = findViewById<ImageView>(R.id.imgDetalleProducto)
        val txtNombre = findViewById<TextView>(R.id.txtNombreDetalle)
        val txtPrecio = findViewById<TextView>(R.id.txtPrecioDetalle)
        val txtDescripcion = findViewById<TextView>(R.id.txtDescripcionDetalle)

        btnEditar = findViewById(R.id.btnEditarProducto)
        btnEliminar = findViewById(R.id.btnEliminarProducto)

        // Datos recibidos
        nombre = intent.getStringExtra("producto_nombre") ?: "Sin nombre"
        precio = intent.getIntExtra("producto_precio", 0)
        descripcion = intent.getStringExtra("producto_descripcion") ?: ""
        imagenUrl = intent.getStringExtra("producto_imagen") ?: ""
        productoId = intent.getStringExtra("producto_id")

        txtNombre.text = nombre
        txtPrecio.text = "$$precio"
        txtDescripcion.text = descripcion

        if (imagenUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imagenUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(imgProducto)
        } else {
            imgProducto.setImageResource(R.drawable.ic_placeholder)
        }

        verificarSiEsAdmin()

        btnEditar.setOnClickListener {
            val intent = Intent(this, EditarProductoActivity::class.java).apply {
                putExtra("producto_id", productoId)
                putExtra("producto_nombre", nombre)
                putExtra("producto_precio", precio)
                putExtra("producto_descripcion", descripcion)
                putExtra("producto_imagen", imagenUrl)
            }
            startActivity(intent)
        }

        btnEliminar.setOnClickListener {
            mostrarConfirmacionEliminacion()
        }
    }

    private fun verificarSiEsAdmin() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance().collection("usuarios")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val esAdmin = doc.getBoolean("admin") ?: false
                if (esAdmin) {
                    btnEditar.visibility = View.VISIBLE
                    btnEliminar.visibility = View.VISIBLE
                } else {
                    btnEditar.visibility = View.GONE
                    btnEliminar.visibility = View.GONE
                }
            }
    }

    private fun mostrarConfirmacionEliminacion() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este producto? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> eliminarProducto() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarProducto() {
        val id = productoId ?: return

        FirebaseFirestore.getInstance().collection("productos")
            .document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ListaProductosActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
            }
    }

}

