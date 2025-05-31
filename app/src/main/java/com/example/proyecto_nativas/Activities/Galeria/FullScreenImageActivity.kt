package com.example.proyecto_nativas.Activities.Galeria

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyecto_nativas.R

class FullScreenImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val imgFullScreen = findViewById<ImageView>(R.id.imgFullScreen)
        val url = intent.getStringExtra("imagen_url") ?: ""

        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_placeholder)
            .into(imgFullScreen)
    }
}
