package com.example.proyecto_nativas.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.R

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val btn_viweCart: Button = findViewById(R.id.btn_view_car)

        btn_viweCart.setOnClickListener {
            val intent = Intent(this@ProductActivity, ViewCarActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}