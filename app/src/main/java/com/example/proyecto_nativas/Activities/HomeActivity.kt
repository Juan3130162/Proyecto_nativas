package com.example.proyecto_nativas.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.Activities.LoginAlternativo.InicioSesionActivity
import com.example.proyecto_nativas.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnComenzar: Button = findViewById(R.id.btn_comenzar)

        btnComenzar.setOnClickListener {
            val intent = Intent(this@HomeActivity, InicioSesionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
