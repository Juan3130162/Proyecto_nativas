package com.example.proyecto_nativas.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyecto_nativas.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtPrecio: EditText
    private lateinit var edtDescripcion: EditText
    private lateinit var imgPreview: ImageView
    private lateinit var btnGuardar: Button
    private lateinit var btnTomarFoto: Button

    private var productoId: String? = null
    private var imagenActualUrl: String? = null
    private var nuevaFotoBitmap: Bitmap? = null

    private val RQ_CAMARA = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        edtNombre = findViewById(R.id.edtNombreEditar)
        edtPrecio = findViewById(R.id.edtPrecioEditar)
        edtDescripcion = findViewById(R.id.edtDescripcionEditar)
        imgPreview = findViewById(R.id.imgPreviewEditar)
        btnGuardar = findViewById(R.id.btnGuardarCambios)
        btnTomarFoto = findViewById(R.id.btnTomarFotoEditar)

        // Obtener datos del intent
        productoId = intent.getStringExtra("producto_id")
        val nombre = intent.getStringExtra("producto_nombre") ?: ""
        val precio = intent.getIntExtra("producto_precio", 0)
        val descripcion = intent.getStringExtra("producto_descripcion") ?: ""
        imagenActualUrl = intent.getStringExtra("producto_imagen")

        edtNombre.setText(nombre)
        edtPrecio.setText(precio.toString())
        edtDescripcion.setText(descripcion)

        if (!imagenActualUrl.isNullOrEmpty()) {
            Glide.with(this).load(imagenActualUrl).into(imgPreview)
        }

        btnTomarFoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, RQ_CAMARA)
        }

        btnGuardar.setOnClickListener {
            guardarCambios()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_CAMARA && resultCode == Activity.RESULT_OK) {
            val foto = data?.extras?.get("data") as? Bitmap
            foto?.let {
                nuevaFotoBitmap = it
                imgPreview.setImageBitmap(it)
            }
        }
    }

    private fun guardarCambios() {
        val nombre = edtNombre.text.toString()
        val precio = edtPrecio.text.toString().toIntOrNull() ?: 0
        val descripcion = edtDescripcion.text.toString()

        if (productoId == null) {
            Toast.makeText(this, "ID del producto no válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (nuevaFotoBitmap != null) {
            subirNuevaImagen(nombre, precio, descripcion)
        } else {
            actualizarProducto(nombre, precio, descripcion, imagenActualUrl)
        }
    }

    private fun subirNuevaImagen(nombre: String, precio: Int, descripcion: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val nombreArchivo = "IMG_EDITADA_${UUID.randomUUID()}.jpg"
        val refImagen = storageRef.child("imagenes_productos/$nombreArchivo")

        val baos = ByteArrayOutputStream()
        nuevaFotoBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val datos = baos.toByteArray()

        refImagen.putBytes(datos)
            .addOnSuccessListener {
                refImagen.downloadUrl.addOnSuccessListener { uri ->
                    actualizarProducto(nombre, precio, descripcion, uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al subir imagen", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarProducto(nombre: String, precio: Int, descripcion: String, imagenUrl: String?) {
        val db = FirebaseFirestore.getInstance()
        val datosActualizados = hashMapOf<String, Any>(
            "nombre" to nombre,
            "precio" to precio,
            "descripcion" to descripcion,
            "imagen_url" to (imagenUrl ?: "")
        )

        db.collection("productos").document(productoId!!).update(datosActualizados)
            .addOnSuccessListener {
                Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
    }
}
