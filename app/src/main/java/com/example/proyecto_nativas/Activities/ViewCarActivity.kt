package com.example.proyecto_nativas.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.R

class ViewCarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)

        val btnBack: Button = findViewById(R.id.btn_back)

        btnBack.setOnClickListener {
            val intent = Intent(this@ViewCarActivity, ProductActivity::class.java)
            startActivity(intent)

        }
}
}