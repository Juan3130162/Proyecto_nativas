package com.example.proyecto_nativas.Activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_nativas.Activities.LoginAlternativo.InicioSesionActivity
import com.example.proyecto_nativas.R
import com.google.firebase.auth.FirebaseAuth
import com.example.proyecto_nativas.models.ProviderType


class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        val btnSignup: Button = findViewById(R.id.btn_sign_up)
//
//        btnSignup.setOnClickListener {
//            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//            startActivity(intent)
//        }

        val btnForgotPassword: Button = findViewById(R.id.btn_forgot_password)

        btnForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, RecoverActivity::class.java)
            startActivity(intent)
        }

//        val btnLogin: Button = findViewById(R.id.btn_login)

//        btnLogin.setOnClickListener {
//            val intent = Intent(this@LoginActivity, ProductActivity::class.java)
//            startActivity(intent)
//        }
        val btnLoginAlt: Button = findViewById(R.id.bnt_inicio_sesion_alt)

        btnLoginAlt.setOnClickListener {
            val intent = Intent(this@LoginActivity, InicioSesionActivity::class.java)
            startActivity(intent)
        }

        //registrarse
        Setup ()
    }



    private fun Setup() {

        val edt_email_login = findViewById<EditText>(R.id.edt_email_login)
        val edt_email_password = findViewById<EditText>(R.id.edt_email_password)
        val btn_sign_up = findViewById<Button>(R.id.btn_sign_up)
        val btn_login = findViewById<Button>(R.id.btn_login)

        this.title = "Autenticación"


        btn_sign_up.setOnClickListener {
            if (edt_email_login.text.isNotEmpty() && edt_email_password.text.isNotEmpty()) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(edt_email_login.text.toString(), edt_email_password.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email ?: "", ProviderType.Basic)
                    } else {
                        showAlert()
                    }
                }
            }
        }

        btn_login.setOnClickListener {
            if (edt_email_login.text.isNotEmpty() && edt_email_password.text.isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(edt_email_login.text.toString(), edt_email_password.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email ?: "", ProviderType.Basic)
                        } else {
                            showAlert()
                        }
                    }
            }
        }
    }
    private fun showAlert (){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("error")
        builder.setMessage("hay un error al autenticar")
        builder.setPositiveButton("aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


        private fun showHome(email: String, provider: ProviderType) {

        val homeIntent = Intent (this, ProductActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

}






