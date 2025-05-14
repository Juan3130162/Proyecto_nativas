package com.example.proyecto_nativas.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyecto_nativas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtApellido: EditText
    private lateinit var edtEdad: EditText
    private lateinit var edtUsuario: EditText
    private lateinit var imgPreview: ImageView
    private lateinit var btnGuardar: Button
    private lateinit var btnTomarFoto: Button

    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val RQ_CAMERA = 101

    private var fotoBitmap: Bitmap? = null
    private var urlFoto: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        edtNombre = findViewById(R.id.edtNombre)
        edtApellido = findViewById(R.id.edtApellido)
        edtEdad = findViewById(R.id.edtEdad)
        edtUsuario = findViewById(R.id.edtUsuario)
        imgPreview = findViewById(R.id.imgPreview)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)

        cargarDatos()

        btnTomarFoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, RQ_CAMERA)
        }

        btnGuardar.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val apellido = edtApellido.text.toString()
            val edad = edtEdad.text.toString().toIntOrNull() ?: 0
            val usuario = edtUsuario.text.toString()

            if (uid == null) return@setOnClickListener

            // Si hay foto, primero subirla, luego guardar datos
            if (fotoBitmap != null) {
                val refFoto = storage.reference.child("fotos_perfil/perfil_$uid.jpg")
                val baos = ByteArrayOutputStream()
                fotoBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, baos)
                val datos = baos.toByteArray()

                refFoto.putBytes(datos)
                    .addOnSuccessListener {
                        refFoto.downloadUrl.addOnSuccessListener { uri ->
                            urlFoto = uri.toString()
                            guardarDatos(nombre, apellido, edad, usuario)
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                    }
            } else {
                guardarDatos(nombre, apellido, edad, usuario)
            }
        }
    }

    private fun guardarDatos(nombre: String, apellido: String, edad: Int, usuario: String) {
        if (uid == null) return

        val datos = mutableMapOf<String, Any>(
            "nombre" to nombre,
            "apellido" to apellido,
            "edad" to edad,
            "usuario" to usuario,
            "email" to FirebaseAuth.getInstance().currentUser?.email.orEmpty()
        )

        urlFoto?.let {
            datos["foto_url"] = it
        }

        db.collection("usuarios").document(uid).set(datos)
            .addOnSuccessListener {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar perfil", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarDatos() {
        if (uid == null) return

        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    edtNombre.setText(doc.getString("nombre") ?: "")
                    edtApellido.setText(doc.getString("apellido") ?: "")
                    edtEdad.setText(doc.getLong("edad")?.toString() ?: "")
                    edtUsuario.setText(doc.getString("usuario") ?: "")

                    val url = doc.getString("foto_url")
                    if (!url.isNullOrEmpty()) {
                        Glide.with(this).load(url).into(imgPreview)
                    }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                fotoBitmap = it
                imgPreview.setImageBitmap(it)
            }
        }
    }
}
