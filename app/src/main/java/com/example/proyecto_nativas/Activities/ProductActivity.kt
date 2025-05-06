package com.example.proyecto_nativas.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.R
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {Basic}

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

        //setUp


        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup (email ?: "", provider ?: "")



    }

    private fun setup(email: String, provider: String) {

        val emailTextView = findViewById<TextView>(R.id.emailTextView)
//        val providerTextView = findViewById<TextView>(R.id.providerTextView)
        val logOutButton = findViewById<Button>(R.id.logOutButton)

        title = "Inicio"
        emailTextView.text= email
//        providerTextView.text= provider

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

    }

}