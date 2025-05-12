package com.example.proyecto_nativas.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream

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
    private val RQ_CAMERA = 101

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

            if (uid != null) {
                val datos = mapOf(
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "edad" to edad,
                    "usuario" to usuario,
                    "email" to FirebaseAuth.getInstance().currentUser?.email
                )

                db.collection("usuarios").document(uid).set(datos)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar perfil", Toast.LENGTH_SHORT).show()
                    }
            }
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

                    val ruta = getExternalFilesDir(null)?.absolutePath + "/foto_perfil.jpg"
                    val archivo = File(ruta)
                    if (archivo.exists()) {
                        imgPreview.setImageBitmap(android.graphics.BitmapFactory.decodeFile(ruta))
                    }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                imgPreview.setImageBitmap(it)
                guardarFotoLocal(it)
            }
        }
    }

    private fun guardarFotoLocal(bitmap: Bitmap) {
        val ruta = getExternalFilesDir(null)?.absolutePath + "/foto_perfil.jpg"
        val archivo = File(ruta)
        val stream = FileOutputStream(archivo)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        stream.flush()
        stream.close()
    }
}
