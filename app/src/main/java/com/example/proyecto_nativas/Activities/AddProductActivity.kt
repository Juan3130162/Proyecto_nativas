package com.example.proyecto_nativas.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.proyecto_nativas.R
import com.example.proyecto_nativas.models.Producto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class AddProductActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtPrecio: EditText
    private lateinit var edtDescripcion: EditText
    private lateinit var imgPreview: ImageView
    private lateinit var btnGuardar: Button
    private lateinit var btnTomarFoto: Button

    private lateinit var currentPhotoPath: String
    private var photoUri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        edtNombre = findViewById(R.id.edtNombre)
        edtPrecio = findViewById(R.id.edtPrecio)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        imgPreview = findViewById(R.id.imgPreview)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)

        btnTomarFoto.setOnClickListener {
            abrirCamara()
        }

        btnGuardar.setOnClickListener {
            subirImagenYGuardarProducto()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun abrirCamara() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (_: IOException) {
                null
            }

            photoFile?.also {
                photoUri = FileProvider.getUriForFile(
                    this,
                    "com.example.proyecto_nativas.fileprovider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)


            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imgPreview.setImageURI(photoUri)
        }
    }

    private fun subirImagenYGuardarProducto() {
        val nombre = edtNombre.text.toString()
        val descripcion = edtDescripcion.text.toString()
        val precio = edtPrecio.text.toString().toIntOrNull() ?: 0

        if (photoUri == null) {
            Toast.makeText(this, "Debes tomar una foto", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef = FirebaseStorage.getInstance().reference
        val nombreImagen = "IMG_${UUID.randomUUID()}.jpg"
        val fotoRef = storageRef.child("imagenes_productos/$nombreImagen")

        fotoRef.putFile(photoUri!!)
            .addOnSuccessListener {
                fotoRef.downloadUrl.addOnSuccessListener { uri ->
                    val imagenUrl = uri.toString()

                    val producto = Producto(
                        id = "",
                        nombre = nombre,
                        imagen_url = imagenUrl,
                        descripcion = descripcion,
                        precio = precio
                    )

                    val db = FirebaseFirestore.getInstance()
                    db.collection("productos")
                        .add(producto)
                        .addOnSuccessListener { documentRef ->
                            val idGenerado = documentRef.id
                            db.collection("productos").document(idGenerado)
                                .update("id", idGenerado)

                            Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar en Firestore", Toast.LENGTH_SHORT).show()
                            Log.e("Firestore", "Error:", it)
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseStorage", "Error:", it)
            }
    }
}
