package com.example.proyecto_nativas.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnSignup: Button = findViewById(R.id.btn_sign_up)

        btnSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        val btnForgotPassword: Button = findViewById(R.id.btn_forgot_password)

        btnForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, RecoverActivity::class.java)
            startActivity(intent)
        }

        val btnLogin: Button = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity, ProductActivity::class.java)
            startActivity(intent)
        }
    }
}
